package meldexun.renderlib.renderer.entity;

import java.util.List;

import meldexun.reflectionutil.ReflectionField;
import meldexun.reflectionutil.ReflectionMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;

public class EntityRendererOptifine extends EntityRenderer {

	private static final ReflectionMethod<Boolean> IS_SHADERS = new ReflectionMethod<>("Config", "isShaders", "isShaders");
	private static final ReflectionField<Boolean> IS_SHADOW_PASS = new ReflectionField<>("net.optifine.shaders.Shaders", "isShadowPass", "isShadowPass");
	private static final ReflectionMethod<Boolean> NEXT_ENTITY = new ReflectionMethod<>("net.optifine.shaders.Shaders", "nextEntity", "nextEntity", Entity.class);
	private static final ReflectionField<Entity> RENDERED_ENTITY = new ReflectionField<>(RenderGlobal.class, "renderedEntity", "renderedEntity");
	private static final ReflectionMethod<Boolean> IS_FAST_RENDER = new ReflectionMethod<>("Config", "isFastRender", "isFastRender");
	private static final ReflectionMethod<Boolean> IS_ANTIALIASING = new ReflectionMethod<>("Config", "isAntialiasing", "isAntialiasing");
	private static final ReflectionMethod<Void> BEGIN_ENTITIES_GLOWING = new ReflectionMethod<>("net.optifine.shaders.Shaders", "beginEntitiesGlowing", "beginEntitiesGlowing");
	private static final ReflectionMethod<Void> END_ENTITIES_GLOWING = new ReflectionMethod<>("net.optifine.shaders.Shaders", "endEntitiesGlowing", "endEntitiesGlowing");
	private boolean isShaders = false;

	@Override
	public void renderEntities(ICamera camera, float partialTicks, double camX, double camY, double camZ, List<Entity> multipassEntities, List<Entity> outlineEntities) {
		this.isShaders = IS_SHADERS.invoke(null);

		int r = this.renderedEntities;
		int o = this.occludedEntities;
		int t = this.totalEntities;
		super.renderEntities(camera, partialTicks, camX, camY, camZ, multipassEntities, outlineEntities);
		if (IS_SHADOW_PASS.getBoolean(null)) {
			this.renderedEntities = r;
			this.occludedEntities = o;
			this.totalEntities = t;
		}

		Minecraft mc = Minecraft.getMinecraft();
		if (!this.isRenderEntityOutlines() && (!outlineEntities.isEmpty() || mc.renderGlobal.entityOutlinesRendered)) {
			mc.world.profiler.endStartSection("entityOutlines");
			mc.renderGlobal.entityOutlinesRendered = !outlineEntities.isEmpty();

			if (!outlineEntities.isEmpty()) {
				if (this.isShaders) {
					BEGIN_ENTITIES_GLOWING.invoke(null);
				}
				GlStateManager.disableFog();
				GlStateManager.disableDepth();
				mc.entityRenderer.disableLightmap();
				RenderHelper.disableStandardItemLighting();
				mc.getRenderManager().setRenderOutlines(true);

				for (Entity entity : outlineEntities) {
					if (this.isShaders) {
						NEXT_ENTITY.invoke(null, entity);
					}
					mc.getRenderManager().renderEntityStatic(entity, mc.getRenderPartialTicks(), false);
				}

				mc.getRenderManager().setRenderOutlines(false);
				RenderHelper.enableStandardItemLighting();
				mc.entityRenderer.enableLightmap();
				GlStateManager.enableDepth();
				GlStateManager.enableFog();
				if (this.isShaders) {
					END_ENTITIES_GLOWING.invoke(null);
				}
			}
		}
	}

	@Override
	protected void preRenderEntity(Entity entity) {
		if (this.isShaders) {
			NEXT_ENTITY.invoke(null, entity);
		}
		RENDERED_ENTITY.set(Minecraft.getMinecraft().renderGlobal, entity);
		super.preRenderEntity(entity);
	}

	@Override
	protected void postRenderEntity() {
		super.postRenderEntity();
		RENDERED_ENTITY.set(Minecraft.getMinecraft().renderGlobal, null);
	}

	@Override
	protected boolean isRenderEntityOutlines() {
		if (IS_FAST_RENDER.invoke(null)) {
			return false;
		}
		if (IS_SHADERS.invoke(null)) {
			return false;
		}
		if (IS_ANTIALIASING.invoke(null)) {
			return false;
		}
		return super.isRenderEntityOutlines();
	}

}
