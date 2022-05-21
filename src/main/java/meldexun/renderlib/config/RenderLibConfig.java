package meldexun.renderlib.config;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.util.ResourceLocationMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.config.Config;

@Config(modid = RenderLib.MODID)
public class RenderLibConfig {

	@Config.Comment("Most tile entities have static bounding boxes and thus they can be cached. Tile entities whose bounding boxes are likely to change every frame or so should be added to the blacklist. Tile entities whose bounding only change every once in a while should be covered by cache updates (update speed adjustable through tileEntityCachedBoundingBoxUpdateInterval)")
	public static boolean tileEntityCachedBoundingBoxEnabled = true;
	@Config.Comment("Every frame there is a 1 in x chance to update the cached bounding box. Higher = better performance, Lower = tile entities with dynamic bounding boxes get updated faster.")
	@Config.RangeInt(min = 1, max = 1_000_000)
	public static int tileEntityCachedBoundingBoxUpdateInterval = 100;
	@Config.Comment("Tile entities whose bounding boxes won't be cached (Accepts 'modid' or 'modid:tileentity').")
	public static String[] tileEntityCachedBoundingBoxBlacklist = new String[0];
	@Config.Ignore
	public static ResourceLocationMap<TileEntity, Boolean> tileEntityCachedBoundingBoxBlacklistImpl = new ResourceLocationMap<>(TileEntity.REGISTRY::getNameForObject, false, s -> true);

}
