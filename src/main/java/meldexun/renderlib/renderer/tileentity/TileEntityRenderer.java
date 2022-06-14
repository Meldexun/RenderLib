package meldexun.renderlib.renderer.tileentity;

import java.util.ArrayDeque;
import java.util.Queue;

import meldexun.renderlib.api.IBoundingBoxCache;
import meldexun.renderlib.api.ILoadable;
import meldexun.renderlib.api.ITileEntityRendererCache;
import meldexun.renderlib.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;

public class TileEntityRenderer {

	protected final Queue<TileEntity> tileEntityListPass0 = new ArrayDeque<>();
	protected final Queue<TileEntity> tileEntityListPass1 = new ArrayDeque<>();
	protected int renderedTileEntities;
	protected int occludedTileEntities;
	protected int totalTileEntities;

	public void setup(ICamera camera, double camX, double camY, double camZ, double partialTicks) {
		this.renderedTileEntities = 0;
		this.occludedTileEntities = 0;
		this.totalTileEntities = 0;
		this.clearTileEntityLists();
		this.fillTileEntityLists(camera, camX, camY, camZ, partialTicks);
	}

	protected void clearTileEntityLists() {
		this.tileEntityListPass0.clear();
		this.tileEntityListPass1.clear();
	}

	protected void fillTileEntityLists(ICamera camera, double camX, double camY, double camZ, double partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.world.loadedTileEntityList.forEach(tileEntity -> this.addToRenderLists(tileEntity, camera, camX, camY, camZ, partialTicks));
	}

	protected <T extends TileEntity> void addToRenderLists(T tileEntity, ICamera camera, double camX, double camY, double camZ, double partialTicks) {
		if (!((ITileEntityRendererCache) tileEntity).hasRenderer()) {
			return;
		}
		if (!((ILoadable) tileEntity).isChunkLoaded()) {
			return;
		}
		if (isOutsideOfRenderDist(tileEntity, partialTicks)) {
			return;
		}

		this.totalTileEntities++;

		if (!((IBoundingBoxCache) tileEntity).getCachedBoundingBox().isVisible(camera)) {
			this.setCanBeOcclusionCulled(tileEntity, false);
			return;
		}
		if (tileEntity.getDistanceSq(camX, camY, camZ) >= tileEntity.getMaxRenderDistanceSquared()) {
			this.setCanBeOcclusionCulled(tileEntity, false);
			return;
		}

		this.setCanBeOcclusionCulled(tileEntity, true);

		if (this.isOcclusionCulled(tileEntity)) {
			this.occludedTileEntities++;
		} else {
			this.renderedTileEntities++;
			this.render(tileEntity);
		}
	}

	private <T extends TileEntity> boolean isOutsideOfRenderDist(T tileEntity, double partialTicks) {
		double renderDist = Minecraft.getMinecraft().gameSettings.renderDistanceChunks;
		double entityX = tileEntity.getPos().getX() >> 4;
		double entityZ = tileEntity.getPos().getZ() >> 4;
		double playerX = MathHelper.floor(RenderUtil.getCameraEntityX()) >> 4;
		double playerZ = MathHelper.floor(RenderUtil.getCameraEntityZ()) >> 4;
		return Math.abs(entityX - playerX) > renderDist || Math.abs(entityZ - playerZ) > renderDist;
	}

	protected <T extends TileEntity> void render(T tileEntity) {
		if (tileEntity.shouldRenderInPass(0)) {
			this.tileEntityListPass0.add(tileEntity);
		}
		if (tileEntity.shouldRenderInPass(1)) {
			this.tileEntityListPass1.add(tileEntity);
		}
	}

	protected <T extends TileEntity> void setCanBeOcclusionCulled(T tileEntity, boolean canBeOcclusionCulled) {

	}

	protected <T extends TileEntity> boolean isOcclusionCulled(T tileEntity) {
		return false;
	}

	public void renderTileEntities(float partialTicks) {
		int pass = MinecraftForgeClient.getRenderPass();

		if (pass == 0) {
			this.tileEntityListPass0.forEach(tileEntity -> {
				this.preRenderTileEntity(tileEntity);
				TileEntityRendererDispatcher.instance.render(tileEntity, partialTicks, -1);
				this.postRenderTileEntity();
			});
		} else if (pass == 1) {
			this.tileEntityListPass1.forEach(tileEntity -> {
				this.preRenderTileEntity(tileEntity);
				TileEntityRendererDispatcher.instance.render(tileEntity, partialTicks, -1);
				this.postRenderTileEntity();
			});
		}
	}

	protected void preRenderTileEntity(TileEntity tileEntity) {
		// workaround for stupid mods
		tileEntity.shouldRenderInPass(MinecraftForgeClient.getRenderPass());
	}

	protected void postRenderTileEntity() {

	}

	public int getRenderedTileEntities() {
		return renderedTileEntities;
	}

	public int getOccludedTileEntities() {
		return occludedTileEntities;
	}

	public int getTotalTileEntities() {
		return totalTileEntities;
	}

}
