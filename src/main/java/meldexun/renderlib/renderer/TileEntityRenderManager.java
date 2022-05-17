package meldexun.renderlib.renderer;

import meldexun.renderlib.asm.RenderLibClassTransformer;
import meldexun.renderlib.renderer.tileentity.TileEntityRenderer;
import meldexun.renderlib.renderer.tileentity.TileEntityRendererOptifine;
import meldexun.renderlib.util.RenderUtil;
import net.minecraft.client.renderer.culling.ICamera;

public final class TileEntityRenderManager {

	private static TileEntityRenderer tileEntityRenderer = RenderLibClassTransformer.OPTIFINE_DETECTED ? new TileEntityRendererOptifine() : new TileEntityRenderer();

	public static void setup(double partialTicks, ICamera frustum) {
		tileEntityRenderer.setup(frustum, RenderUtil.getCameraEntityX(), RenderUtil.getCameraEntityY(), RenderUtil.getCameraEntityZ(), partialTicks);
	}

	public static void renderTileEntities(float partialTicks) {
		tileEntityRenderer.renderTileEntities(partialTicks);
	}

	public static int totalTileEntities() {
		return tileEntityRenderer.totalTileEntities;
	}

	public static int renderedTileEntities() {
		return tileEntityRenderer.renderedTileEntities;
	}

	public static int occludedTileEntities() {
		return tileEntityRenderer.occludedTileEntities;
	}

}
