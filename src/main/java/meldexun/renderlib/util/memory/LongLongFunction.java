package meldexun.renderlib.util.memory;

@FunctionalInterface
public interface LongLongFunction<T> {

	T apply(long x, long y);

}
