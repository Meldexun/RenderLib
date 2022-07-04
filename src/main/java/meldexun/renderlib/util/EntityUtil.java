package meldexun.renderlib.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.minecraft.entity.Entity;

public class EntityUtil {

	public static Iterable<Entity> entityIterable(Iterable<Entity> iterable) {
		return () -> new Iterator<Entity>() {
			Iterator<Entity> iterator = iterable.iterator();
			Iterator<Entity> partIterator;

			@Override
			public boolean hasNext() {
				return (partIterator != null && partIterator.hasNext()) || iterator.hasNext();
			}

			@Override
			public Entity next() {
				if (partIterator != null) {
					if (partIterator.hasNext()) {
						return partIterator.next();
					}
					partIterator = null;
				}
				if (!iterator.hasNext()) {
					throw new NoSuchElementException();
				}
				Entity entity = iterator.next();
				Entity[] parts = entity.getParts();
				if (parts != null && parts.length > 0) {
					Iterator<Entity> partIterator1 = Arrays.stream(parts).filter(e -> e.addedToChunk).iterator();
					if (partIterator1.hasNext()) {
						partIterator = partIterator1;
					}
				}
				return entity;
			}
		};
	}

}
