package meldexun.renderlib.renderer.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;

public class TileEntityRenderList {

	private final List<TileEntity> tileEntitiesPass0 = new ArrayList<>();
	private final List<TileEntity> tileEntitiesPass1 = new ArrayList<>();

	public void addTileEntity(TileEntity tileEntity) {
		if (tileEntity.shouldRenderInPass(0))
			this.tileEntitiesPass0.add(tileEntity);
		if (tileEntity.shouldRenderInPass(1))
			this.tileEntitiesPass1.add(tileEntity);
	}

	public List<TileEntity> getTileEntities() {
		return MinecraftForgeClient.getRenderPass() == 0 ? this.tileEntitiesPass0 : this.tileEntitiesPass1;
	}

}
