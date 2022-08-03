package meldexun.renderlib.integration;

import org.valkyrienskies.mod.common.ships.ship_transform.ShipTransformationManager;
import org.valkyrienskies.mod.common.ships.ship_world.PhysicsObject;
import org.valkyrienskies.mod.common.util.ValkyrienUtils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import valkyrienwarfare.api.TransformType;

public class ValkyrienSkies {

	public static AxisAlignedBB getAABB(TileEntity tileEntity) {
		return ValkyrienUtils.getAABBInGlobal(tileEntity.getRenderBoundingBox(), tileEntity.getWorld(), tileEntity.getPos());
	}

	public static BlockPos getPos(TileEntity tileEntity) {
		return ValkyrienUtils.getPhysoManagingBlock(tileEntity.getWorld(), tileEntity.getPos())
				.map(PhysicsObject::getShipTransformationManager)
				.map(ShipTransformationManager::getCurrentTickTransform)
				.map(shipTransform -> shipTransform.transform(tileEntity.getPos(), TransformType.SUBSPACE_TO_GLOBAL))
				.orElse(null);
	}

}
