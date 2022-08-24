package meldexun.renderlib.renderer.entity;

import java.util.List;

import meldexun.renderlib.integration.Optifine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;

public class EntityRendererOptifine extends EntityRenderer {

	private boolean isShaders = false;

	@Override
	public void renderEntities(ICamera camera, float partialTicks, double camX, double camY, double camZ, List<Entity> multipassEntities, List<Entity> outlineEntities) {
		this.isShaders = Optifine.isShaders();

		int r = this.renderedEntities;
		int o = this.occludedEntities;
		int t = this.totalEntities;
		super.renderEntities(camera, partialTicks, camX, camY, camZ, multipassEntities, outlineEntities);
		if (Optifine.isShadowPass()) {
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
					Optifine.beginEntitiesGlowing();
				}
				GlStateManager.disableFog();
				GlStateManager.disableDepth();
				mc.entityRenderer.disableLightmap();
				RenderHelper.disableStandardItemLighting();
				mc.getRenderManager().setRenderOutlines(true);

				for (Entity entity : outlineEntities) {
					if (this.isShaders) {
						Optifine.nextEntity(entity);
					}
					mc.getRenderManager().renderEntityStatic(entity, mc.getRenderPartialTicks(), false);
				}

				mc.getRenderManager().setRenderOutlines(false);
				RenderHelper.enableStandardItemLighting();
				mc.entityRenderer.enableLightmap();
				GlStateManager.enableDepth();
				GlStateManager.enableFog();
				if (this.isShaders) {
					Optifine.endEntitiesGlowing();
				}
			}
		}
	}

	@Override
	protected void preRenderEntity(Entity entity) {
		if (this.isShaders) {
			Optifine.nextEntity(entity);
		}
		Optifine.setRenderedEntity(entity);
		super.preRenderEntity(entity);
	}

	@Override
	protected void postRenderEntity() {
		super.postRenderEntity();
		Optifine.setRenderedEntity(null);
	}

	@Override
	protected boolean isRenderEntityOutlines() {
		if (Optifine.isFastRender()) {
			return false;
		}
		if (Optifine.isShaders()) {
			return false;
		}
		if (Optifine.isAntialiasing()) {
			return false;
		}
		return super.isRenderEntityOutlines();
	}

}
