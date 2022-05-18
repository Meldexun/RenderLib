package meldexun.renderlib.renderer.entity;

import java.util.ArrayDeque;
import java.util.Queue;

import org.lwjgl.opengl.GL11;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.api.IEntityRendererCache;
import meldexun.renderlib.api.ILoadable;
import meldexun.renderlib.integration.FairyLights;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.MinecraftForgeClient;

public class EntityRenderer {

	protected final Queue<Entity> entityListStaticPass0 = new ArrayDeque<>();
	protected final Queue<Entity> entityListMultipassPass0 = new ArrayDeque<>();
	protected final Queue<Entity> entityListOutlinePass0 = new ArrayDeque<>();
	protected final Queue<Entity> entityListStaticPass1 = new ArrayDeque<>();
	protected final Queue<Entity> entityListMultipassPass1 = new ArrayDeque<>();
	public int renderedEntities;
	public int occludedEntities;
	public int totalEntities;

	public void setup(ICamera camera, double camX, double camY, double camZ, double partialTicks) {
		this.renderedEntities = 0;
		this.occludedEntities = 0;
		this.totalEntities = 0;
		this.clearEntityLists();
		this.fillEntityLists(camera, camX, camY, camZ, partialTicks);
	}

	protected void clearEntityLists() {
		this.entityListStaticPass0.clear();
		this.entityListMultipassPass0.clear();
		this.entityListOutlinePass0.clear();
		this.entityListStaticPass1.clear();
		this.entityListMultipassPass1.clear();
	}

	protected void fillEntityLists(ICamera camera, double camX, double camY, double camZ, double partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.world.loadedEntityList.forEach(entity -> this.addToRenderLists(entity, camera, camX, camY, camZ, partialTicks));
	}

	protected <T extends Entity> void addToRenderLists(T entity, ICamera camera, double camX, double camY, double camZ, double partialTicks) {
		if (!((IEntityRendererCache) entity).hasRenderer()) {
			return;
		}
		if (!((ILoadable) entity).isChunkLoaded()) {
			return;
		}

		this.totalEntities++;

		if (!((IEntityRendererCache) entity).getRenderer().shouldRender(entity, camera, camX, camY, camZ)
				&& !entity.isRidingOrBeingRiddenBy(Minecraft.getMinecraft().player)
				&& (!RenderLib.isFairyLightsInstalled || !FairyLights.isFairyLightEntity(entity))) {
			this.setCanBeOcclusionCulled(entity, false);
			return;
		}
		if (!this.shouldRenderEntity(entity, camX, camY, camZ)) {
			this.setCanBeOcclusionCulled(entity, false);
			return;
		}

		this.setCanBeOcclusionCulled(entity, true);

		if (this.isOcclusionCulled(entity)) {
			this.occludedEntities++;
		} else {
			this.renderedEntities++;
			this.render(entity);
		}

		if (this.shouldRenderOutlines(entity)) {
			this.renderOutline(entity);
		}

		Entity[] parts = entity.getParts();
		if (parts != null) {
			for (Entity part : parts) {
				this.addToRenderLists(part, camera, camX, camY, camZ, partialTicks);
			}
		}
	}

	protected <T extends Entity> void render(T entity) {
		Render<T> renderer = ((IEntityRendererCache) entity).getRenderer();

		if (entity.shouldRenderInPass(0)) {
			this.entityListStaticPass0.add(entity);
			if (renderer.isMultipass()) {
				this.entityListMultipassPass0.add(entity);
			}
		}

		if (entity.shouldRenderInPass(1)) {
			this.entityListStaticPass1.add(entity);
			if (renderer.isMultipass()) {
				this.entityListMultipassPass1.add(entity);
			}
		}
	}

	protected <T extends Entity> void renderOutline(T entity) {
		if (entity.shouldRenderInPass(0)) {
			this.entityListOutlinePass0.add(entity);
		}
	}

	protected <T extends Entity> void setCanBeOcclusionCulled(T entity, boolean canBeOcclusionCulled) {

	}

	protected <T extends Entity> boolean isOcclusionCulled(T entity) {
		return false;
	}

	protected boolean shouldRenderEntity(Entity entity, double camX, double camY, double camZ) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.gameSettings.thirdPersonView != 0) {
			return true;
		}
		Entity viewEntity = mc.getRenderViewEntity();
		if (entity != viewEntity) {
			return true;
		}
		if (!(viewEntity instanceof EntityLivingBase)) {
			return true;
		}
		return ((EntityLivingBase) viewEntity).isPlayerSleeping();
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

	public void renderEntities(float partialTicks) {
		int pass = MinecraftForgeClient.getRenderPass();
		Minecraft mc = Minecraft.getMinecraft();
		RenderManager renderManager = mc.getRenderManager();

		if (pass == 0) {
			this.entityListStaticPass0.forEach(entity -> {
				this.preRenderEntity(entity);
				renderManager.renderEntityStatic(entity, partialTicks, false);
				this.postRenderEntity();
			});
			this.entityListMultipassPass0.forEach(entity -> {
				this.preRenderEntity(entity);
				renderManager.renderMultipass(entity, partialTicks);
				this.postRenderEntity();
			});
			if (this.isRenderEntityOutlines() && (!this.entityListOutlinePass0.isEmpty() || mc.renderGlobal.entityOutlinesRendered)) {
				mc.world.profiler.endStartSection("entityOutlines");
				mc.renderGlobal.entityOutlineFramebuffer.framebufferClear();
				mc.renderGlobal.entityOutlinesRendered = !this.entityListOutlinePass0.isEmpty();

				if (!this.entityListOutlinePass0.isEmpty()) {
					GlStateManager.depthFunc(GL11.GL_ALWAYS);
					GlStateManager.disableFog();
					mc.renderGlobal.entityOutlineFramebuffer.bindFramebuffer(false);
					RenderHelper.disableStandardItemLighting();
					renderManager.setRenderOutlines(true);

					this.entityListOutlinePass0.forEach(entity -> {
						this.preRenderEntity(entity);
						renderManager.renderEntityStatic(entity, partialTicks, false);
						this.postRenderEntity();
					});

					renderManager.setRenderOutlines(false);
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
		} else if (pass == 1) {
			this.entityListStaticPass1.forEach(entity -> {
				this.preRenderEntity(entity);
				renderManager.renderEntityStatic(entity, partialTicks, false);
				this.postRenderEntity();
			});
			this.entityListMultipassPass1.forEach(entity -> {
				this.preRenderEntity(entity);
				renderManager.renderMultipass(entity, partialTicks);
				this.postRenderEntity();
			});
		}
	}

	protected void preRenderEntity(Entity entity) {
		// workaround for stupid mods
		entity.shouldRenderInPass(MinecraftForgeClient.getRenderPass());
	}

	protected void postRenderEntity() {

	}

	protected boolean isRenderEntityOutlines() {
		Minecraft mc = Minecraft.getMinecraft();
		return mc.renderGlobal.entityOutlineFramebuffer != null && mc.renderGlobal.entityOutlineShader != null;
	}

}
