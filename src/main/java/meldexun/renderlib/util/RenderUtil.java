package meldexun.renderlib.util;

import org.lwjgl.opengl.GL11;

import meldexun.matrixutil.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class RenderUtil {

	private static int frame;
	private static double partialTick;
	private static double partialTickDelta;

	private static Matrix4f projectionMatrix;
	private static Matrix4f modelViewMatrix;
	private static Matrix4f projectionModelViewMatrix;
	private static double cameraEntityX;
	private static double cameraEntityY;
	private static double cameraEntityZ;
	private static double cameraOffsetX;
	private static double cameraOffsetY;
	private static double cameraOffsetZ;
	private static double cameraX;
	private static double cameraY;
	private static double cameraZ;
	private static Frustum frustum;

	public static void update(double partialTickIn) {
		frame++;
		partialTickDelta = partialTickIn - partialTick;
		if (partialTickDelta < 0.0D)
			partialTickDelta += 1.0D;
		partialTick = partialTickIn;
	}

	public static void updateCamera() {
		Minecraft mc = Minecraft.getMinecraft();
		Entity cameraEntity = mc.getRenderViewEntity();
		Vec3d cameraOffset = ActiveRenderInfo.getCameraPosition();
		projectionMatrix = GLUtil.getMatrix(GL11.GL_PROJECTION_MATRIX);
		modelViewMatrix = GLUtil.getMatrix(GL11.GL_MODELVIEW_MATRIX);
		projectionModelViewMatrix = projectionMatrix.copy();
		projectionModelViewMatrix.multiply(modelViewMatrix);
		cameraEntityX = cameraEntity.lastTickPosX + (cameraEntity.posX - cameraEntity.lastTickPosX) * partialTick;
		cameraEntityY = cameraEntity.lastTickPosY + (cameraEntity.posY - cameraEntity.lastTickPosY) * partialTick;
		cameraEntityZ = cameraEntity.lastTickPosZ + (cameraEntity.posZ - cameraEntity.lastTickPosZ) * partialTick;
		cameraOffsetX = cameraOffset.x;
		cameraOffsetY = cameraOffset.y;
		cameraOffsetZ = cameraOffset.z;
		cameraX = cameraEntityX + cameraOffsetX;
		cameraY = cameraEntityY + cameraOffsetY;
		cameraZ = cameraEntityZ + cameraOffsetZ;
		frustum = new Frustum(projectionModelViewMatrix, cameraEntityX, cameraEntityY, cameraEntityZ);
	}

	public static int getFrame() {
		return frame;
	}

	public static double getPartialTick() {
		return partialTick;
	}

	public static double getPartialTickDelta() {
		return partialTickDelta;
	}

	public static Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public static Matrix4f getModelViewMatrix() {
		return modelViewMatrix;
	}

	public static Matrix4f getProjectionModelViewMatrix() {
		return projectionModelViewMatrix;
	}

	public static double getCameraEntityX() {
		return cameraEntityX;
	}

	public static double getCameraEntityY() {
		return cameraEntityY;
	}

	public static double getCameraEntityZ() {
		return cameraEntityZ;
	}

	public static double getCameraOffsetX() {
		return cameraOffsetX;
	}

	public static double getCameraOffsetY() {
		return cameraOffsetY;
	}

	public static double getCameraOffsetZ() {
		return cameraOffsetZ;
	}

	public static double getCameraX() {
		return cameraX;
	}

	public static double getCameraY() {
		return cameraY;
	}

	public static double getCameraZ() {
		return cameraZ;
	}

	public static Frustum getFrustum() {
		return frustum;
	}

}
