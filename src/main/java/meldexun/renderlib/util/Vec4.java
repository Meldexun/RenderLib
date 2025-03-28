package meldexun.renderlib.util;

public class Vec4 {

	public final float x;
	public final float y;
	public final float z;
	public final float w;

	public Vec4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vec4 add(Vec4 other) {
		return new Vec4(x + other.x, y + other.y, z + other.z, w + other.w);
	}

	public Vec4 mult(float f) {
		return new Vec4(x * f, y * f, z * f, w * f);
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

	public float dot(Vec4 other) {
		return x * other.x + y * other.y + z * other.z + w * other.w;
	}

	public Vec4 normalize() {
		return mult(1.0F / length());
	}

	public float length() {
		return (float) Math.sqrt(lengthSqr());
	}

	public float lengthSqr() {
		return x * x + y * y + z * z + w * w;
	}

}
