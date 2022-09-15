package meldexun.renderlib.renderer.tileentity;

import meldexun.renderlib.integration.Optifine;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRendererOptifine extends TileEntityRenderer {

	@Override
	protected void preRenderTileEntity(TileEntity tileEntity) {
		if (Optifine.isShaders()) {
			Optifine.nextBlockEntity(tileEntity);
		}
		super.preRenderTileEntity(tileEntity);
	}

}
