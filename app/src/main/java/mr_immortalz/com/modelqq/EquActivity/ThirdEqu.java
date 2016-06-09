package mr_immortalz.com.modelqq.EquActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import mr_immortalz.com.modelqq.R;

public class ThirdEqu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_equ);
    }


    public void Play(View view) {
        SuperActivityToast.create(ThirdEqu.this, "正在打开烤箱,请稍后......",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
    }

    public void Stop(View view) {
        SuperActivityToast.create(ThirdEqu.this, "正在关闭烤箱,请稍后......",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
    }

    public void Pause(View view) {
        SuperActivityToast.create(ThirdEqu.this, "正在暂停烤箱,请稍后......",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
    }
}
