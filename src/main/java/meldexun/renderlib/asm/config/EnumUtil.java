package meldexun.renderlib.asm.config;

public class EnumUtil {

	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T valueOf(Class<?> enumClass, String name) {
		return Enum.valueOf((Class<T>) enumClass, name);
	}

}
