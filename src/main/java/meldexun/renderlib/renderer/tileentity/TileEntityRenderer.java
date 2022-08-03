package meldexun.renderlib.renderer.tileentity;

import meldexun.renderlib.api.IBoundingBoxCache;
import meldexun.renderlib.api.ILoadable;
import meldexun.renderlib.api.ITileEntityRendererCache;
import meldexun.renderlib.util.IRenderable;
import meldexun.renderlib.util.RenderUtil;
import meldexun.renderlib.util.TileEntityUtil;
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
		if (MinecraftForgeClient.getRenderPass() == 0) {
			this.renderedTileEntities = 0;
			this.occludedTileEntities = 0;
			this.totalTileEntities = mc.world.loadedTileEntityList.size();
		}
		this.camChunkX = MathHelper.floor(RenderUtil.getCameraEntityX()) >> 4;
		this.camChunkZ = MathHelper.floor(RenderUtil.getCameraEntityZ()) >> 4;
		this.renderDist = mc.gameSettings.renderDistanceChunks;

		TileEntityUtil.processTileEntities(mc.world, tileEntities -> {
			for (TileEntity tileEntity : tileEntities) {
				if (!shouldRender(tileEntity, frustum, partialTicks, camX, camY, camZ)) {
					continue;
				}
				if (isOcclusionCulled(tileEntity)) {
					if (((IRenderable) tileEntity).setLastTimeRendered(RenderUtil.getFrame())) {
						this.occludedTileEntities++;
					}
				} else {
					if (((IRenderable) tileEntity).setLastTimeRendered(RenderUtil.getFrame())) {
						this.renderedTileEntities++;
					}
					this.preRenderTileEntity(tileEntity);
					TileEntityRendererDispatcher.instance.render(tileEntity, partialTicks, -1);
					this.postRenderTileEntity();
				}
			}
		});
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
