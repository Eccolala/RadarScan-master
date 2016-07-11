package mr_immortalz.com.modelqq.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by woops on 16-7-11.
 */
public class ParticleSystem {
    private List<Particle> particles = new ArrayList<>();
    private int border_width = 20;
    private Paint paint = new Paint();
    private Random random;

    public ParticleSystem(int particlesCount) {
        paint.setColor(Color.RED);
        random = new Random();
        for (int i = 0; i < particlesCount; i++) {
            Particle particle = new Particle(100 + 100 * random.nextFloat(), 500);
            particle.xv = 0.5f - 1 * random.nextFloat();
            particles.add(particle);
        }
    }

    public void onDraw(Canvas canvas) {
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            paint.setAlpha(particle.alpha);
            canvas.drawRect(particle.getX(), particle.getY(), particle.getX() + border_width, particle.getY() + border_width, paint);
        }
    }

    /**
     * 粒子集合更新方法
     *
     * @param wind wind是风，0时代表无风，粒子不偏移
     */
    public void update(float wind) {
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            if (particle.xv > 0) {
                //当初始加速度为正，就一直为正
                particle.xv += 0.01f + wind;
            } else {
                //当初始加速度为负，就一直为正
                particle.xv += -0.01f + wind;
            }
            particle.yv = particle.yv + 0.1f;
            particle.setX(particle.getX() + particle.xv);
            particle.setY(particle.getY() - particle.yv);
        }
        List<Particle> list = new ArrayList<>();
        list.addAll(particles);
        for (Particle particle : list) {
            if (particle.getY() < 200) {
                particles.remove(particle);
            }
        }
        for (int i = 0; i < 5; i++) {
            Particle particle = new Particle(100 + 100 * random.nextFloat(), 500);

            particle.xv = 0.5f - 1 * random.nextFloat();
            particles.add(particle);
        }
    }
}
