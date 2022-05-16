package meldexun.renderlib.mixin.caching.renderer;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import meldexun.renderlib.api.ITileEntityRendererCache;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

@Mixin(TileEntity.class)
public class MixinTileEntity implements ITileEntityRendererCache {

	@Unique
	private TileEntitySpecialRenderer<TileEntity> renderer;
	@Unique
	private boolean rendererInitialized;

	@SuppressWarnings("unchecked")
	@Unique
	@Override
	@Nullable
	public <T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer() {
		if (!rendererInitialized) {
			renderer = loadRenderer((TileEntity) (Object) this);
			rendererInitialized = true;
		}
		return (TileEntitySpecialRenderer<T>) renderer;
	}

}
