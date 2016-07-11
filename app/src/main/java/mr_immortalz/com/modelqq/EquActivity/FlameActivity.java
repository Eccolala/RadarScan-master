package mr_immortalz.com.modelqq.EquActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        button = (Button) findViewById(R.id.button1);

        mContext = this;

        Thread mReceiveData = new MyReceiveDataThread();
        mReceiveData.start();

    }





    class MyReceiveDataThread extends Thread {

        private int port;
        private Socket socket;
        private ServerSocket serverSocket;


        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    socket = serverSocket.accept();

//                    startActivity(new Intent(FlameActivity.this,));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }



}
