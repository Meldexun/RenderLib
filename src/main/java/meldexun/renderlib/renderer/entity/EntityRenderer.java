package meldexun.renderlib.renderer.entity;

import java.util.ArrayDeque;
import java.util.Deque;

import org.lwjgl.opengl.GL11;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.api.IEntityRendererCache;
import meldexun.renderlib.api.ILoadable;
import meldexun.renderlib.integration.FairyLights;
import meldexun.renderlib.util.EntityUtil;
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
	private final Deque<EntityRenderList> entityListQueue = new ArrayDeque<>();

	public void setup(ICamera frustum, float partialTicks, double camX, double camY, double camZ) {
		Minecraft mc = Minecraft.getMinecraft();
		this.renderedEntities = 0;
		this.occludedEntities = 0;
		this.totalEntities = mc.world.loadedEntityList.size();
		this.camChunkX = MathHelper.floor(RenderUtil.getCameraEntityX()) >> 4;
		this.camChunkZ = MathHelper.floor(RenderUtil.getCameraEntityZ()) >> 4;
		this.renderDist = mc.gameSettings.renderDistanceChunks;

		EntityRenderList entityList = new EntityRenderList();
		for (Entity entity : EntityUtil.entityIterable(mc.world.loadedEntityList)) {
			if (!this.shouldRender(entity, frustum, partialTicks, camX, camY, camZ)) {
				continue;
			}

			if (this.isOcclusionCulled(entity)) {
				this.occludedEntities++;
			} else {
				this.renderedEntities++;

				entityList.addEntity(entity);

				if (((IEntityRendererCache) entity).getRenderer().isMultipass()) {
					entityList.addMultipassEntity(entity);
				}
			}

			if (this.shouldRenderOutlines(entity)) {
				entityList.addOutlineEntity(entity);
			}
		}
		this.entityListQueue.addLast(entityList);
	}

	public void reset() {
		this.entityListQueue.removeLast();
	}

	public void renderEntities(float partialTicks) {
		this.renderEntities(partialTicks, this.entityListQueue.getLast());
	}

	protected void renderEntities(float partialTicks, EntityRenderList entityList) {
		Minecraft mc = Minecraft.getMinecraft();

		for (Entity entity : entityList.getEntities()) {
			this.preRenderEntity(entity);
			mc.getRenderManager().renderEntityStatic(entity, partialTicks, false);
			this.postRenderEntity();
		}
		for (Entity entity : entityList.getMultipassEntities()) {
			this.preRenderEntity(entity);
			mc.getRenderManager().renderMultipass(entity, partialTicks);
			this.postRenderEntity();
		}
		if (MinecraftForgeClient.getRenderPass() == 0 && this.isRenderEntityOutlines() && (!entityList.getOutlineEntities().isEmpty() || mc.renderGlobal.entityOutlinesRendered)) {
			mc.world.profiler.endStartSection("entityOutlines");
			mc.renderGlobal.entityOutlineFramebuffer.framebufferClear();
			mc.renderGlobal.entityOutlinesRendered = !entityList.getOutlineEntities().isEmpty();

			if (!entityList.getOutlineEntities().isEmpty()) {
				GlStateManager.depthFunc(GL11.GL_ALWAYS);
				GlStateManager.disableFog();
				mc.renderGlobal.entityOutlineFramebuffer.bindFramebuffer(false);
				RenderHelper.disableStandardItemLighting();
				mc.getRenderManager().setRenderOutlines(true);

				for (Entity entity : entityList.getOutlineEntities()) {
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
		if (!entity.shouldRenderInPass(0) && !entity.shouldRenderInPass(1)) {
			return false;
		}
		if (this.isOutsideOfRenderDist(entity, partialTicks)) {
			return false;
		}

		if (!((IEntityRendererCache) entity).getRenderer().shouldRender(entity, frustum, camX, camY, camZ) && !entity.isRidingOrBeingRiddenBy(Minecraft.getMinecraft().player) && (!RenderLib.isFairyLightsInstalled || !FairyLights.isFairyLightEntity(entity))) {
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
		if (Math.abs(entityX - this.camChunkX) > this.renderDist) {
			return true;
		}
		int entityZ = MathHelper.floor(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks) >> 4;
		return Math.abs(entityZ - this.camChunkZ) > this.renderDist;
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
		entity.shouldRenderInPass(MinecraftForgeClient.getRenderPass());
	}

	protected void postRenderEntity() {

	}

	protected boolean isRenderEntityOutlines() {
		Minecraft mc = Minecraft.getMinecraft();
		return mc.renderGlobal.entityOutlineFramebuffer != null && mc.renderGlobal.entityOutlineShader != null;
	}

	public int getRenderedEntities() {
		return this.renderedEntities;
	}

	public int getOccludedEntities() {
		return this.occludedEntities;
	}

	public int getTotalEntities() {
		return this.totalEntities;
	}

}
