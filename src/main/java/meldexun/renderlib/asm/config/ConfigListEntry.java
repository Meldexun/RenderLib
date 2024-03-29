package meldexun.renderlib.asm.config;

import java.lang.reflect.Field;
import java.util.List;

class ConfigListEntry extends ConfigEntry {

	private final Type type;
	private final List<String> list;

	ConfigListEntry(Type type, List<String> list) {
		this.type = type;
		this.list = list;
	}

	@Override
	void load(Object object, Field field) throws ReflectiveOperationException {
		type.setListField(object, field, list);
	}

}
