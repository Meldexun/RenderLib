package meldexun.renderlib.mixin.caching.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import meldexun.renderlib.api.IEntityRendererCache;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager {

	/** {@link RenderManager#isRenderMultipass(Entity)} */
	@Redirect(method = "isRenderMultipass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;getEntityRenderObject(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;"))
	public Render<Entity> isRenderMultipass_getEntityRenderObject(RenderManager renderManager, Entity entityIn) {
		return ((IEntityRendererCache) entityIn).getRenderer();
	}

	/** {@link RenderManager#shouldRender(Entity, ICamera, double, double, double)} */
	@Redirect(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;getEntityRenderObject(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;"))
	public Render<Entity> shouldRender_getEntityRenderObject(RenderManager renderManager, Entity entityIn) {
		return ((IEntityRendererCache) entityIn).getRenderer();
	}

	/** {@link RenderManager#renderMultipass(Entity, float)} */
	@Redirect(method = "renderMultipass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;getEntityRenderObject(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;"))
	public Render<Entity> renderMultipass_getEntityRenderObject(RenderManager renderManager, Entity entityIn) {
		return ((IEntityRendererCache) entityIn).getRenderer();
	}

}
