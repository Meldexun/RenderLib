package meldexun.renderlib.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import meldexun.matrixutil.Matrix4f;
import meldexun.renderlib.RenderLib;
import meldexun.renderlib.api.IBoundingBoxCache;
import meldexun.renderlib.util.memory.NIOBufferUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class BoundingBoxHelper {

	private static final byte[] VERTEX_DATA = {
			0, 0, 0,
			0, 0, 1,
			0, 1, 0,
			0, 1, 1,
			1, 0, 0,
			1, 0, 1,
			1, 1, 0,
			1, 1, 1
	};
	private static final byte[] INDICES = {
			0, 4, 5, 1,
			3, 7, 6, 2,
			4, 0, 2, 6,
			1, 5, 7, 3,
			0, 1, 3, 2,
			5, 4, 6, 7
	};
	private static final String A_POS = "a_Pos";
	private static final String U_MATRIX = "u_ModelViewProjectionMatrix";
	private static BoundingBoxHelper instance;
	private final GLShader shader = GLShader.builder()
			.addShader(GL20.GL_VERTEX_SHADER, new ResourceSupplier(new ResourceLocation(RenderLib.MODID, "shaders/debug.vsh")))
			.addShader(GL20.GL_FRAGMENT_SHADER, new ResourceSupplier(new ResourceLocation(RenderLib.MODID, "shaders/debug.fsh")))
			.bindAttribute(A_POS, 0)
			.build();
	private final int cubeVertexBuffer;
	private final int quadsCubeIndexBuffer;

	public BoundingBoxHelper() {
		cubeVertexBuffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cubeVertexBuffer);
		NIOBufferUtil.tempByteBuffer(VERTEX_DATA, buffer -> GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW));
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		quadsCubeIndexBuffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, quadsCubeIndexBuffer);
		NIOBufferUtil.tempByteBuffer(INDICES, buffer -> GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW));
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public static BoundingBoxHelper getInstance() {
		if (instance == null) {
			instance = new BoundingBoxHelper();
		}
		return instance;
	}

	public void drawRenderBoxes(double partialTicks) {
		this.setupRenderState();

		GLShader.push();
		shader.use();

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, quadsCubeIndexBuffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cubeVertexBuffer);
		GL20.glVertexAttribPointer(shader.getAttribute(A_POS), 3, GL11.GL_BYTE, false, 0, 0);
		GL20.glEnableVertexAttribArray(shader.getAttribute(A_POS));

		Minecraft mc = Minecraft.getMinecraft();
		EntityUtil.entityIterable(mc.world.loadedEntityList).forEach(entity -> {
			if (entity == mc.getRenderViewEntity()) {
				return;
			}

			MutableAABB aabb = ((IBoundingBoxCache) entity).getCachedBoundingBox();
			if (!aabb.isVisible(RenderUtil.getFrustum())) {
				return;
			}

			Matrix4f matrix = RenderUtil.getProjectionModelViewMatrix().copy();
			matrix.translate(
					(float) (aabb.minX() - RenderUtil.getCameraEntityX()),
					(float) (aabb.minY() - RenderUtil.getCameraEntityY()),
					(float) (aabb.minZ() - RenderUtil.getCameraEntityZ()));
			matrix.scale((float) aabb.sizeX(), (float) aabb.sizeY(), (float) aabb.sizeZ());
			GLUtil.setMatrix(shader.getUniform(U_MATRIX), matrix);

			GL11.glDrawElements(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_BYTE, 0);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glDrawElements(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_BYTE, 0);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		});

		TileEntityUtil.processTileEntities(mc.world, tileEntity -> {
			MutableAABB aabb = ((IBoundingBoxCache) tileEntity).getCachedBoundingBox();
			if (!aabb.isVisible(RenderUtil.getFrustum())) {
				return;
			}

			Matrix4f matrix = RenderUtil.getProjectionModelViewMatrix().copy();
			matrix.translate(
					(float) (aabb.minX() - RenderUtil.getCameraEntityX()),
					(float) (aabb.minY() - RenderUtil.getCameraEntityY()),
					(float) (aabb.minZ() - RenderUtil.getCameraEntityZ()));
			matrix.scale((float) aabb.sizeX(), (float) aabb.sizeY(), (float) aabb.sizeZ());
			GLUtil.setMatrix(shader.getUniform(U_MATRIX), matrix);

			GL11.glDrawElements(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_BYTE, 0);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glDrawElements(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_BYTE, 0);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		});

		GL20.glDisableVertexAttribArray(shader.getAttribute(A_POS));
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		GLShader.pop();

		this.clearRenderState();
	}

	private void setupRenderState() {
		GLUtil.saveShaderGLState();

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

		GlStateManager.enableDepth();
		GlStateManager.depthFunc(GL11.GL_LEQUAL);
		GlStateManager.depthMask(false);

		GlStateManager.disableCull();

		GlStateManager.colorMask(true, true, true, true);
	}

	private void clearRenderState() {
		GLUtil.restoreShaderGLState();
	}

}
