package meldexun.renderlib.renderer.tileentity;

import java.util.ArrayDeque;
import java.util.Deque;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.api.IBoundingBoxCache;
import meldexun.renderlib.api.ILoadable;
import meldexun.renderlib.integration.ValkyrienSkies;
import meldexun.renderlib.util.RenderUtil;
import meldexun.renderlib.util.TileEntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;

public class TileEntityRenderer {

	protected int renderedTileEntities;
	protected int occludedTileEntities;
	protected int totalTileEntities;
	private int camChunkX;
	private int camChunkZ;
	private int renderDist;
	private final Deque<TileEntityRenderList> tileEntityListQueue = new ArrayDeque<>();

	public void setup(ICamera frustum, float partialTicks, double camX, double camY, double camZ) {
		Minecraft mc = Minecraft.getMinecraft();
		this.renderedTileEntities = 0;
		this.occludedTileEntities = 0;
		this.totalTileEntities = mc.world.loadedTileEntityList.size();
		this.camChunkX = MathHelper.floor(RenderUtil.getCameraEntityX()) >> 4;
		this.camChunkZ = MathHelper.floor(RenderUtil.getCameraEntityZ()) >> 4;
		this.renderDist = mc.gameSettings.renderDistanceChunks;

		TileEntityRenderList tileEntityList = new TileEntityRenderList();
		TileEntityUtil.processTileEntities(mc.world, tileEntity -> {
			if (!this.shouldRender(tileEntity, frustum, partialTicks, camX, camY, camZ)) {
				return;
			}

			if (this.isOcclusionCulled(tileEntity)) {
				this.occludedTileEntities++;
			} else {
				this.renderedTileEntities++;

				tileEntityList.addTileEntity(tileEntity);
			}
		});
		this.tileEntityListQueue.addLast(tileEntityList);
	}

	public void reset() {
		this.tileEntityListQueue.removeLast();
	}

	public void renderTileEntities(float partialTicks) {
		this.renderTileEntities(partialTicks, this.tileEntityListQueue.getLast());
	}

	protected void renderTileEntities(float partialTicks, TileEntityRenderList tileEntityList) {
		for (TileEntity tileEntity : tileEntityList.getTileEntities()) {
			this.preRenderTileEntity(tileEntity);
			TileEntityRendererDispatcher.instance.render(tileEntity, partialTicks, -1);
			this.postRenderTileEntity();
		}
	}

	private boolean shouldRender(TileEntity tileEntity, ICamera frustum, float partialTicks, double camX, double camY, double camZ) {
		if (!((ILoadable) tileEntity).isChunkLoaded()) {
			return false;
		}
		if (!tileEntity.shouldRenderInPass(0) && !tileEntity.shouldRenderInPass(1)) {
			return false;
		}
		if (this.isOutsideOfRenderDist(tileEntity)) {
			return false;
		}

		if (!((IBoundingBoxCache) tileEntity).getCachedBoundingBox().isVisible(frustum)) {
			this.setCanBeOcclusionCulled(tileEntity, false);
			return false;
		}
		if (tileEntity.getDistanceSq(camX, camY, camZ) >= tileEntity.getMaxRenderDistanceSquared()) {
			this.setCanBeOcclusionCulled(tileEntity, false);
			return false;
		}

		this.setCanBeOcclusionCulled(tileEntity, true);
		return true;
	}

	private boolean isOutsideOfRenderDist(TileEntity tileEntity) {
		BlockPos pos = RenderLib.isValkyrienSkiesInstalled ? ValkyrienSkies.getPos(tileEntity) : tileEntity.getPos();
		return Math.abs((pos.getX() >> 4) - this.camChunkX) > this.renderDist || Math.abs((pos.getZ() >> 4) - this.camChunkZ) > this.renderDist;
	}

	protected <T extends TileEntity> void setCanBeOcclusionCulled(T tileEntity, boolean canBeOcclusionCulled) {

	}

	protected <T extends TileEntity> boolean isOcclusionCulled(T tileEntity) {
		return false;
	}

	protected void preRenderTileEntity(TileEntity tileEntity) {
		tileEntity.shouldRenderInPass(MinecraftForgeClient.getRenderPass());
	}

	protected void postRenderTileEntity() {

	}

	public int getRenderedTileEntities() {
		return this.renderedTileEntities;
	}

	public int getOccludedTileEntities() {
		return this.occludedTileEntities;
	}

	public int getTotalTileEntities() {
		return this.totalTileEntities;
	}

}
