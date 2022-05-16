package meldexun.renderlib.mixin.caching.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import meldexun.renderlib.util.ITileEntityRendererCache;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

@Mixin(TileEntityRendererDispatcher.class)
public class MixinTileEntityRendererDispatcher {

	/** {@link TileEntityRendererDispatcher#render(TileEntity, double, double, double, float, int, float)} */
	@Redirect(method = "render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;getRenderer(Lnet/minecraft/tileentity/TileEntity;)Lnet/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer;"))
	public TileEntitySpecialRenderer<TileEntity> render_getRenderer(TileEntityRendererDispatcher tileEntityRenderDispatcher, TileEntity tileEntity) {
		if (tileEntity == null)
			return null;
		if (tileEntity.isInvalid())
			return null;
		return ((ITileEntityRendererCache) tileEntity).getRenderer();
	}

}
