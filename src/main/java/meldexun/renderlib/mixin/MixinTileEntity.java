package meldexun.renderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(TileEntity.class)
public class MixinTileEntity {

	@Shadow
	private BlockPos pos;
	@Shadow
	private World world;

	@Overwrite(remap = false)
	public AxisAlignedBB getRenderBoundingBox() {
		BlockPos pos = this.pos;
		try {
			World world = this.world;
			return world.getBlockState(pos).getBoundingBox(world, pos).offset(pos);
		} catch (Exception e) {
			return new AxisAlignedBB(pos);
		}
	}

}
