package my.whx.music.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import me.wcy.music.R;
import my.whx.music.adapter.FragmentAdapter;
import my.whx.music.constants.Extras;
import my.whx.music.constants.Keys;
import my.whx.music.executor.ControlPanel;
import my.whx.music.executor.NaviMenuExecutor;
import my.whx.music.executor.WeatherExecutor;
import my.whx.music.fragment.LocalMusicFragment;
import my.whx.music.fragment.PlayFragment;
import my.whx.music.fragment.SheetListFragment;
import my.whx.music.service.AudioPlayer;
import my.whx.music.service.QuitTimer;
import my.whx.music.utils.PermissionReq;
import my.whx.music.utils.SystemUtils;
import my.whx.music.utils.ToastUtils;
import my.whx.music.utils.binding.Bind;

@SuppressWarnings("ALL")
public class MusicActivity extends BaseActivity implements View.OnClickListener, QuitTimer.OnTimerListener,
        NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    //抽屉式导航栏视图
    @Bind(R.id.drawer_layout)
    private DrawerLayout drawerLayout;
    //导航栏
    @Bind(R.id.navigation_view)
    private NavigationView navigationView;
    //菜单
    @Bind(R.id.iv_menu)
    private ImageView ivMenu;
    //搜索
    @Bind(R.id.iv_search)
    private ImageView ivSearch;
    //本地音乐
    @Bind(R.id.tv_local_music)
    private TextView tvLocalMusic;
    //在线音乐
    @Bind(R.id.tv_online_music)
    private TextView tvOnlineMusic;
    //页面切换
    @Bind(R.id.viewpager)
    private ViewPager mViewPager;
    //播放条
    @Bind(R.id.fl_play_bar)
    private FrameLayout flPlayBar;
    //导航头
    private View vNavigationHeader;
    //在线音乐页面
    private LocalMusicFragment mLocalMusicFragment;
    //音乐列表项
    private SheetListFragment mSheetListFragment;
    //播放页面
    private PlayFragment mPlayFragment;
    //控制面板
    private ControlPanel controlPanel;
    //导航菜单执行者
    private NaviMenuExecutor naviMenuExecutor;
    //菜单项
    private MenuItem timerItem;
    //是否播放
    private boolean isPlayFragmentShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
    }
    //绑定服务
    @Override
    protected void onServiceBound() {
        //加载初始视图
        setupView();
        //更新天气信息
        updateWeather();
        //创建控制面板对象
        controlPanel = new ControlPanel(flPlayBar);
        //创建导航菜单执行者对象
        naviMenuExecutor = new NaviMenuExecutor(this);
        //自动播放添加播放进度监听器
        AudioPlayer.get().addOnPlayEventListener(controlPanel);
        //停止播放时间更新添加监听器
        QuitTimer.get().setOnTimerListener(this);
        //调用parseIntent方法
        parseIntent();
    }
    //如果一个Activity已经启动过，并且存在当前应用的Activity任务栈中，启动模式为singleTask，
    //singleInstance或singleTop(此时已在任务栈顶端)，那么在此启动或回到这个Activity的时候，
    //不会创建新的实例，也就是不会执行onCreate方法，而是执行onNewIntent方法
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        parseIntent();
    }

    private void setupView() {
        //添加导航头
        vNavigationHeader = LayoutInflater.from(this).inflate(R.layout.navigation_header, navigationView, false);
        navigationView.addHeaderView(vNavigationHeader);
        //创建本地音乐页面的fragment
        mLocalMusicFragment = new LocalMusicFragment();
        //创建音乐列表项页面的frament
        mSheetListFragment = new SheetListFragment();
        //创建fragmentadapter的对象adapter
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        //对本地音乐页面的fragment添加监听器
        adapter.addFragment(mLocalMusicFragment);
        //对音乐列表项页面的fragment添加监听器
        adapter.addFragment(mSheetListFragment);
        //设置页面切换viewpaper的监听器
        mViewPager.setAdapter(adapter);
        //设置本地音乐点击状态为true
        tvLocalMusic.setSelected(true);
        //设置菜单的监听器
        ivMenu.setOnClickListener(this);
        //设置搜索的监听器
        ivSearch.setOnClickListener(this);
        //设置本地音乐的监听器
        tvLocalMusic.setOnClickListener(this);
        //设置在线音乐的监听器
        tvOnlineMusic.setOnClickListener(this);
        //设置播放条的监听器
        flPlayBar.setOnClickListener(this);
        //设置切换页面的监听器
        mViewPager.addOnPageChangeListener(this);
        //设置导航视图的监听器
        navigationView.setNavigationItemSelectedListener(this);
    }
    //更新天气的方法
    private void updateWeather() {
        //设置权限信息
        PermissionReq.with(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                //权限申请结果
                .result(new PermissionReq.Result() {
                    @Override
                    //得到授权时
                    public void onGranted() {
                        new WeatherExecutor(MusicActivity.this, vNavigationHeader).execute();
                    }
                    //授权取消时
                    @Override
                    public void onDenied() {
                        //输出没有位置信息，无法更新天气
                        ToastUtils.show(R.string.no_permission_location);
                    }
                })
                //请求
                .request();
    }
    //parseIntent的方法
    private void parseIntent() {
        //此方法在MainActivity中调用
        Intent intent = getIntent();
        //对intent对象设置extras接口属性
        if (intent.hasExtra(Extras.EXTRA_NOTIFICATION)) {
            //调用播放视图的方法
            showPlayingFragment();
            //设置Intent
            setIntent(new Intent());
        }
    }
    //计时的方法
    @Override
    public void onTimer(long remain) {
        //如果计时项为空
        if (timerItem == null) {
            //获取导航菜单视图
            timerItem = navigationView.getMenu().findItem(R.id.action_timer);
        }
        //获取title信息
        String title = getString(R.string.menu_timer);
        //设置时间格式信息
        timerItem.setTitle(remain == 0 ? title : SystemUtils.formatTime(title + "(mm:ss)", remain));
    }
    //单击的方法
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //加载菜单项
            case R.id.iv_menu:
                //打开抽屉式导航栏
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            //加载搜索
            case R.id.iv_search:
                //开启SearchMusicActivity的跳转
                startActivity(new Intent(this, SearchMusicActivity.class));
                break;
            //加载本地音乐页面的fragment
            case R.id.tv_local_music:
                //设置viewpaper的页面为1
                mViewPager.setCurrentItem(0);
                break;
            //加载在线音乐页面的fragment
            case R.id.tv_online_music:
                //设置viewpaper的页面为2
                mViewPager.setCurrentItem(1);
                break;
            //加载播放条
            case R.id.fl_play_bar:
                //开启播放页面的视图
                showPlayingFragment();
                break;
        }
    }
    //导航列表选中的方法
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //关闭抽屉式导航栏
        drawerLayout.closeDrawers();
        //设置延迟500ms
        handler.postDelayed(() -> item.setChecked(false), 500);
        //导购菜单执行设置选中
        return naviMenuExecutor.onNavigationItemSelected(item);
    }
    //页面滚动的方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    //页面选中的方法
    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            //点击本地音乐的状态设置为true
            tvLocalMusic.setSelected(true);
            //点击在线音乐的状态设置为false
            tvOnlineMusic.setSelected(false);
        } else {
            //点击本地音乐的状态设置为false
            tvLocalMusic.setSelected(false);
            //点击在线音乐的状态设置为true
            tvOnlineMusic.setSelected(true);
        }
    }
    //页面滚动状态改变的方法
    @Override
    public void onPageScrollStateChanged(int state) {
    }
    //开启播放视图的方法
    private void showPlayingFragment() {
        if (isPlayFragmentShow) {
            return;
        }
        //开启fragment事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //设置滑动效果
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        //判断是否加载播放视图
        if (mPlayFragment == null) {
            //创建播放视图的对象
            mPlayFragment = new PlayFragment();
            //替换为播放视图
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            //对事务显示播放视图
            ft.show(mPlayFragment);
        }
        //提交事务
        ft.commitAllowingStateLoss();
        //把处于播放视图的状态设置为true
        isPlayFragmentShow = true;
    }
    //隐藏播放视图的方法
    private void hidePlayingFragment() {
        //创建事务并且开启
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //设置事务的加载动画
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        //隐藏事务的播放事务
        ft.hide(mPlayFragment);
        //提交事务
        ft.commitAllowingStateLoss();
        //把处于播放视图的状态设置为false
        isPlayFragmentShow = false;
    }
    //监听用户是否点击back按键的方法
    @Override
    public void onBackPressed() {
        //如果播放视图或处于播放视图的页面不为空
        if (mPlayFragment != null && isPlayFragmentShow) {
            //隐藏当前播放视图
            hidePlayingFragment();
            return;
        }
        //如果抽屉式导航栏打开
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //关闭抽屉式导航栏
            drawerLayout.closeDrawers();
            return;
        }
        //调用点击back按键的方法
        super.onBackPressed();
    }
    //保存状态的方法
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Keys.VIEW_PAGER_INDEX, mViewPager.getCurrentItem());
        //保存本地音乐的状态
        mLocalMusicFragment.onSaveInstanceState(outState);
        //保存音乐列表项的状态
        mSheetListFragment.onSaveInstanceState(outState);
    }
    //销毁状态的方法
    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        mViewPager.post(() -> {
            mViewPager.setCurrentItem(savedInstanceState.getInt(Keys.VIEW_PAGER_INDEX), false);
            //销毁本地音乐的状态
            mLocalMusicFragment.onRestoreInstanceState(savedInstanceState);
            //销毁音乐列表项的状态
            mSheetListFragment.onRestoreInstanceState(savedInstanceState);
        });
    }
    //释放activity的方法
    @Override
    protected void onDestroy() {
        //获取自动播放的控制面板
        AudioPlayer.get().removeOnPlayEventListener(controlPanel);
        //获取更新后的停止时间
        QuitTimer.get().setOnTimerListener(null);
        //释放当前的activity
        super.onDestroy();
    }
}
