package meldexun.renderlib.renderer;

import meldexun.renderlib.integration.Optifine;
import meldexun.renderlib.renderer.entity.EntityRenderer;
import meldexun.renderlib.renderer.entity.EntityRendererOptifine;
import meldexun.renderlib.util.RenderUtil;
import net.minecraft.client.renderer.culling.ICamera;

public final class EntityRenderManager {

	private static EntityRenderer entityRenderer = Optifine.isOptifineDetected() ? new EntityRendererOptifine() : new EntityRenderer();

	public static void setup(ICamera frustum, float partialTicks) {
		entityRenderer.setup(frustum, partialTicks, RenderUtil.getCameraEntityX(), RenderUtil.getCameraEntityY(), RenderUtil.getCameraEntityZ());
	}

	public static void reset() {
		entityRenderer.reset();
	}

	public static void renderEntities(float partialTicks) {
		entityRenderer.renderEntities(partialTicks);
	}

	public static int renderedEntities() {
		return entityRenderer.getRenderedEntities();
	}

	public static int occludedEntities() {
		return entityRenderer.getOccludedEntities();
	}

	public static int totalEntities() {
		return entityRenderer.getTotalEntities();
	}

}
