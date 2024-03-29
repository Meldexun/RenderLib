package meldexun.renderlib.asm.config;

import java.lang.reflect.Field;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

class ConfigCategory extends ConfigEntry {

	private final Map<String, ConfigEntry> entries = new Object2ObjectOpenHashMap<>();

	void putEntry(String name, ConfigEntry entry) {
		entries.put(name, entry);
	}

	ConfigEntry getEntry(String name) {
		return entries.get(name);
	}

	@Override
	void load(Object object, Field field) throws ReflectiveOperationException {
		Object subObject = field.get(object);
		if (subObject == null) {
			subObject = field.getType().getConstructor().newInstance();
			field.set(object, subObject);
		}
		EarlyConfigLoader.loadFromConfig(this, subObject);
	}

}
