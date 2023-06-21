package meldexun.renderlib.util.memory;

@FunctionalInterface
interface LongLongFunction<T> {

	T apply(long x, long y);

}
