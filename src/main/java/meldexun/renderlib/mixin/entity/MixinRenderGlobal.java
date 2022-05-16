package meldexun.renderlib.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.renderlib.renderer.EntityRenderManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

	/** {@link RenderGlobal#setupTerrain(Entity, double, ICamera, int, boolean)} */
	@Inject(method = "setupTerrain", at = @At("HEAD"))
	public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator, CallbackInfo info) {
		EntityRenderManager.setup(partialTicks, camera);
	}

}
