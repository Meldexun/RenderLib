package meldexun.renderlib.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import meldexun.renderlib.api.IBoundingBoxCache;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

@Mixin(Render.class)
public class MixinRender {

	/** {@link Render#shouldRender(Entity, ICamera, double, double, double)} */
	@Inject(method = "shouldRender", cancellable = true, at = @At("HEAD"))
	public void shouldRender(Entity entity, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> info) {
		if (!entity.isInRangeToRender3d(camX, camY, camZ)) {
			info.setReturnValue(false);
			return;
		}

		if (entity.ignoreFrustumCheck) {
			info.setReturnValue(true);
			return;
		}

		info.setReturnValue(((IBoundingBoxCache) entity).getCachedBoundingBox().isVisible(camera));
	}

}
