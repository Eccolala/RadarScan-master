package mr_immortalz.com.modelqq.EquActivity;

import android.content.DialogInterface;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
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
import mr_immortalz.com.modelqq.view.WaveformView;

public class FirstEqu extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    private String date = "请先选择日期";

    private static final int MSG_GET_VOLUME = 0x1001;
    private WaveformView mWaveformView;
    //private GLWaveformView mGLWaveformView;
    private Handler mHandler;
    private RecordThread mRecordThread;

    private Handler handler_open;
    private Handler handler_close;

    private Runnable runnable;
    private Runnable runnable2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_equ);


        mWaveformView = (WaveformView) findViewById(R.id.waveform_view);
        //mGLWaveformView = (GLWaveformView) findViewById(R.id.gl_waveform_view);
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == MSG_GET_VOLUME) {
                    update((Float) msg.obj);
                }
                return true;
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecordThread = new RecordThread(mHandler);
        mRecordThread.start();
        //mGLWaveformView.onResume();
    }

    @Override
    protected void onPause() {
        //mGLWaveformView.onPause();
        if (mRecordThread != null) {
            mRecordThread.pause();
            mRecordThread = null;
        }
        super.onPause();
    }

    private void update(final float volume) {
        mWaveformView.post(new Runnable() {
            @Override
            public void run() {
                mWaveformView.updateAmplitude(volume * 0.1f / 500);
            }
        });
    }

    static class RecordThread extends Thread {
        private AudioRecord ar;
        private int bs;
        private final int SAMPLE_RATE_IN_HZ = 8000;
        private boolean isRun = false;
        private Handler mHandler;

        public RecordThread(Handler handler) {
            super();
            bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            ar = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE_IN_HZ,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bs);
            mHandler = handler;
        }

        public void run() {
            super.run();
            ar.startRecording();
            byte[] buffer = new byte[bs];
            isRun = true;
            while (running()) {
                int r = ar.read(buffer, 0, bs);
                int v = 0;
                for (byte aBuffer : buffer) {
                    v += aBuffer * aBuffer;
                }

                Message msg = mHandler.obtainMessage(MSG_GET_VOLUME, v * 1f / r);
                mHandler.sendMessage(msg);
            }
            ar.stop();
        }

        public synchronized void pause() {
            isRun = false;
        }

        private synchronized boolean running() {
            return isRun;
        }

        public void start() {
            if (!running()) {
                super.start();
            }
        }
    }

    public void Play(View view) throws IOException {
        SuperActivityToast.create(FirstEqu.this, "正在打开微波炉,请稍后......",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
        Thread device = new MySendCommondThread("1");
        device.start();
    }

    public void Stop(View view) {
        SuperActivityToast.create(FirstEqu.this, "正在关闭微波炉,请稍后......",
                SuperToast.Duration.SHORT, Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
        Thread device = new MySendCommondThread("0");
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
}

class MySendCommondThread extends Thread {
    private String hostName = "192.168.1.101";  //要连接的服务端IP地址
    private int portNumber = 8899;   //要连接的服务端对应的监听端口
    private String commond;

    public MySendCommondThread(String commond) {
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