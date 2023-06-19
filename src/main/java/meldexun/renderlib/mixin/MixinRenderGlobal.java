package meldexun.renderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.renderlib.config.RenderLibConfig;
import meldexun.renderlib.integration.Optifine;
import meldexun.renderlib.renderer.EntityRenderManager;
import meldexun.renderlib.renderer.TileEntityRenderManager;
import meldexun.renderlib.util.BoundingBoxHelper;
import meldexun.renderlib.util.RenderUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.MinecraftForgeClient;

@Mixin(value = RenderGlobal.class)
public class MixinRenderGlobal {

	@Inject(method = "setupTerrain", at = @At("HEAD"))
	public void setupCamera(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator, CallbackInfo info) {
		RenderUtil.updateCamera();
	}

	@Inject(method = "renderEntities", at = @At("HEAD"))
	public void preEntities(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo info) {
		if (MinecraftForgeClient.getRenderPass() == 0) {
			EntityRenderManager.setup(camera, (float) partialTicks);
			TileEntityRenderManager.setup(camera, (float) partialTicks);
		}
	}

	@Inject(method = "renderEntities", at = @At("RETURN"))
	public void postEntities(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo info) {
		if (MinecraftForgeClient.getRenderPass() == 1) {
			EntityRenderManager.reset();
			TileEntityRenderManager.reset();
		}

		if (RenderLibConfig.debugRenderBoxes
				&& MinecraftForgeClient.getRenderPass() == 1
				&& (!Optifine.isOptifineDetected() || !Optifine.isShadowPass())) {
			BoundingBoxHelper.getInstance().drawRenderBoxes(partialTicks);
		}
	}

}
