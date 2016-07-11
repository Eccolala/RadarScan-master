package mr_immortalz.com.modelqq;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

public class VideoAty extends AppCompatActivity {
    private VideoView vv_video;
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        vv_video = (VideoView) findViewById(R.id.vv_video);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.flame;
        vv_video.setVideoURI(Uri.parse(uri));
        vv_video.start();


        vv_video.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }
}
