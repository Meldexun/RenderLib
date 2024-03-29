package meldexun.renderlib.asm.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

class ConfigReader implements AutoCloseable, Iterable<String>, Iterator<String> {

	private final BufferedReader reader;
	private String nextLine;
	private int lineNumber = -1;

	public ConfigReader(BufferedReader reader) {
		this.reader = reader;
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	public String readLine() throws IOException {
		String l;
		while ((l = reader.readLine()) != null) {
			lineNumber++;
			l = l.trim();
			if (l.isEmpty()) {
				continue;
			}
			if (l.startsWith("#")) {
				continue;
			}
			return l;
		}
		return null;
	}

	public int lineNumber() {
		return lineNumber;
	}

	@Override
	public Iterator<String> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		if (nextLine != null) {
			return true;
		} else {
			try {
				nextLine = readLine();
				return (nextLine != null);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}

	@Override
	public String next() {
		if (nextLine != null || hasNext()) {
			String line = nextLine;
			nextLine = null;
			return line;
		} else {
			throw new NoSuchElementException();
		}
	}

}
