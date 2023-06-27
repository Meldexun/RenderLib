package meldexun.renderlib.util;

import static it.unimi.dsi.fastutil.HashCommon.arraySize;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

@SuppressWarnings("serial")
class FunctionalObject2IntMap<K> extends Object2IntOpenHashMap<K> {

	public int computeIfAbsent(final K k, final java.util.function.ToIntFunction<? super K> mappingFunction) {
		java.util.Objects.requireNonNull(mappingFunction);
		final int pos = find(k);
		if (pos >= 0) return value[pos];
		final int newValue = mappingFunction.applyAsInt(k);
		insert(-pos - 1, k, newValue);
		return newValue;
	}

	private int find(final K k) {
		if (((k) == null)) return containsNullKey ? n : -(n + 1);
		K curr;
		final K[] key = this.key;
		int pos;
		// The starting point.
		if (((curr = key[pos = (it.unimi.dsi.fastutil.HashCommon.mix((k).hashCode())) & mask]) == null)) return -(pos + 1);
		if (((k).equals(curr))) return pos;
		// There's always an unused entry.
		while (true) {
			if (((curr = key[pos = (pos + 1) & mask]) == null)) return -(pos + 1);
			if (((k).equals(curr))) return pos;
		}
	}

	private void insert(final int pos, final K k, final int v) {
		if (pos == n) containsNullKey = true;
		key[pos] = k;
		value[pos] = v;
		if (size++ >= maxFill) rehash(arraySize(size + 1, f));
	}

}
