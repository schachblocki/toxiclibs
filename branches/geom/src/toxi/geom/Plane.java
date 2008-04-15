package toxi.geom;

public class Plane extends Vec3D {

	public static final Plane XY = new Plane(new Vec3D(), Vec3D.Z_AXIS);
	public static final Plane XZ = new Plane(new Vec3D(), Vec3D.Y_AXIS);
	public static final Plane YZ = new Plane(new Vec3D(), Vec3D.X_AXIS);

	Vec3D normal;

	Plane(Vec3D origin, Vec3D norm) {
		super(origin);
		normal = new Vec3D(norm);
	}

	float getDistanceToPoint(Vec3D p) {
		float sn = -normal.dot(p.sub(this));
		float sd = normal.magSquared();
		Vec3D isec = p.add(normal.scale(sn / sd));
		return isec.distanceTo(p);
	}
}
