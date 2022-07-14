package meldexun.renderlib.mixin.caching.boundingbox;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.renderlib.api.IBoundingBoxCache;
import meldexun.renderlib.api.IEntityRendererCache;
import meldexun.renderlib.api.ILoadable;
import meldexun.renderlib.api.ITileEntityRendererCache;
import meldexun.renderlib.util.EntityUtil;
import meldexun.renderlib.util.TileEntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

	/** {@link EntityRenderer#renderWorld(float, long)} */
	@Inject(method = "renderWorld", at = @At("HEAD"))
	public void renderWorld(float partialTicks, long finishTimeNano, CallbackInfo info) {
		Minecraft mc = Minecraft.getMinecraft();

		if (mc.world == null)
			return;

		for (Entity e : EntityUtil.entityIterable(mc.world.loadedEntityList)) {
			if (((IEntityRendererCache) e).hasRenderer() && ((ILoadable) e).isChunkLoaded())
				((IBoundingBoxCache) e).updateCachedBoundingBox(partialTicks);
		}

		TileEntityUtil.processTileEntities(mc.world, tileEntities -> {
			for (TileEntity te : tileEntities) {
				if (((ITileEntityRendererCache) te).hasRenderer() && ((ILoadable) te).isChunkLoaded())
					((IBoundingBoxCache) te).updateCachedBoundingBox(partialTicks);
			}
		});
	}

}
