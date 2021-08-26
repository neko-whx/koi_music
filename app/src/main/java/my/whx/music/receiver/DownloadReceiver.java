package my.whx.music.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.io.File;

import me.wcy.music.R;
import my.whx.music.application.AppCache;
import my.whx.music.executor.DownloadMusicInfo;
import my.whx.music.utils.ToastUtils;
import my.whx.music.utils.id3.ID3TagUtils;
import my.whx.music.utils.id3.ID3Tags;

/**
 * 下载完成广播接收器
 * Created by hzwangchenyan on 2015/12/30.
 */
public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        DownloadMusicInfo downloadMusicInfo = AppCache.get().getDownloadList().get(id);
        if (downloadMusicInfo != null) {
            ToastUtils.show(context.getString(R.string.download_success, downloadMusicInfo.getTitle()));

            String musicPath = downloadMusicInfo.getMusicPath();
            String coverPath = downloadMusicInfo.getCoverPath();
            if (!TextUtils.isEmpty(musicPath) && !TextUtils.isEmpty(coverPath)) {
                // 设置专辑封面
                File musicFile = new File(musicPath);
                File coverFile = new File(coverPath);
                if (musicFile.exists() && coverFile.exists()) {
                    ID3Tags id3Tags = new ID3Tags.Builder().setCoverFile(coverFile).build();
                    ID3TagUtils.setID3Tags(musicFile, id3Tags, false);
                }
            }
        }
    }
}
