package meldexun.renderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner.BlockBannerHanging;
import net.minecraft.block.BlockBanner.BlockBannerStanding;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Mixin(TileEntityBanner.class)
public class MixinTileEntityBanner extends TileEntity {

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		BlockPos pos = this.getPos();
		double x0 = pos.getX();
		double y0 = pos.getY();
		double z0 = pos.getZ();
		double x1 = pos.getX() + 1.0D;
		double y1 = pos.getY() + 1.0D;
		double z1 = pos.getZ() + 1.0D;
		Block block = this.getBlockType();
		if (block instanceof BlockBannerHanging) {
			y0--;
		} else if (block instanceof BlockBannerStanding) {
			y1++;
		}
		return new AxisAlignedBB(x0, y0, z0, x1, y1, z1);
	}

}
