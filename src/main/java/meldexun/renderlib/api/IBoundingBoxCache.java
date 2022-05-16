package meldexun.renderlib.api;

import meldexun.renderlib.util.MutableAABB;

public interface IBoundingBoxCache {

	void updateCachedBoundingBox(double partialTicks);

	MutableAABB getCachedBoundingBox();

}
