package meldexun.renderlib.renderer.entity;

import meldexun.renderlib.integration.Optifine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;

public class EntityRendererOptifine extends EntityRenderer {

	@Override
	protected void renderEntities(float partialTicks, EntityRenderList entityList) {
		super.renderEntities(partialTicks, entityList);

		Minecraft mc = Minecraft.getMinecraft();
		if (!this.isRenderEntityOutlines() && (!entityList.getOutlineEntities().isEmpty() || mc.renderGlobal.entityOutlinesRendered)) {
			mc.world.profiler.endStartSection("entityOutlines");
			mc.renderGlobal.entityOutlinesRendered = !entityList.getOutlineEntities().isEmpty();

			if (!entityList.getOutlineEntities().isEmpty()) {
				if (Optifine.isShaders()) {
					Optifine.beginEntitiesGlowing();
				}
				GlStateManager.disableFog();
				GlStateManager.disableDepth();
				mc.entityRenderer.disableLightmap();
				RenderHelper.disableStandardItemLighting();
				mc.getRenderManager().setRenderOutlines(true);

				for (Entity entity : entityList.getOutlineEntities()) {
					if (Optifine.isShaders()) {
						Optifine.nextEntity(entity);
					}
					mc.getRenderManager().renderEntityStatic(entity, mc.getRenderPartialTicks(), false);
				}

				mc.getRenderManager().setRenderOutlines(false);
				RenderHelper.enableStandardItemLighting();
				mc.entityRenderer.enableLightmap();
				GlStateManager.enableDepth();
				GlStateManager.enableFog();
				if (Optifine.isShaders()) {
					Optifine.endEntitiesGlowing();
				}
			}
		}
	}

	@Override
	protected void preRenderEntity(Entity entity) {
		if (Optifine.isShaders()) {
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
