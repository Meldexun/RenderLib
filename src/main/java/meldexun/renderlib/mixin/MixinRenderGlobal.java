package meldexun.renderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.renderlib.util.RenderUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;

@Mixin(value = RenderGlobal.class, priority = 900)
public class MixinRenderGlobal {

	/** {@link RenderGlobal#setupTerrain(Entity, double, ICamera, int, boolean)} */
	@Inject(method = "setupTerrain", at = @At("HEAD"))
	public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator, CallbackInfo info) {
		RenderUtil.updateCamera();
	}

}
