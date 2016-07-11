package mr_immortalz.com.modelqq.EquActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

import mr_immortalz.com.modelqq.R;
import mr_immortalz.com.modelqq.utils.JsonParser;
import mr_immortalz.com.modelqq.view.WaveformView;

public class FirstEqu extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    private String date = "请先选择日期";

    private static final int MSG_GET_VOLUME = 0x1001;
    private WaveformView mWaveformView;
    //private GLWaveformView mGLWaveformView;
    private Handler mHandler;
//    private RecordThread mRecordThread;

    private Handler handler_open;
    private Handler handler_close;

    private Runnable runnable;
    private Runnable runnable2;

    //设置存储容器
    private SharedPreferences mSharedPreferences;
    //设置语音识别对象
    private SpeechRecognizer mIat;
    //迷之Toast,删掉会出现奇怪的错误
    private Toast mToast;

    //识别结果
    private String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_equ);

        SpeechUtility.createUtility(this, "appid=570b2ef9");

        //初始化语音识别对象
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        mSharedPreferences = getSharedPreferences("com.iflytek.setting", Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);




    }

    //设置返回值参数
    int ret = 0;

    //普通的听写功能,监听事件
    public void startClick(View view) {
        setParams();
        mIat.stopListening();
        ret = mIat.startListening(mRecognizerListener);

        //貌似没有卵用
        if (ret != ErrorCode.SUCCESS) {
            showTip("听写失败,错误码：" + ret);
        } else {
//            showTip("请开始BB");
        }
    }

    //设置参数
    private void setParams() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
        // 设置引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");


    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            SuperActivityToast.create(FirstEqu.this, "请开始说话",
                    SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            text = JsonParser.parseIatResult(results.getResultString());

            int num = text.length();

            if (num >=2 ){
                if (text.substring(0,2).equals("打开")){
                    Log.d("Jay","open");
                    SuperActivityToast.create(FirstEqu.this, "正在打开,请稍后...^_^",
                            SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
                    Thread device = new MySendCommondThread('1');
                    device.start();
                }else if (text.substring(0,2).equals("关闭")){
                    Log.d("Jay","close");
                    SuperActivityToast.create(FirstEqu.this, "正在关闭,请稍后...^o^",
                            SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
                    Thread device = new MySendCommondThread('0');
                    device.start();

                }else {
                    SuperActivityToast.create(FirstEqu.this, "矮油,我没有听清楚,再说一遍嘛　T_T",
                            SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
                }
            }


        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            showTip("当前正在说话，音量大小：" + volume);

            Log.d("TAG", "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();
    }



    public void Play(View view) throws IOException {
        SuperActivityToast.create(FirstEqu.this, "正在打开微波炉,请稍后......",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
        Thread device = new MySendCommondThread('1');
        device.start();
    }

    public void Stop(View view) {
        SuperActivityToast.create(FirstEqu.this, "正在关闭微波炉,请稍后......",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
        Thread device = new MySendCommondThread('0');
        device.start();
    }

    public void Pause(View view) {

        SuperActivityToast.create(FirstEqu.this, "正在暂停微波炉,请稍后......",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = +year + "/" + (++monthOfYear) + "/" + dayOfMonth;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "您已成功预约,微波炉将在如下时间启动 : " + "\n" + date + "\n" + hourString + ":" + minuteString + ":" + secondString;
        SuperActivityToast.create(FirstEqu.this, time,
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
    }

    public void TimePick(View view) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                FirstEqu.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.is24HourMode();
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    public void DataPick(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                FirstEqu.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("TAG", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };
    private void showTip(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }

}

class MySendCommondThread extends Thread {
    private String hostName = "192.168.1.193";  //要连接的服务端IP地址
    private int portNumber = 7373;   //要连接的服务端对应的监听端口
    private char commond;

    public MySendCommondThread(char commond) {
        this.commond = commond;
    }

    public void run() {
        //实例化Socket
        try {
            Socket client = new Socket(hostName, portNumber);
            Writer writer = new OutputStreamWriter(client.getOutputStream(),"UTF-8");

            writer.write(commond);
            writer.flush();//写完后要记得flush
            writer.close();
            client.close();
        } catch (UnknownHostException e) {
        } catch (IOException e) {
        }
    }
}