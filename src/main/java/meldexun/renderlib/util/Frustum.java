package meldexun.renderlib.util;

import java.util.ArrayList;
import java.util.List;

import meldexun.matrixutil.Matrix4f;
import meldexun.renderlib.integration.Optifine;

public class Frustum {

	private final Vec4[] planes;
	private final double cameraX;
	private final double cameraY;
	private final double cameraZ;

	public Frustum(Matrix4f matrix, double cameraX, double cameraY, double cameraZ) {
		if (Optifine.isOptifineDetected() && Optifine.isShadowPass()) {
			matrix = Optifine.getProjectionMatrix().copy();
			matrix.multiply(Optifine.getModelViewMatrix());
			Vec3 lightPosition = Optifine.getShadowLightPositionVector();

			Vec4[] planes = new Vec4[6];
			planes[0] = new Vec4(matrix.m30 + matrix.m00, matrix.m31 + matrix.m01, matrix.m32 + matrix.m02, matrix.m33 + matrix.m03);
			planes[1] = new Vec4(matrix.m30 - matrix.m00, matrix.m31 - matrix.m01, matrix.m32 - matrix.m02, matrix.m33 - matrix.m03);
			planes[2] = new Vec4(matrix.m30 + matrix.m10, matrix.m31 + matrix.m11, matrix.m32 + matrix.m12, matrix.m33 + matrix.m13);
			planes[3] = new Vec4(matrix.m30 - matrix.m10, matrix.m31 - matrix.m11, matrix.m32 - matrix.m12, matrix.m33 - matrix.m13);
			planes[4] = new Vec4(matrix.m30 + matrix.m20, matrix.m31 + matrix.m21, matrix.m32 + matrix.m22, matrix.m33 + matrix.m23);
			planes[5] = new Vec4(matrix.m30 - matrix.m20, matrix.m31 - matrix.m21, matrix.m32 - matrix.m22, matrix.m33 - matrix.m23);
			float[] distances = new float[6];
			distances[0] = planes[0].dot(lightPosition);
			distances[1] = planes[1].dot(lightPosition);
			distances[2] = planes[2].dot(lightPosition);
			distances[3] = planes[3].dot(lightPosition);
			distances[4] = planes[4].dot(lightPosition);
			distances[5] = planes[5].dot(lightPosition);

			List<Vec4> list = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				if (distances[i] < 0.0F) {
					continue;
				}
				list.add(planes[i]);
				if (distances[i] > 0.0F) {
					for (int j = 2; j < 6; j++) {
						int k = ((i & ~1) + j) % planes.length;
						if (distances[k] >= 0.0F) {
							continue;
						}
						Vec4 plane1 = planes[i];
						Vec4 plane2 = planes[k];
						Vec3 v;
						Vec3 n = (v = plane1.cross(plane2)).cross(lightPosition).normalize();
						Vec3 p = v.cross(plane2).mult(plane1.w).add(plane1.cross(v).mult(plane2.w)).mult(1.0F / v.lengthSqr());
						list.add(new Vec4(n.x, n.y, n.z, Math.abs(p.dot(n))));
					}
				}
			}
			this.planes = list.toArray(new Vec4[list.size()]);
		} else {
			this.planes = new Vec4[6];
			this.planes[0] = new Vec4(matrix.m30 + matrix.m00, matrix.m31 + matrix.m01, matrix.m32 + matrix.m02, matrix.m33 + matrix.m03);
			this.planes[1] = new Vec4(matrix.m30 - matrix.m00, matrix.m31 - matrix.m01, matrix.m32 - matrix.m02, matrix.m33 - matrix.m03);
			this.planes[2] = new Vec4(matrix.m30 + matrix.m10, matrix.m31 + matrix.m11, matrix.m32 + matrix.m12, matrix.m33 + matrix.m13);
			this.planes[3] = new Vec4(matrix.m30 - matrix.m10, matrix.m31 - matrix.m11, matrix.m32 - matrix.m12, matrix.m33 - matrix.m13);
			this.planes[4] = new Vec4(matrix.m30 + matrix.m20, matrix.m31 + matrix.m21, matrix.m32 + matrix.m22, matrix.m33 + matrix.m23);
			this.planes[5] = new Vec4(matrix.m30 - matrix.m20, matrix.m31 - matrix.m21, matrix.m32 - matrix.m22, matrix.m33 - matrix.m23);
		}
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
