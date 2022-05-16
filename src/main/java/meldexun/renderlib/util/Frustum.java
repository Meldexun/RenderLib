package meldexun.renderlib.util;

import meldexun.matrixutil.Matrix4f;

public class Frustum {

	private final Plane[] planes = new Plane[6];
	private final double cameraX;
	private final double cameraY;
	private final double cameraZ;

	public Frustum(Matrix4f matrix, double cameraX, double cameraY, double cameraZ) {
		this.planes[0] = new Plane(matrix.m30 + matrix.m00, matrix.m31 + matrix.m01, matrix.m32 + matrix.m02, matrix.m33 + matrix.m03);
		this.planes[1] = new Plane(matrix.m30 - matrix.m00, matrix.m31 - matrix.m01, matrix.m32 - matrix.m02, matrix.m33 - matrix.m03);
		this.planes[2] = new Plane(matrix.m30 + matrix.m10, matrix.m31 + matrix.m11, matrix.m32 + matrix.m12, matrix.m33 + matrix.m13);
		this.planes[3] = new Plane(matrix.m30 - matrix.m10, matrix.m31 - matrix.m11, matrix.m32 - matrix.m12, matrix.m33 - matrix.m13);
		this.planes[4] = new Plane(matrix.m30 + matrix.m20, matrix.m31 + matrix.m21, matrix.m32 + matrix.m22, matrix.m33 + matrix.m23);
		this.planes[5] = new Plane(matrix.m30 - matrix.m20, matrix.m31 - matrix.m21, matrix.m32 - matrix.m22, matrix.m33 - matrix.m23);
		this.cameraX = cameraX;
		this.cameraY = cameraY;
		this.cameraZ = cameraZ;
	}

	public boolean isAABBInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		return this.isAABBInFrustum((float) (minX - cameraX), (float) (minY - cameraY), (float) (minZ - cameraZ), (float) (maxX - cameraX), (float) (maxY - cameraY), (float) (maxZ - cameraZ));
	}

	private boolean isAABBInFrustum(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		for (Plane plane : this.planes) {
			if (plane.dist(plane.positiveX ? maxX : minX, plane.positiveY ? maxY : minY, plane.positiveZ ? maxZ : minZ) < 0.0F) {
				return false;
			}
		}
		return true;
	}

	public static class Plane {

		private final float x;
		private final float y;
		private final float z;
		private final float w;
		private final boolean positiveX;
		private final boolean positiveY;
		private final boolean positiveZ;

		public Plane(float x, float y, float z, float w) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
			this.positiveX = x >= 0.0F;
			this.positiveY = y >= 0.0F;
			this.positiveZ = z >= 0.0F;
		}

		public float dist(float x, float y, float z) {
			return this.x * x + this.y * y + this.z * z + this.w;
		}

	}

}
