package net.imglib2.algorithm.region.localneighborhood;

import net.imglib2.RandomAccess;

public interface PeriodicLineNeighborhoodFactory< T >
{
	public Neighborhood< T > create( final long[] position, final long span, final int[] increments, final RandomAccess< T > sourceRandomAccess );
}
