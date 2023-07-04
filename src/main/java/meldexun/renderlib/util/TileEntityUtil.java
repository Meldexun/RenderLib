package meldexun.renderlib.util;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class TileEntityUtil {

	public static void processTileEntities(World world, Consumer<List<TileEntity>> processor) {
		world.processingLoadedTiles = true;

		processor.accept(((ITileEntityHolder) world).getTileEntities());

		world.processingLoadedTiles = false;

		for (TileEntity tileEntity : world.addedTileEntityList) {
			if (tileEntity.isInvalid()) {
				continue;
			}

			if (!world.loadedTileEntityList.contains(tileEntity)) {
				world.addTileEntity(tileEntity);
			}
			if (world.isBlockLoaded(tileEntity.getPos())) {
				Chunk chunk = world.getChunk(tileEntity.getPos());
				IBlockState iblockstate = chunk.getBlockState(tileEntity.getPos());
				chunk.addTileEntity(tileEntity.getPos(), tileEntity);
				world.notifyBlockUpdate(tileEntity.getPos(), iblockstate, iblockstate, 3);
			}
		}
		world.addedTileEntityList.clear();
	}

}
