package meldexun.renderlib.renderer;

import meldexun.renderlib.asm.RenderLibClassTransformer;
import meldexun.renderlib.renderer.entity.EntityRenderer;
import meldexun.renderlib.renderer.entity.EntityRendererOptifine;
import meldexun.renderlib.util.RenderUtil;
import net.minecraft.client.renderer.culling.ICamera;

public final class EntityRenderManager {

	private static EntityRenderer entityRenderer = RenderLibClassTransformer.OPTIFINE_DETECTED ? new EntityRendererOptifine() : new EntityRenderer();

	public static void setup(double partialTicks, ICamera frustum) {
		entityRenderer.setup(frustum, RenderUtil.getCameraEntityX(), RenderUtil.getCameraEntityY(), RenderUtil.getCameraEntityZ(), partialTicks);
	}

	public static void renderEntities(float partialTicks) {
		entityRenderer.renderEntities(partialTicks);
	}

}
