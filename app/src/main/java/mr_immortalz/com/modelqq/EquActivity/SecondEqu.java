package mr_immortalz.com.modelqq.EquActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import mr_immortalz.com.modelqq.R;

public class SecondEqu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_equ);
    }


    public void Play(View view) {
        SuperActivityToast.create(SecondEqu.this, "正在打开电磁炉,请稍后......",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
    }

    public void Stop(View view) {
        SuperActivityToast.create(SecondEqu.this, "正在关闭电磁炉,请稍后......",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
    }

    public void Pause(View view) {
        SuperActivityToast.create(SecondEqu.this, "正在暂停电磁炉,请稍后......",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
    }
}

