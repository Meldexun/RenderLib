package meldexun.renderlib.renderer.tileentity;

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

	protected int renderedTileEntities;
	protected int occludedTileEntities;
	protected int totalTileEntities;
	private int camChunkX;
	private int camChunkZ;
	private int renderDist;

	public void renderTileEntities(ICamera frustum, float partialTicks, double camX, double camY, double camZ) {
		Minecraft mc = Minecraft.getMinecraft();
		this.renderedTileEntities = 0;
		this.occludedTileEntities = 0;
		this.totalTileEntities = 0;
		this.camChunkX = MathHelper.floor(RenderUtil.getCameraEntityX()) >> 4;
		this.camChunkZ = MathHelper.floor(RenderUtil.getCameraEntityZ()) >> 4;
		this.renderDist = mc.gameSettings.renderDistanceChunks;

		for (TileEntity tileEntity : mc.world.loadedTileEntityList) {
			if (!shouldRender(tileEntity, frustum, partialTicks, camX, camY, camZ)) {
				continue;
			}
			if (isOcclusionCulled(tileEntity)) {
				this.occludedTileEntities++;
			} else {
				this.renderedTileEntities++;
				this.preRenderTileEntity(tileEntity);
				TileEntityRendererDispatcher.instance.render(tileEntity, partialTicks, -1);
				this.postRenderTileEntity();
			}
		}
	}

	private boolean shouldRender(TileEntity tileEntity, ICamera frustum, float partialTicks, double camX, double camY, double camZ) {
		if (!((ITileEntityRendererCache) tileEntity).hasRenderer()) {
			return false;
		}
		if (!((ILoadable) tileEntity).isChunkLoaded()) {
			return false;
		}
		if (!tileEntity.shouldRenderInPass(MinecraftForgeClient.getRenderPass())) {
			return false;
		}
		if (isOutsideOfRenderDist(tileEntity)) {
			return false;
		}

		this.totalTileEntities++;

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
		int tileEntityX = tileEntity.getPos().getX() >> 4;
		if (Math.abs(tileEntityX - camChunkX) > renderDist)
			return true;
		int tileEntityZ = tileEntity.getPos().getZ() >> 4;
		return Math.abs(tileEntityZ - camChunkZ) > renderDist;
	}

	protected <T extends TileEntity> void setCanBeOcclusionCulled(T tileEntity, boolean canBeOcclusionCulled) {

	}

	protected <T extends TileEntity> boolean isOcclusionCulled(T tileEntity) {
		return false;
	}

	protected void preRenderTileEntity(TileEntity tileEntity) {

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
