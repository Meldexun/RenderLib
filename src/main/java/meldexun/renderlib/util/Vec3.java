package meldexun.renderlib.util;

public class Vec3 {

	public final float x;
	public final float y;
	public final float z;

	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3(float[] data) {
		this(data[0], data[1], data[2]);
	}

	public Vec3 add(Vec3 other) {
		return new Vec3(x + other.x, y + other.y, z + other.z);
	}

	public Vec3 mult(float f) {
		return new Vec3(x * f, y * f, z * f);
	}

	public Vec3 cross(Vec3 other) {
		return new Vec3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
	}

	public Vec3 cross(Vec4 other) {
		return new Vec3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
	}

	public float dot(Vec3 other) {
		return x * other.x + y * other.y + z * other.z;
	}

	public Vec3 normalize() {
		return mult(1.0F / length());
	}

	public float length() {
		return (float) Math.sqrt(lengthSqr());
	}

	public float lengthSqr() {
		return x * x + y * y + z * z;
	}

}
