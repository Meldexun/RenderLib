package meldexun.renderlib.asm.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import net.minecraft.launchwrapper.Launch;

public class EarlyConfigLoader {

	private static final File configDir;
	static {
		try {
			configDir = new File(Launch.minecraftHome, "config").getCanonicalFile();
		} catch (IOException e) {
			throw new UnsupportedOperationException("Failed locating config directory", e);
		}
	}

	public static <T> T loadConfigEarly(String name, String category, T object) {
		File file = new File(configDir, name + ".cfg");
		if (!file.exists()) {
			return object;
		}

		try {
			loadFromConfig(ConfigFileLoader.load(file.toPath()), category, object);
		} catch (Exception e) {
			throw new ConfigLoadException("Failed loading category " + category + " from config " + name, e);
		}

		return object;
	}

	private static void loadFromConfig(ConfigCategory config, String categoryName, Object object) throws ReflectiveOperationException {
		ConfigCategory category = getCategory(config, categoryName);
		if (category != null) {
			loadFromConfig(category, object);
		}
	}

	private static ConfigCategory getCategory(ConfigCategory config, String categoryName) {
		return getCategory(config, categoryName.split("\\."), 0);
	}

	private static ConfigCategory getCategory(ConfigCategory config, String[] categoryNames, int i) {
		ConfigEntry entry = config.getEntry(categoryNames[i]);
		if (entry == null) {
			return null;
		}
		if (!(entry instanceof ConfigCategory)) {
			throw new ConfigLoadException("Config entry " + categoryNames[i] + " is not a category");
		}
		if (i < categoryNames.length - 1) {
			return getCategory((ConfigCategory) entry, categoryNames, i + 1);
		}
		return (ConfigCategory) entry;
	}

	static void loadFromConfig(ConfigCategory config, Object object) throws ReflectiveOperationException {
		for (Field field : ConfigUtil.getFields(object)) {
			String name = ConfigUtil.getName(field);
			ConfigEntry entry = config.getEntry(name);
			if (entry != null) {
				entry.load(object, field);
			}
		}
	}

}
