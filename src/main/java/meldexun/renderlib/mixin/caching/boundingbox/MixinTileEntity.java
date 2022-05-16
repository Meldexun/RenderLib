package meldexun.renderlib.mixin.caching.boundingbox;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.api.IBoundingBoxCache;
import meldexun.renderlib.integration.ValkyrienSkies;
import meldexun.renderlib.util.MutableAABB;
import net.minecraft.tileentity.TileEntity;

@Mixin(TileEntity.class)
public class MixinTileEntity implements IBoundingBoxCache {

	@Unique
	private final MutableAABB cachedBoundingBox = new MutableAABB();

	@Unique
	@Override
	public void updateCachedBoundingBox(double partialTicks) {
		cachedBoundingBox.set(RenderLib.isValkyrienSkiesInstalled ? ValkyrienSkies.getAABB((TileEntity) (Object) this) : ((TileEntity) (Object) this).getRenderBoundingBox());
	}

	@Unique
	@Override
	public MutableAABB getCachedBoundingBox() {
		return this.cachedBoundingBox;
	}

}
