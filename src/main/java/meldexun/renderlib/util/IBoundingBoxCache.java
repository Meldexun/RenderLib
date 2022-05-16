package meldexun.renderlib.util;

public interface IBoundingBoxCache {

	void updateCachedBoundingBox(double partialTicks);

	MutableAABB getCachedBoundingBox();

}
