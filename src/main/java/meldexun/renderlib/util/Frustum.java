package meldexun.renderlib.util;

import meldexun.matrixutil.Matrix4f;

public class Frustum {

	private final Vec4[] planes = new Vec4[6];
	private final double cameraX;
	private final double cameraY;
	private final double cameraZ;

	public Frustum(Matrix4f matrix, double cameraX, double cameraY, double cameraZ) {
		this.planes[0] = new Vec4(matrix.m30 + matrix.m00, matrix.m31 + matrix.m01, matrix.m32 + matrix.m02, matrix.m33 + matrix.m03);
		this.planes[1] = new Vec4(matrix.m30 - matrix.m00, matrix.m31 - matrix.m01, matrix.m32 - matrix.m02, matrix.m33 - matrix.m03);
		this.planes[2] = new Vec4(matrix.m30 + matrix.m10, matrix.m31 + matrix.m11, matrix.m32 + matrix.m12, matrix.m33 + matrix.m13);
		this.planes[3] = new Vec4(matrix.m30 - matrix.m10, matrix.m31 - matrix.m11, matrix.m32 - matrix.m12, matrix.m33 - matrix.m13);
		this.planes[4] = new Vec4(matrix.m30 + matrix.m20, matrix.m31 + matrix.m21, matrix.m32 + matrix.m22, matrix.m33 + matrix.m23);
		this.planes[5] = new Vec4(matrix.m30 - matrix.m20, matrix.m31 - matrix.m21, matrix.m32 - matrix.m22, matrix.m33 - matrix.m23);
		this.cameraX = cameraX;
		this.cameraY = cameraY;
		this.cameraZ = cameraZ;
	}

	public boolean isAABBInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		return this.isAABBInFrustum((float) (minX - cameraX), (float) (minY - cameraY), (float) (minZ - cameraZ), (float) (maxX - cameraX), (float) (maxY - cameraY), (float) (maxZ - cameraZ));
	}

	private boolean isAABBInFrustum(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		for (Vec4 plane : this.planes) {
			if (plane.x * (plane.x >= 0.0F ? maxX : minX) + plane.y * (plane.y >= 0.0F ? maxY : minY) + plane.z * (plane.z >= 0.0F ? maxZ : minZ) + plane.w < 0.0F) {
				return false;
			}
		}
		return true;
	}

}
