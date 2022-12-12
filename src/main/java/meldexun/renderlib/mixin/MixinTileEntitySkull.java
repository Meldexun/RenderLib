package meldexun.renderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Mixin(TileEntitySkull.class)
public class MixinTileEntitySkull extends TileEntity {

	@Shadow
	private int skullType;

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		BlockPos pos = this.getPos();
		double x0 = pos.getX();
		double y0 = pos.getY();
		double z0 = pos.getZ();
		double x1 = pos.getX() + 1.0D;
		double y1 = pos.getY() + 1.0D;
		double z1 = pos.getZ() + 1.0D;
		if (this.skullType == 5) {
			x0 -= 0.75D;
			y0 -= 0.5D;
			z0 -= 0.75D;
			x1 += 0.75D;
			z1 += 0.75D;
		} else {
			y1 -= 0.25D;
			if (this.skullType == 2 || this.skullType == 3) {
				x0 -= 0.125D;
				y0 -= 0.125D;
				z0 -= 0.125D;
				x1 += 0.125D;
				y1 += 0.125D;
				z1 += 0.125D;
			}
		}
		return new AxisAlignedBB(x0, y0, z0, x1, y1, z1);
	}

}
