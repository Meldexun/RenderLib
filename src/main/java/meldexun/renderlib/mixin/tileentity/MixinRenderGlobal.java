package meldexun.renderlib.mixin.tileentity;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.tileentity.TileEntity;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

	/** {@link RenderGlobal#updateTileEntities(Collection, Collection)} */
	@Inject(method = "updateTileEntities", cancellable = true, at = @At("HEAD"))
	public void updateTileEntities(Collection<TileEntity> tileEntitiesToRemove, Collection<TileEntity> tileEntitiesToAdd, CallbackInfo info) {
		info.cancel();
	}

}
