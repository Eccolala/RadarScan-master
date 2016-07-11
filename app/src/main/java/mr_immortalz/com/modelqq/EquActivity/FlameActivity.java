package mr_immortalz.com.modelqq.EquActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import mr_immortalz.com.modelqq.R;

public class FlameActivity extends AppCompatActivity {
    private Button button;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flame);

        mContext = this;
        Log.d("Jay", "连接成功");

        Thread mReceiveData = new MyReceiveDataThread();
        mReceiveData.start();

    }


    class MyReceiveDataThread extends Thread {

        public void run() {

            while (true) {
                ServerSocket serverSocket = null;
                try {
                    Log.d("Jay", "开始连接");

                    serverSocket = new ServerSocket(8888);
                    Log.d("Jay", "连接成功");

                    Socket socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                    startActivity(new Intent(this,FlameActivity.class));

            }

        }


    }


}
