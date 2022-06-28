package meldexun.renderlib.renderer;

import meldexun.renderlib.asm.RenderLibClassTransformer;
import meldexun.renderlib.renderer.tileentity.TileEntityRenderer;
import meldexun.renderlib.renderer.tileentity.TileEntityRendererOptifine;
import meldexun.renderlib.util.RenderUtil;
import net.minecraft.client.renderer.culling.ICamera;

public final class TileEntityRenderManager {

	private static TileEntityRenderer tileEntityRenderer = RenderLibClassTransformer.OPTIFINE_DETECTED ? new TileEntityRendererOptifine() : new TileEntityRenderer();

	public static void renderTileEntities(ICamera frustum, float partialTicks) {
		tileEntityRenderer.renderTileEntities(frustum, partialTicks, RenderUtil.getCameraEntityX(), RenderUtil.getCameraEntityY(), RenderUtil.getCameraEntityZ());
	}

	public static int renderedTileEntities() {
		return tileEntityRenderer.getRenderedTileEntities();
	}

	public static int occludedTileEntities() {
		return tileEntityRenderer.getOccludedTileEntities();
	}

	public static int totalTileEntities() {
		return tileEntityRenderer.getTotalTileEntities();
	}

}
