package meldexun.renderlib.mixin.caching.boundingbox;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import meldexun.randomutil.FastRandom;
import meldexun.renderlib.RenderLib;
import meldexun.renderlib.api.IBoundingBoxCache;
import meldexun.renderlib.config.RenderLibConfig;
import meldexun.renderlib.integration.ValkyrienSkies;
import meldexun.renderlib.util.MutableAABB;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;

@Mixin(TileEntity.class)
public class MixinTileEntity implements IBoundingBoxCache {

	@Unique
	private static final Random RAND = new FastRandom();
	@Unique
	private final MutableAABB cachedBoundingBox = new MutableAABB();
	@Unique
	private boolean initialized;

	@Unique
	@Override
	public void updateCachedBoundingBox(double partialTicks) {
		if (!initialized
				|| !RenderLibConfig.tileEntityCachedBoundingBoxEnabled
				|| RenderLibConfig.tileEntityCachedBoundingBoxBlacklistImpl.get((TileEntity) (Object) this)
				|| RenderLibConfig.tileEntityCachedBoundingBoxUpdateInterval == 1
				|| RAND.nextInt(RenderLibConfig.tileEntityCachedBoundingBoxUpdateInterval) == 0
				|| RenderLib.isValkyrienSkiesInstalled && ValkyrienSkies.isOnShip((TileEntity) (Object) this)) {
			cachedBoundingBox.set(RenderLib.isValkyrienSkiesInstalled ? ValkyrienSkies.getAABB((TileEntity) (Object) this) : ((TileEntity) (Object) this).getRenderBoundingBox());
			Vec3d v = RenderLibConfig.tileEntityBoundingBoxGrowthListImpl.get((TileEntity) (Object) this);
			if (v != null) {
				cachedBoundingBox.grow(v.x, v.y, v.x, v.x, v.z, v.x);
			}
			initialized = true;
		}
	}

	@Unique
	@Override
	public MutableAABB getCachedBoundingBox() {
		return this.cachedBoundingBox;
	}

}
