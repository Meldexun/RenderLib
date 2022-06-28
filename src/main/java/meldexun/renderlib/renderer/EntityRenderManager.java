package meldexun.renderlib.renderer;

import meldexun.renderlib.asm.RenderLibClassTransformer;
import meldexun.renderlib.renderer.entity.EntityRenderer;
import meldexun.renderlib.renderer.entity.EntityRendererOptifine;
import meldexun.renderlib.util.RenderUtil;
import net.minecraft.client.renderer.culling.ICamera;

public final class EntityRenderManager {

	private static EntityRenderer entityRenderer = RenderLibClassTransformer.OPTIFINE_DETECTED ? new EntityRendererOptifine() : new EntityRenderer();

	public static void renderEntities(ICamera frustum, float partialTicks) {
		entityRenderer.renderEntities(frustum, partialTicks, RenderUtil.getCameraEntityX(), RenderUtil.getCameraEntityY(), RenderUtil.getCameraEntityZ());
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
