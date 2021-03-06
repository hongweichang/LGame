/**
 * Copyright 2008 - 2015 The Loon Game Engine Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loon
 * @author cping
 * @email：javachenpeng@yahoo.com
 * @version 0.5
 */
package loon.geom;

import loon.utils.MathUtils;

/*最简化的浮点坐标处理类,以减少对象大小*/
public class PointF implements XY {

	public float x = 0;
	public float y = 0;

	public PointF() {

	}

	public PointF(float x1, float y1) {
		set(x1, y1);
	}

	public PointF(PointF p) {
		this.x = p.x;
		this.y = p.y;
	}

	public void set(float x1, float y1) {
		this.x = x1;
		this.y = y1;
	}

	public final boolean equals(float x, float y) {
		return MathUtils.equal(x, this.x) && MathUtils.equal(y, this.y);
	}

	public final float length() {
		return MathUtils.sqrt(MathUtils.mul(x, x) + MathUtils.mul(y, y));
	}

	public final void negate() {
		x = -x;
		y = -y;
	}

	public final void offset(float x, float y) {
		this.x += x;
		this.y += y;
	}

	public final void set(PointF p) {
		this.x = p.x;
		this.y = p.y;
	}

	public final float distanceTo(PointF p) {
		final float tx = this.x - p.x;
		final float ty = this.y - p.y;
		return MathUtils.sqrt(MathUtils.mul(tx, tx) + MathUtils.mul(ty, ty));
	}

	public final float distanceTo(float x, float y) {
		final float tx = this.x - x;
		final float ty = this.y - y;
		return MathUtils.sqrt(MathUtils.mul(tx, tx) + MathUtils.mul(ty, ty));
	}

	public final float distanceTo(PointF p1, PointF p2) {
		final float tx = p2.x - p1.x;
		final float ty = p2.y - p1.y;
		final float u = MathUtils.div(
				MathUtils.mul(x - p1.x, tx) + MathUtils.mul(y - p1.y, ty),
				MathUtils.mul(tx, tx) + MathUtils.mul(ty, ty));
		final float ix = p1.x + MathUtils.mul(u, tx);
		final float iy = p1.y + MathUtils.mul(u, ty);
		final float dx = ix - x;
		final float dy = iy - y;
		return MathUtils.sqrt(MathUtils.mul(dx, dx) + MathUtils.mul(dy, dy));
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}
}
