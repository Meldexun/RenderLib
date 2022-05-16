package meldexun.renderlib.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import meldexun.matrixutil.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class RenderUtil {

	private static final FloatBuffer FLOAT_BUFFER = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer();

	private static int frame;
	private static double partialTicks;
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

	public static void update() {
		Minecraft mc = Minecraft.getMinecraft();
		Entity cameraEntity = mc.getRenderViewEntity();
		Vec3d cameraOffset = ActiveRenderInfo.getCameraPosition();
		frame++;
		partialTicks = mc.isGamePaused() ? mc.renderPartialTicksPaused : mc.getRenderPartialTicks();
		projectionMatrix = getMatrix(GL11.GL_PROJECTION_MATRIX);
		modelViewMatrix = getMatrix(GL11.GL_MODELVIEW_MATRIX);
		projectionModelViewMatrix = projectionMatrix.copy();
		projectionModelViewMatrix.multiply(modelViewMatrix);
		cameraEntityX = cameraEntity.lastTickPosX + (cameraEntity.posX - cameraEntity.lastTickPosX) * partialTicks;
		cameraEntityY = cameraEntity.lastTickPosY + (cameraEntity.posY - cameraEntity.lastTickPosY) * partialTicks;
		cameraEntityZ = cameraEntity.lastTickPosZ + (cameraEntity.posZ - cameraEntity.lastTickPosZ) * partialTicks;
		cameraOffsetX = cameraOffset.x;
		cameraOffsetY = cameraOffset.y;
		cameraOffsetZ = cameraOffset.z;
		cameraX = cameraEntityX + cameraOffsetX;
		cameraY = cameraEntityY + cameraOffsetY;
		cameraZ = cameraEntityZ + cameraOffsetZ;
		frustum = new Frustum(projectionModelViewMatrix, cameraEntityX, cameraEntityY, cameraEntityZ);
	}

	/**
	 * {@link GL11#GL_PROJECTION_MATRIX},
	 * {@link GL11#GL_MODELVIEW_MATRIX},
	 * {@link GL11#GL_TEXTURE_MATRIX}
	 */
	private static Matrix4f getMatrix(int matrix) {
		GL11.glGetFloat(matrix, FLOAT_BUFFER);
		Matrix4f m = new Matrix4f();
		m.load(FLOAT_BUFFER);
		return m;
	}

	public static int getFrame() {
		return frame;
	}

	public static double getPartialTicks() {
		return partialTicks;
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
