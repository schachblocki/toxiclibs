/* 
 * Copyright (c) 2007 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package toxi.geom;

import toxi.math.MathUtils;

/**
 * Axis-aligned bounding box
 */
public class AABB extends Vec3D {

	public Vec3D extend;

	public AABB(Vec3D pos, Vec3D extend) {
		super(pos);
		this.extend = extend;
	}

	public AABB(AABB box) {
		this(box, box.extend);
	}

	public final float minX() {
		return x - extend.x;
	}

	public final float maxX() {
		return x + extend.x;
	}

	public final float minY() {
		return y - extend.y;
	}

	public final float maxY() {
		return y + extend.y;
	}

	public final float minZ() {
		return z - extend.z;
	}

	public final float maxZ() {
		return z + extend.z;
	}

	public final Vec3D getMin() {
		return new Vec3D(x - extend.x, y - extend.y, z - extend.z);
	}

	public final Vec3D getMax() {
		return new Vec3D(x + extend.x, y + extend.y, z + extend.z);
	}

	/**
	 * @param c
	 *            sphere centre
	 * @param r
	 *            sphere radius
	 * @return true, if AABB intersects with sphere
	 */
	public boolean intersectsSphere(Vec3D c, float r) {
		float s, d = 0;
		// find the square of the distance
		// from the sphere to the box
		if (c.x < minX()) {
			s = c.x - minX();
			d += s * s;
		} else if (c.x > maxX()) {
			s = c.x - maxX();
			d += s * s;
		}

		if (c.y < minY()) {
			s = c.y - minY();
			d += s * s;
		} else if (c.y > maxY()) {
			s = c.y - maxY();
			d += s * s;
		}

		if (c.z < minZ()) {
			s = c.z - minZ();
			d += s * s;
		} else if (c.z > maxZ()) {
			s = c.z - maxZ();
			d += s * s;
		}

		return d <= r * r;
	}

	public boolean intersectsSphere(Sphere s) {
		return intersectsSphere(s, s.radius);
	}

	/**
	 * @param b
	 * @return
	 */
	public boolean intersectsBox(AABB b) {
		Vec3D t = b.sub(this);
		return MathUtils.abs(t.x) <= (extend.x + b.extend.x)
				&& MathUtils.abs(t.y) <= (extend.y + b.extend.y)
				&& MathUtils.abs(t.z) <= (extend.z + b.extend.z);
	}

	/**
	 * Calculates intersection with the given ray between a certain distance
	 * interval.
	 * 
	 * @param ray
	 *            incident ray
	 * @param minDir
	 * @param maxDir
	 * @return intersection point on the bounding box (only the first is
	 *         returned) or null if no intersection
	 */
	public Vec3D intersectsRay(Ray3D ray, float minDir, float maxDir) {
		Vec3D invDir = new Vec3D(1 / ray.dir.x, 1 / ray.dir.y, 1 / ray.dir.z);
		boolean signDirX = invDir.x < 0;
		boolean signDirY = invDir.y < 0;
		boolean signDirZ = invDir.z < 0;
		Vec3D min = getMin();
		Vec3D max = getMax();
		Vec3D bbox = signDirX ? max : min;
		float tmin = (bbox.x - ray.x) * invDir.x;
		bbox = signDirX ? min : max;
		float tmax = (bbox.x - ray.x) * invDir.x;
		bbox = signDirY ? max : min;
		float tymin = (bbox.y - ray.y) * invDir.y;
		bbox = signDirY ? min : max;
		float tymax = (bbox.y - ray.y) * invDir.y;

		if ((tmin > tymax) || (tymin > tmax))
			return null;
		if (tymin > tmin)
			tmin = tymin;
		if (tymax < tmax)
			tmax = tymax;

		bbox = signDirZ ? max : min;
		float tzmin = (bbox.z - ray.z) * invDir.z;
		bbox = signDirZ ? min : max;
		float tzmax = (bbox.z - ray.z) * invDir.z;

		if ((tmin > tzmax) || (tzmin > tmax))
			return null;
		if (tzmin > tmin)
			tmin = tzmin;
		if (tzmax < tmax)
			tmax = tzmax;
		if ((tmin < maxDir) && (tmax > minDir)) {
			return ray.getPointAtDistance(tmin);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.Vec3D#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<aabb> pos: ").append(super.toString()).append(" ext: ")
				.append(extend);
		return sb.toString();
	}
}
