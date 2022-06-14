package meldexun.renderlib.mixin.caching.boundingbox;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import meldexun.renderlib.api.IBoundingBoxCache;
import meldexun.renderlib.config.RenderLibConfig;
import meldexun.renderlib.util.MutableAABB;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

@Mixin(Entity.class)
public class MixinEntity implements IBoundingBoxCache {

	@Unique
	private final MutableAABB cachedBoundingBox = new MutableAABB();

	@Unique
	@Override
	public void updateCachedBoundingBox(double partialTicks) {
		cachedBoundingBox.set(((Entity) (Object) this).getRenderBoundingBox());
		cachedBoundingBox.grow(0.5D);
		cachedBoundingBox.offset(
				-(((Entity) (Object) this).posX - ((Entity) (Object) this).lastTickPosX) * (1.0D - partialTicks),
				-(((Entity) (Object) this).posY - ((Entity) (Object) this).lastTickPosY) * (1.0D - partialTicks),
				-(((Entity) (Object) this).posZ - ((Entity) (Object) this).lastTickPosZ) * (1.0D - partialTicks));
		Vec3d v = RenderLibConfig.entityBoundingBoxGrowthListImpl.get((Entity) (Object) this);
		if (v != null) {
			cachedBoundingBox.grow(v);
		}
	}

	@Unique
	@Override
	public MutableAABB getCachedBoundingBox() {
		return this.cachedBoundingBox;
	}

}
