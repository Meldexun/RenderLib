package meldexun.renderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(TileEntityChest.class)
public class MixinTileEntityChest extends TileEntity {

	@Shadow
	public TileEntityChest adjacentChestZNeg;
	@Shadow
	public TileEntityChest adjacentChestXPos;
	@Shadow
	public TileEntityChest adjacentChestXNeg;
	@Shadow
	public TileEntityChest adjacentChestZPos;

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		BlockPos pos = this.getPos();
		double x0 = pos.getX();
		double y0 = pos.getY();
		double z0 = pos.getZ();
		double x1 = pos.getX() + 1.0D;
		double y1 = pos.getY() + 1.6D;
		double z1 = pos.getZ() + 1.0D;

		EnumFacing facing = EnumFacing.NORTH;
		if (this.hasWorld()) {
			World world = this.getWorld();
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() instanceof BlockChest) {
				facing = state.getValue(BlockChest.FACING);
			}
		}
		switch (facing) {
		case NORTH:
			z1 += 0.25D;
			break;
		case SOUTH:
			z0 -= 0.25D;
			break;
		case WEST:
			x1 += 0.25D;
			break;
		case EAST:
			x0 -= 0.25D;
			break;
		default:
			throw new IllegalArgumentException();
		}

		if (this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
			if (this.adjacentChestXPos != null) {
				x1++;
			} else if (this.adjacentChestZPos != null) {
				z1++;
			}
		}

		return new AxisAlignedBB(x0, y0, z0, x1, y1, z1);
	}

}
