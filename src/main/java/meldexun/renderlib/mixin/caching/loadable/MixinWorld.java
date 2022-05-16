package meldexun.renderlib.mixin.caching.loadable;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.renderlib.api.ILoadable;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@Mixin(World.class)
public class MixinWorld {

	/** {@link World#markTileEntityForRemoval(TileEntity)} */
	@Inject(method = "markTileEntityForRemoval", at = @At("HEAD"))
	public void markTileEntityForRemoval(TileEntity tileEntity, CallbackInfo info) {
		((ILoadable) tileEntity).setChunkLoaded(false);
	}

	/** {@link World#unloadEntities(Collection)} */
	@Inject(method = "unloadEntities", at = @At("HEAD"))
	public void unloadEntities(Collection<Entity> entityCollection, CallbackInfo info) {
		entityCollection.forEach(e -> ((ILoadable) e).setChunkLoaded(false));
	}

}
