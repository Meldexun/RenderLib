package meldexun.renderlib.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import meldexun.renderlib.api.ITileEntityRendererCache;
import meldexun.renderlib.util.ITileEntityHolder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@Mixin(World.class)
public class MixinWorld implements ITileEntityHolder {

	@Shadow
	@Final
	public boolean isRemote;
	@Unique
	private List<TileEntity> renderableTileEntityList = new ArrayList<>();

	@ModifyVariable(method = "updateEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z", remap = false, ordinal = 2, shift = Shift.AFTER), index = 1, ordinal = 0, name = "remove")
	public Set<TileEntity> updateEntities_removeAll(Set<TileEntity> tileEntities) {
		if (isRemote) {
			renderableTileEntityList.removeAll(tileEntities);
		}
		return tileEntities;
	}

	@ModifyVariable(method = "updateEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z", remap = false, ordinal = 0, shift = Shift.AFTER), index = 2, ordinal = 0, name = "tileentity")
	public TileEntity updateEntities_remove(TileEntity tileEntity) {
		if (isRemote && ((ITileEntityRendererCache) tileEntity).hasRenderer()) {
			renderableTileEntityList.remove(tileEntity);
		}
		return tileEntity;
	}

	@ModifyVariable(method = "addTileEntity", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false, ordinal = 1, shift = Shift.AFTER), index = 1, ordinal = 0, name = "tile")
	public TileEntity addTileEntity_add(TileEntity tileEntity) {
		if (isRemote && ((ITileEntityRendererCache) tileEntity).hasRenderer()) {
			renderableTileEntityList.add(tileEntity);
		}
		return tileEntity;
	}

	@ModifyVariable(method = "removeTileEntity", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z", remap = false, ordinal = 1, shift = Shift.AFTER), index = 2, ordinal = 0, name = "tileentity2")
	public TileEntity removeTileEntity_remove1(TileEntity tileEntity) {
		if (isRemote && ((ITileEntityRendererCache) tileEntity).hasRenderer()) {
			renderableTileEntityList.remove(tileEntity);
		}
		return tileEntity;
	}

	@ModifyVariable(method = "removeTileEntity", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z", remap = false, ordinal = 3, shift = Shift.AFTER), index = 2, ordinal = 0, name = "tileentity2")
	public TileEntity removeTileEntity_remove2(TileEntity tileEntity) {
		if (isRemote && ((ITileEntityRendererCache) tileEntity).hasRenderer()) {
			renderableTileEntityList.remove(tileEntity);
		}
		return tileEntity;
	}

	@Override
	public List<TileEntity> getTileEntities() {
		return renderableTileEntityList;
	}

}
