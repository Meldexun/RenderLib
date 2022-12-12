package meldexun.renderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Mixin(TileEntityEnchantmentTable.class)
public class MixinTileEntityEnchantingTable extends TileEntity {

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		BlockPos pos = this.getPos();
		double x0 = pos.getX();
		double y0 = pos.getY();
		double z0 = pos.getZ();
		double x1 = pos.getX() + 1.0D;
		double y1 = pos.getY() + 1.3D;
		double z1 = pos.getZ() + 1.0D;
		return new AxisAlignedBB(x0, y0, z0, x1, y1, z1);
	}

}
