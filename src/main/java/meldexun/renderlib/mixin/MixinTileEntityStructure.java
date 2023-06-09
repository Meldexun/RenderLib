package meldexun.renderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.tileentity.TileEntityStructure.Mode;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Mixin(TileEntityStructure.class)
public class MixinTileEntityStructure extends TileEntity {

	@Shadow
	private BlockPos position;
	@Shadow
	private BlockPos size;
	@Shadow
	private Mirror mirror;
	@Shadow
	private Rotation rotation;
	@Shadow
	private TileEntityStructure.Mode mode;

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		if (this.size.getX() <= 0 || this.size.getY() <= 0 || this.size.getZ() <= 0) {
			return new AxisAlignedBB(this.pos);
		}

		if (this.mode != Mode.SAVE && this.mode != Mode.LOAD) {
			return new AxisAlignedBB(this.pos);
		}

		double x = this.pos.getX();
		double y = this.pos.getY();
		double z = this.pos.getZ();

		double offsetX = this.position.getX();
		double offsetZ = this.position.getZ();
		double y0 = y + this.position.getY() - 0.01D;
		double y1 = y0 + this.size.getY() + 0.02D;
		double sizeX;
		double sizeZ;

		switch (this.mirror) {
		case LEFT_RIGHT:
			sizeX = this.size.getX() + 0.02D;
			sizeZ = -(this.size.getZ() + 0.02D);
			break;
		case FRONT_BACK:
			sizeX = -(this.size.getX() + 0.02D);
			sizeZ = this.size.getZ() + 0.02D;
			break;
		default:
			sizeX = this.size.getX() + 0.02D;
			sizeZ = this.size.getZ() + 0.02D;
		}

		double x0;
		double z0;
		double x1;
		double z1;

		switch (this.rotation) {
		case CLOCKWISE_90:
			x0 = x + (sizeZ < 0.0D ? offsetX - 0.01D : offsetX + 1.0D + 0.01D);
			z0 = z + (sizeX < 0.0D ? offsetZ + 1.0D + 0.01D : offsetZ - 0.01D);
			x1 = x0 - sizeZ;
			z1 = z0 + sizeX;
			break;
		case CLOCKWISE_180:
			x0 = x + (sizeX < 0.0D ? offsetX - 0.01D : offsetX + 1.0D + 0.01D);
			z0 = z + (sizeZ < 0.0D ? offsetZ - 0.01D : offsetZ + 1.0D + 0.01D);
			x1 = x0 - sizeX;
			z1 = z0 - sizeZ;
			break;
		case COUNTERCLOCKWISE_90:
			x0 = x + (sizeZ < 0.0D ? offsetX + 1.0D + 0.01D : offsetX - 0.01D);
			z0 = z + (sizeX < 0.0D ? offsetZ - 0.01D : offsetZ + 1.0D + 0.01D);
			x1 = x0 + sizeZ;
			z1 = z0 - sizeX;
			break;
		default:
			x0 = x + (sizeX < 0.0D ? offsetX + 1.0D + 0.01D : offsetX - 0.01D);
			z0 = z + (sizeZ < 0.0D ? offsetZ + 1.0D + 0.01D : offsetZ - 0.01D);
			x1 = x0 + sizeX;
			z1 = z0 + sizeZ;
		}

		return new AxisAlignedBB(x0, y0, z0, x1, y1, z1);
	}

}
