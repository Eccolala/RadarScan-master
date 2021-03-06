package mr_immortalz.com.modelqq.view;

/**
 * Created by woops on 16-7-11.
 */
public class Particle {
    //火焰粒子坐标
    private float x;
    private float y;
    public float xv = 1;//x轴加速度
    public float yv = 1;//y轴加速度
    public int alpha;

    public Particle(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        //500-200为火焰的y轴左边范围，所以根据这个计算透明度
        alpha = (int) ((y-200)*255/300);
    }
}
