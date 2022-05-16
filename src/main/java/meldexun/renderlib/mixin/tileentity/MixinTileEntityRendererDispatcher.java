package meldexun.renderlib.mixin.tileentity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(TileEntityRendererDispatcher.class)
public class MixinTileEntityRendererDispatcher {

	/** {@link TileEntityRendererDispatcher#render(TileEntity, float, int)} */
	@Redirect(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;getDistanceSq(DDD)D"))
	public double render_getDistanceSq(TileEntity tileEntity, double x, double y, double z) {
		return 0.0D;
	}

	/** {@link TileEntityRendererDispatcher#render(TileEntity, float, int)} */
	@Redirect(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;getMaxRenderDistanceSquared()D"))
	public double render_getMaxRenderDistanceSquared(TileEntity tileEntity) {
		return 1.0D;
	}

	/** {@link TileEntityRendererDispatcher#render(TileEntity, float, int)} */
	@Redirect(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isBlockLoaded(Lnet/minecraft/util/math/BlockPos;Z)Z"))
	public boolean render_isBlockLoaded(World world, BlockPos pos, boolean allowEmpty) {
		return true;
	}

}
