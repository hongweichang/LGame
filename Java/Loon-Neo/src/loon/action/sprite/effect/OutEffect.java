package loon.action.sprite.effect;

import loon.LObject;
import loon.LSystem;
import loon.LTexture;
import loon.LTextures;
import loon.action.map.Config;
import loon.action.sprite.ISprite;
import loon.geom.RectBox;
import loon.opengl.GLEx;

public class OutEffect extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LTexture texture;

	private boolean visible, completed;

	private int width, height;

	private int type, multiples;

	private RectBox limit;

	public OutEffect(String fileName, int code) {
		this(LTextures.loadTexture(fileName), code);
	}

	public OutEffect(LTexture t, int code) {
		this(t, LSystem.viewSize.getRect(), code);
	}

	public OutEffect(LTexture t, RectBox limit, int code) {
		this.texture = t;
		this.type = code;
		this.width = (int)t.width();
		this.height = (int)t.height();
		this.multiples = 1;
		this.limit = limit;
		this.visible = true;
	}

	public void update(long elapsedTime) {
		if (!completed) {
			switch (type) {
			case Config.DOWN:
				move_45D_down(multiples);
				break;
			case Config.UP:
				move_45D_up(multiples);
				break;
			case Config.LEFT:
				move_45D_left(multiples);
				break;
			case Config.RIGHT:
				move_45D_right(multiples);
				break;
			case Config.TDOWN:
				move_down(multiples);
				break;
			case Config.TUP:
				move_up(multiples);
				break;
			case Config.TLEFT:
				move_left(multiples);
				break;
			case Config.TRIGHT:
				move_right(multiples);
				break;
			}
			if (!limit.intersects(x(), y(), width, height)) {
				completed = true;
			}
		}
	}

	public boolean isComplete() {
		return completed;
	}

	public float getHeight() {
		return width;
	}

	public float getWidth() {
		return height;
	}

	public void createUI(GLEx g) {
		if (!visible) {
			return;
		}
		if (!completed) {
			float tmp = g.alpha();
			if (_alpha > 0 && _alpha < 1) {
				g.setAlpha(_alpha);
			}
			g.draw(texture, x(), y());
			g.setAlpha(tmp);
		}
	}

	public LTexture getBitmap() {
		return texture;
	}

	public RectBox getCollisionBox() {
		return getRect(x(), y(), width, height);
	}

	public int getMultiples() {
		return multiples;
	}

	public void setMultiples(int multiples) {
		this.multiples = multiples;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void close() {

		visible = false;
		completed = true;
	
		if (texture != null) {
			texture.close();
			texture = null;
		}
	}

}
