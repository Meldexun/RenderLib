package meldexun.renderlib.mixin.caching.loadable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import meldexun.renderlib.api.ILoadable;
import net.minecraft.tileentity.TileEntity;

@Mixin(TileEntity.class)
public class MixinTileEntity implements ILoadable {

	@Unique
	private boolean isChunkLoaded = true;

	@Override
	public boolean isChunkLoaded() {
		return isChunkLoaded;
	}

	@Override
	public void setChunkLoaded(boolean isChunkLoaded) {
		this.isChunkLoaded = isChunkLoaded;
	}

}
