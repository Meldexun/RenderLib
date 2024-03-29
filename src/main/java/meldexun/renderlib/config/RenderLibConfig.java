package meldexun.renderlib.config;

import java.util.Arrays;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.opengl.debug.GLDebugMessageFilter;
import meldexun.renderlib.opengl.debug.LogStackTraceMode;
import meldexun.renderlib.opengl.debug.Severity;
import meldexun.renderlib.opengl.debug.Source;
import meldexun.renderlib.opengl.debug.Type;
import meldexun.renderlib.util.ResourceLocationMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.config.Config;

@Config(modid = RenderLib.MODID)
public class RenderLibConfig {

	public static boolean debugRenderBoxes = false;

	public static int mainMenuFPS = 60;
	@Config.Comment("If set to true the main menu FPS is synced to the in game FPS (but clamped between 30 and 240).")
	public static boolean mainMenuFPSSynced = false;

	public static OpenGLDebugConfiguration openGLDebugOutput = new OpenGLDebugConfiguration();
	public static boolean openGLLogExtensions = false;

	public static boolean showFrameTimes = false;

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

	public static class OpenGLDebugConfiguration {

		@Config.Comment("Enable/Disable crashing when an OpenGL error occurs. If disabled OpenGL errors are likely to go unnoticed unless the log is checked manually.")
		public boolean crashOnError = false;
		@Config.Comment("Better debugging of OpenGL errors. Might not be supported by your hardware/driver.")
		public boolean enabled = true;
		@Config.Comment("Enable/Disable appending of the stack trace when logging a debug message.")
		public LogStackTraceMode logStackTrace = LogStackTraceMode.ERRORS_ONLY;
		@Config.Comment("Enable/Disable debug messages matching the specified filters."
				+ "\n" + "Format: 'source, type, severity, enabled'"
				+ "\n" + "Valid source values: [ ANY, API, WINDOW_SYSTEM, SHADER_COMPILER, THIRD_PARTY, APPLICATION, OTHER ]"
				+ "\n" + "Valid type values: [ ANY, ERROR, DEPRECATED_BEHAVIOR, UNDEFINED_BEHAVIOR, PORTABILITY, PERFORMANCE, MARKER, PUSH_GROUP, POP_GROUP, OTHER ]"
				+ "\n" + "Valid severity values: [ ANY, HIGH, MEDIUM, LOW, NOTIFICATION ]")
		public String[] messageFilters = { new GLDebugMessageFilter(Source.ANY, Type.ERROR, Severity.ANY, true).toString() };
		@Config.Comment("May be required by some systems to generate OpenGL debug output. (Enabling might have a negative impact on performance)")
		@Config.RequiresMcRestart
		public boolean setContextDebugBit = false;

		public GLDebugMessageFilter[] getMessageFilters() {
			return Arrays.stream(messageFilters).map(GLDebugMessageFilter::new).toArray(GLDebugMessageFilter[]::new);
		}

	}

	public static void onConfigChanged() {
		tileEntityCachedBoundingBoxBlacklistImpl.load(tileEntityCachedBoundingBoxBlacklist);
		entityBoundingBoxGrowthListImpl.load(entityBoundingBoxGrowthList);
		tileEntityBoundingBoxGrowthListImpl.load(tileEntityBoundingBoxGrowthList);
	}

}
