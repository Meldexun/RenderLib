package meldexun.renderlib.config;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.util.ResourceLocationMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.config.Config;

@Config(modid = RenderLib.MODID)
public class RenderLibConfig {

	public static OpenGLDebugOutput openGLDebugOutput = new OpenGLDebugOutput();

	@Config.Comment("Most tile entities have static bounding boxes and thus they can be cached. Tile entities whose bounding boxes are likely to change every frame or so should be added to the blacklist. Tile entities whose bounding only change every once in a while should be covered by cache updates (update speed adjustable through tileEntityCachedBoundingBoxUpdateInterval)")
	public static boolean tileEntityCachedBoundingBoxEnabled = true;
	@Config.Comment("Every frame there is a 1 in x chance to update the cached bounding box. Higher = better performance, Lower = tile entities with dynamic bounding boxes get updated faster.")
	@Config.RangeInt(min = 1, max = 1_000_000)
	public static int tileEntityCachedBoundingBoxUpdateInterval = 100;
	@Config.Comment("Tile entities whose bounding boxes won't be cached (Accepts 'modid' or 'modid:tileentity').")
	public static String[] tileEntityCachedBoundingBoxBlacklist = new String[0];
	@Config.Ignore
	public static ResourceLocationMap<TileEntity, Boolean> tileEntityCachedBoundingBoxBlacklistImpl = new ResourceLocationMap<>(TileEntity.REGISTRY::getNameForObject, false, s -> true);

	@Config.Comment("Allows you to increase the render bounding boxes of entities (or all entities of a mod). Width increases the size on the X and Z axis. Top increases the size in the positive Y direction. Bottom increases the size in the negative Y direction. (Accepts 'modid=width,top,bottom' or 'modid:entity=width,top,bottom').")
	public static String[] entityBoundingBoxGrowthList = new String[0];
	@Config.Ignore
	public static ResourceLocationMap<Entity, Vec3d> entityBoundingBoxGrowthListImpl = new ResourceLocationMap<>(EntityList::getKey, null, s -> {
		double x = s.length >= 1 ? Double.parseDouble(s[0]) : 0.0D;
		double y = s.length >= 2 ? Double.parseDouble(s[1]) : 0.0D;
		double z = s.length >= 3 ? Double.parseDouble(s[2]) : 0.0D;
		return x != 0.0D || y != 0.0D || z != 0.0D ? new Vec3d(x, y, z) : null;
	});

	@Config.Comment("Allows you to increase the render bounding boxes of tile entities (or all entities of a mod). Width increases the size on the X and Z axis. Top increases the size in the positive Y direction. Bottom increases the size in the negative Y direction. (Accepts 'modid=width,top,bottom' or 'modid:tileentity=width,top,bottom').")
	public static String[] tileEntityBoundingBoxGrowthList = new String[0];
	@Config.Ignore
	public static ResourceLocationMap<TileEntity, Vec3d> tileEntityBoundingBoxGrowthListImpl = new ResourceLocationMap<>(TileEntity.REGISTRY::getNameForObject, null, s -> {
		double x = s.length >= 1 ? Double.parseDouble(s[0]) : 0.0D;
		double y = s.length >= 2 ? Double.parseDouble(s[1]) : 0.0D;
		double z = s.length >= 3 ? Double.parseDouble(s[2]) : 0.0D;
		return x != 0.0D || y != 0.0D || z != 0.0D ? new Vec3d(x, y, z) : null;
	});

	public static class OpenGLDebugOutput {

		@Config.Comment("Better debugging of OpenGL errors. (Requires OpenGL 4.3!)")
		public boolean enabled = false;
		@Config.Comment("Enable/Disable logging of high severity non-error messages. (You probably never want to enable this as a normal user)")
		public boolean logHighSeverity = false;
		@Config.Comment("Enable/Disable logging of medium severity non-error messages. (You probably never want to enable this as a normal user)")
		public boolean logMediumSeverity = false;
		@Config.Comment("Enable/Disable logging of low severity non-error messages. (You probably never want to enable this as a normal user)")
		public boolean logLowSeverity = false;
		@Config.Comment("Enable/Disable logging of notification severity non-error messages. (You probably never want to enable this as a normal user)")
		public boolean logNotificationSeverity = false;

	}

	public static void onConfigChanged() {
		tileEntityCachedBoundingBoxBlacklistImpl.load(tileEntityCachedBoundingBoxBlacklist);
		entityBoundingBoxGrowthListImpl.load(entityBoundingBoxGrowthList);
		tileEntityBoundingBoxGrowthListImpl.load(tileEntityBoundingBoxGrowthList);
	}

}
