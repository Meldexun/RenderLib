package meldexun.renderlib.renderer.tileentity;

import meldexun.renderlib.integration.Optifine;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRendererOptifine extends TileEntityRenderer {

	private boolean isShaders = false;

	@Override
	public void renderTileEntities(ICamera camera, float partialTicks, double camX, double camY, double camZ) {
		this.isShaders = Optifine.isShaders();

		int r = this.renderedTileEntities;
		int o = this.occludedTileEntities;
		int t = this.totalTileEntities;
		super.renderTileEntities(camera, partialTicks, camX, camY, camZ);
		if (Optifine.isShadowPass()) {
			this.renderedTileEntities = r;
			this.occludedTileEntities = o;
			this.totalTileEntities = t;
		}
	}

	@Override
	protected void preRenderTileEntity(TileEntity tileEntity) {
		if (this.isShaders) {
			Optifine.nextBlockEntity(tileEntity);
		}
		super.preRenderTileEntity(tileEntity);
	}

}
