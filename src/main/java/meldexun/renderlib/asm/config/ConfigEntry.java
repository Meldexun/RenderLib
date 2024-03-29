package meldexun.renderlib.asm.config;

import java.lang.reflect.Field;

abstract class ConfigEntry {

	abstract void load(Object object, Field field) throws ReflectiveOperationException;

}
