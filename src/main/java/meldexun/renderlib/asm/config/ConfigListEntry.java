package meldexun.renderlib.asm.config;

import java.lang.reflect.Field;

class ConfigListEntry extends ConfigEntry {

	private final Type type;
	private final String[] list;

	ConfigListEntry(Type type, String[] list) {
		this.type = type;
		this.list = list;
	}

	@Override
	void load(Object object, Field field) throws ReflectiveOperationException {
		type.setListField(object, field, list);
	}

}
