package meldexun.renderlib.mixin.tileentity;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.renderlib.renderer.TileEntityRenderManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

	/** {@link RenderGlobal#setupTerrain(Entity, double, ICamera, int, boolean)} */
	@Inject(method = "setupTerrain", at = @At("HEAD"))
	public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator, CallbackInfo info) {
		TileEntityRenderManager.setup(partialTicks, camera);
	}

	/** {@link RenderGlobal#updateTileEntities(Collection, Collection)} */
	@Inject(method = "updateTileEntities", cancellable = true, at = @At("HEAD"))
	public void updateTileEntities(Collection<TileEntity> tileEntitiesToRemove, Collection<TileEntity> tileEntitiesToAdd, CallbackInfo info) {
		info.cancel();
	}

}
