package mr_immortalz.com.modelqq.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by woops on 16-7-11.
 */
public class MyView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder holder;
    private MyThread thread;
    public boolean running = true;
    private ParticleSystem particleSystem;
    private Paint paint;
    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        holder = getHolder();
        holder.addCallback(this);
        thread = new MyThread();
        particleSystem = new ParticleSystem(50);
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        running = true;
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
    }

    class  MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            while(true){
                if(running){
                    drawUI();
                }
            }
        }
    }

    private void drawUI(){
        Canvas canvas = holder.lockCanvas();

        if(canvas==null){
            return;
        }
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);
        particleSystem.onDraw(canvas);
        particleSystem.update(new Random().nextFloat()*0.18f);
        holder.unlockCanvasAndPost(canvas);
    }
}