package loon.action.sprite;

import loon.LObject;
import loon.LTexture;
import loon.canvas.LColor;
import loon.geom.RectBox;
import loon.opengl.GLEx;
import loon.utils.MathUtils;
import loon.utils.timer.LTimer;

public class Blood extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class Drop {
		public float x, y, xspeed, yspeed;
	}

	private float xSpeed, ySpeed;

	private LTimer timer;

	private int step, limit;

	private Drop[] drops;

	private boolean visible;

	private LColor color;

	public Blood(int x, int y) {
		this(LColor.red, x, y);
	}

	public Blood(LColor c, int x, int y) {
		this.setLocation(x, y);
		this.color = c;
		this.timer = new LTimer(20);
		this.drops = new Drop[20];
		this.limit = 50;
		for (int i = 0; i < drops.length; ++i) {
			setBoolds(i, x, y, 6.f * (MathUtils.random() - 0.5f), -2.0f
					* MathUtils.random());
		}
		this.xSpeed = 0F;
		this.ySpeed = 0.5F;
		this.step = 0;
		this.visible = true;
	}

	public void setBoolds(int index, float x, float y, float xs, float ys) {
		if (index > drops.length - 1) {
			return;
		}
		drops[index] = new Drop();
		drops[index].x = x;
		drops[index].y = y;
		drops[index].xspeed = xs;
		drops[index].yspeed = ys;
	}

	public void update(long elapsedTime) {
		if (timer.action(elapsedTime)) {
			for (int i = 0; i < drops.length; ++i) {
				drops[i].xspeed += xSpeed;
				drops[i].yspeed += ySpeed;
				drops[i].x -= drops[i].xspeed;
				drops[i].y += drops[i].yspeed;
			}
			step++;
			if (step > limit) {
				this.visible = false;
			}
		}
	}

	public void setDelay(long delay) {
		timer.setDelay(delay);
	}

	public long getDelay() {
		return timer.getDelay();
	}

	private int tmpColor;
	
	public void createUI(GLEx g) {
		if (!visible) {
			return;
		}
		tmpColor = g.color();
		if (_alpha > 0 && _alpha < 1) {
			g.setAlpha(_alpha);
		}
		g.setColor(color);
		for (int i = 0; i < drops.length; ++i) {
			g.fillOval((int) drops[i].x, (int) drops[i].y, 2, 2);
		}
		if (_alpha != 1f) {
			g.setAlpha(1f);
		}
		g.setColor(tmpColor);
	}

	public LColor getColor() {
		return color;
	}

	public void setColor(LColor color) {
		this.color = color;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public LTexture getBitmap() {
		return null;
	}

	public RectBox getCollisionBox() {
		return null;
	}

	public float getXSpeed() {
		return xSpeed;
	}

	public void setXSpeed(float speed) {
		this.xSpeed = speed;
	}

	public float getYSpeed() {
		return ySpeed;
	}

	public void setYSpeed(float speed) {
		this.ySpeed = speed;
	}

	public float getHeight() {
		return 0;
	}

	public float getWidth() {
		return 0;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void close() {

	}

}
