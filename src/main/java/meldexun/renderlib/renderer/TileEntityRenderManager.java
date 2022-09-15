package meldexun.renderlib.renderer;

import meldexun.renderlib.integration.Optifine;
import meldexun.renderlib.renderer.tileentity.TileEntityRenderer;
import meldexun.renderlib.renderer.tileentity.TileEntityRendererOptifine;
import meldexun.renderlib.util.RenderUtil;
import net.minecraft.client.renderer.culling.ICamera;

public final class TileEntityRenderManager {

	private static TileEntityRenderer tileEntityRenderer = Optifine.isOptifineDetected() ? new TileEntityRendererOptifine() : new TileEntityRenderer();

	public static void setup(ICamera frustum, float partialTicks) {
		tileEntityRenderer.setup(frustum, partialTicks, RenderUtil.getCameraEntityX(), RenderUtil.getCameraEntityY(), RenderUtil.getCameraEntityZ());
	}

	public static void reset() {
		tileEntityRenderer.reset();
	}

	public static void renderTileEntities(float partialTicks) {
		tileEntityRenderer.renderTileEntities(partialTicks);
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
