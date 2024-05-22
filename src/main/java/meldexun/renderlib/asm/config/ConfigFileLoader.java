package meldexun.renderlib.asm.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ConfigFileLoader {

	static ConfigCategory load(Path file) {
		ConfigCategory root = new ConfigCategory();

		try (ConfigReader reader = new ConfigReader(Files.newBufferedReader(file))) {
			loadCategory(reader, root, true);
		} catch (Exception e) {
			throw new ConfigLoadException("Failed to load config from file " + file.toAbsolutePath().toString(), e);
		}

		return root;
	}

	private static void loadCategory(ConfigReader reader, ConfigCategory category, boolean isRoot) throws IOException {
		int startingLine = reader.lineNumber();
		for (String line : reader) {
			if (line.startsWith("~")) {
				continue;
			}
			if (line.length() >= 2 && line.charAt(1) == ':') {
				Type type = Type.get(line.charAt(0));
				if (loadEntry(line, 2, type, category)) {
					continue;
				}
				if (loadListEntry(reader, line, 2, type, category)) {
					continue;
				}
				throw new ConfigLoadException("Malformed entry at line " + reader.lineNumber());
			}
			if (line.endsWith("{")) {
				String name = line.substring(0, line.length() - 1).trim().toLowerCase(Locale.ENGLISH);
				ConfigCategory subCategory = new ConfigCategory();
				loadCategory(reader, subCategory, false);
				category.putEntry(name, subCategory);
				continue;
			}
			if (!isRoot && line.equals("}")) {
				return;
			}
			throw new ConfigLoadException("Malformed entry at line " + reader.lineNumber());
		}
		if (!isRoot) {
			throw new ConfigLoadException("Category starting at line " + startingLine + " not closed");
		}
	}

	private static boolean loadEntry(String line, int prefixLength, Type type, ConfigCategory config) throws IOException {
		int i = line.indexOf('=', prefixLength + 1);
		if (i < 0) {
			return false;
		}
		String name = line.substring(prefixLength, i).trim();
		String value = line.substring(i + 1).trim();
		config.putEntry(name, new ConfigValueEntry(type, value));
		return true;
	}

	private static boolean loadListEntry(ConfigReader reader, String line, int prefixLength, Type type, ConfigCategory config) throws IOException {
		if (!line.endsWith("<")) {
			return false;
		}
		String name = line.substring(prefixLength, line.length() - 1).trim();
		String[] value = readList(reader);
		config.putEntry(name, new ConfigListEntry(type, value));
		return true;
	}

	private static String[] readList(ConfigReader reader) {
		int startingLine = reader.lineNumber();
		List<String> list = new ArrayList<>();
		for (String line : reader) {
			if (line.equals(">")) {
				return list.toArray(new String[list.size()]);
			}
			list.add(line);
		}
		throw new ConfigLoadException("List starting at line " + startingLine + " not closed");
	}

}
