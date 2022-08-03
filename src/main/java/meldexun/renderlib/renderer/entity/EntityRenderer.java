package meldexun.renderlib.renderer.entity;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.api.IEntityRendererCache;
import meldexun.renderlib.api.ILoadable;
import meldexun.renderlib.integration.FairyLights;
import meldexun.renderlib.util.EntityUtil;
import meldexun.renderlib.util.IRenderable;
import meldexun.renderlib.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;

public class EntityRenderer {

	protected int renderedEntities;
	protected int occludedEntities;
	protected int totalEntities;
	private int camChunkX;
	private int camChunkZ;
	private int renderDist;

	public void renderEntities(ICamera frustum, float partialTicks, double camX, double camY, double camZ) {
		this.renderEntities(frustum, partialTicks, camX, camY, camZ, new ArrayList<>(), new ArrayList<>());
	}

	protected void renderEntities(ICamera frustum, float partialTicks, double camX, double camY, double camZ, List<Entity> multipassEntities, List<Entity> outlineEntities) {
		Minecraft mc = Minecraft.getMinecraft();
		if (MinecraftForgeClient.getRenderPass() == 0) {
			this.renderedEntities = 0;
			this.occludedEntities = 0;
			this.totalEntities = mc.world.loadedEntityList.size();
		}
		this.camChunkX = MathHelper.floor(RenderUtil.getCameraEntityX()) >> 4;
		this.camChunkZ = MathHelper.floor(RenderUtil.getCameraEntityZ()) >> 4;
		this.renderDist = mc.gameSettings.renderDistanceChunks;

		for (Entity entity : EntityUtil.entityIterable(mc.world.loadedEntityList)) {
			if (!shouldRender(entity, frustum, partialTicks, camX, camY, camZ)) {
				continue;
			}
			if (isOcclusionCulled(entity)) {
				if (((IRenderable) entity).setLastTimeRendered(RenderUtil.getFrame())) {
					this.occludedEntities++;
				}
			} else {
				if (((IRenderable) entity).setLastTimeRendered(RenderUtil.getFrame())) {
					this.renderedEntities++;
				}
				this.preRenderEntity(entity);
				mc.getRenderManager().renderEntityStatic(entity, partialTicks, false);
				this.postRenderEntity();
				if (((IEntityRendererCache) entity).getRenderer().isMultipass()) {
					multipassEntities.add(entity);
				}
			}
			if (shouldRenderOutlines(entity)) {
				outlineEntities.add(entity);
			}
		}
		for (Entity entity : multipassEntities) {
			this.preRenderEntity(entity);
			mc.getRenderManager().renderMultipass(entity, partialTicks);
			this.postRenderEntity();
		}
		if (MinecraftForgeClient.getRenderPass() == 0 && this.isRenderEntityOutlines() && (!outlineEntities.isEmpty() || mc.renderGlobal.entityOutlinesRendered)) {
			mc.world.profiler.endStartSection("entityOutlines");
			mc.renderGlobal.entityOutlineFramebuffer.framebufferClear();
			mc.renderGlobal.entityOutlinesRendered = !outlineEntities.isEmpty();

			if (!outlineEntities.isEmpty()) {
				GlStateManager.depthFunc(GL11.GL_ALWAYS);
				GlStateManager.disableFog();
				mc.renderGlobal.entityOutlineFramebuffer.bindFramebuffer(false);
				RenderHelper.disableStandardItemLighting();
				mc.getRenderManager().setRenderOutlines(true);

				for (Entity entity : outlineEntities) {
					this.preRenderEntity(entity);
					mc.getRenderManager().renderEntityStatic(entity, partialTicks, false);
					this.postRenderEntity();
				}

				mc.getRenderManager().setRenderOutlines(false);
				RenderHelper.enableStandardItemLighting();
				GlStateManager.depthMask(false);
				mc.renderGlobal.entityOutlineShader.render(partialTicks);
				GlStateManager.enableLighting();
				GlStateManager.depthMask(true);
				GlStateManager.enableFog();
				GlStateManager.enableBlend();
				GlStateManager.enableColorMaterial();
				GlStateManager.depthFunc(GL11.GL_LEQUAL);
				GlStateManager.enableDepth();
				GlStateManager.enableAlpha();
			}

			mc.getFramebuffer().bindFramebuffer(false);
		}
	}

	private boolean shouldRender(Entity entity, ICamera frustum, double partialTicks, double camX, double camY, double camZ) {
		if (!((IEntityRendererCache) entity).hasRenderer()) {
			return false;
		}
		if (!((ILoadable) entity).isChunkLoaded()) {
			return false;
		}
		if (!entity.shouldRenderInPass(MinecraftForgeClient.getRenderPass())) {
			return false;
		}
		if (isOutsideOfRenderDist(entity, partialTicks)) {
			return false;
		}

		if (!((IEntityRendererCache) entity).getRenderer().shouldRender(entity, frustum, camX, camY, camZ) && !entity.isRidingOrBeingRiddenBy(Minecraft.getMinecraft().player)
				&& (!RenderLib.isFairyLightsInstalled || !FairyLights.isFairyLightEntity(entity))) {
			this.setCanBeOcclusionCulled(entity, false);
			return false;
		}
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.gameSettings.thirdPersonView == 0 && entity == mc.getRenderViewEntity() && entity instanceof EntityLivingBase && !((EntityLivingBase) entity).isPlayerSleeping()) {
			this.setCanBeOcclusionCulled(entity, false);
			return false;
		}

		this.setCanBeOcclusionCulled(entity, true);
		return true;
	}

	private boolean isOutsideOfRenderDist(Entity entity, double partialTicks) {
		int entityX = MathHelper.floor(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks) >> 4;
		if (Math.abs(entityX - camChunkX) > renderDist)
			return true;
		int entityZ = MathHelper.floor(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks) >> 4;
		return Math.abs(entityZ - camChunkZ) > renderDist;
	}

	protected <T extends Entity> void setCanBeOcclusionCulled(T entity, boolean canBeOcclusionCulled) {

	}

	protected <T extends Entity> boolean isOcclusionCulled(T entity) {
		return false;
	}

	protected boolean shouldRenderOutlines(Entity entity) {
		if (entity.isGlowing()) {
			return true;
		}
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.player.isSpectator()) {
			return false;
		}
		if (!mc.gameSettings.keyBindSpectatorOutlines.isKeyDown()) {
			return false;
		}
		return entity instanceof EntityPlayer;
	}

	protected void preRenderEntity(Entity entity) {

	}

	protected void postRenderEntity() {

	}

	protected boolean isRenderEntityOutlines() {
		Minecraft mc = Minecraft.getMinecraft();
		return mc.renderGlobal.entityOutlineFramebuffer != null && mc.renderGlobal.entityOutlineShader != null;
	}

	public int getRenderedEntities() {
		return renderedEntities;
	}

	public int getOccludedEntities() {
		return occludedEntities;
	}

	public int getTotalEntities() {
		return totalEntities;
	}

}
