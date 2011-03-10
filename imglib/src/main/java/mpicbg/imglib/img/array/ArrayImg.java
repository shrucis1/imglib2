/**
 * Copyright (c) 2009--2010, Stephan Preibisch & Stephan Saalfeld
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.  Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution.  Neither the name of the Fiji project nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package mpicbg.imglib.img.array;

import mpicbg.imglib.Interval;
import mpicbg.imglib.IterableRealInterval;
import mpicbg.imglib.img.AbstractImgOutOfBoundsRandomAccess;
import mpicbg.imglib.img.AbstractNativeImg;
import mpicbg.imglib.img.Img;
import mpicbg.imglib.img.basictypeaccess.DataAccess;
import mpicbg.imglib.img.list.ListImg;
import mpicbg.imglib.outofbounds.OutOfBoundsFactory;
import mpicbg.imglib.type.NativeType;
import mpicbg.imglib.util.IntervalIndexer;

/**
 * This {@link Img} stores an image in a single linear array of basic
 * types.  By that, it provides the fastest possible access to data while
 * limiting the number of basic types stored to {@link Integer#MAX_VALUE}.
 * Keep in mind that this does not necessarily reflect the number of pixels,
 * because a pixel can be stored in less than or more than a basic type entry.
 * 
 * @param <T>
 * @param <A>
 *
 * @author Stephan Preibisch and Stephan Saalfeld <saalfeld@mpi-cbg.de>
 */
public class ArrayImg< T extends NativeType< T >, A extends DataAccess > extends AbstractNativeImg< T, A >
{
	final int[] steps, dim;
	
	// the DataAccess created by the ArrayContainerFactory
	final private A data;

	/**
	 * TODO check for the size of numPixels being < Integer.MAX_VALUE?
	 * TODO Type is suddenly not necessary anymore
	 * 
	 * @param factory
	 * @param data
	 * @param dim
	 * @param entitiesPerPixel
	 */
	public ArrayImg( final A data, final long[] dim, final int entitiesPerPixel )
	{
		super( dim, entitiesPerPixel );
		this.dim = new int[ n ];
		for ( int d = 0; d < n; ++d )
			this.dim[ d ] = ( int )dim[ d ];

		this.steps = new int[ n ];
		IntervalIndexer.createAllocationSteps( this.dim, this.steps );
		this.data = data;
	}

	@Override
	public A update( final Object o )
	{
		return data;
	}

	@Override
	public ArrayCursor< T > cursor() { return new ArrayCursor< T >( this ); }

	@Override
	public ArrayLocalizingCursor< T > localizingCursor() { return new ArrayLocalizingCursor< T >( this ); }

	@Override
	public ArrayRandomAccess< T > randomAccess() { return new ArrayRandomAccess< T >( this ); }

	@Override
	public AbstractImgOutOfBoundsRandomAccess<T> randomAccess( final OutOfBoundsFactory<T, Img< T >> factory )
	{
		return new AbstractImgOutOfBoundsRandomAccess< T >( this, factory );
	}

	@Override
	public boolean equalIterationOrder( final IterableRealInterval< ? > f )
	{
		if ( f.numDimensions() != this.numDimensions() )
			return false;
		
		if ( getClass().isInstance( f ) || ListImg.class.isInstance( f ) )
		{
			final Interval a = ( Interval )f;
			for ( int d = 0; d < n; ++d )
				if ( dimension[ d ] != a.dimension( d ) )
					return false;
			
			return true;
		}
		
		return false;
	}

	@Override
	public ArrayImgFactory<T> factory() { return new ArrayImgFactory<T>(); }
}
