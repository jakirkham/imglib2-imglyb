package net.imglib2.python.view.reshape;

import net.imglib2.AbstractInterval;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.Point;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayCursor;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class ReshapeView< T > extends AbstractInterval implements RandomAccessibleInterval< T > {

	private final RandomAccessibleInterval<T> source;

	public ReshapeView( final RandomAccessibleInterval< T > source, final long... dim )
	{
		super( new FinalInterval( dim ) );
		this.source = minIsZero( source ) ? source : Views.offset( source, Intervals.minAsLongArray( source ) );
	}

	@Override
	public ReshapeRandomAccess< T > randomAccess()
	{
		return new ReshapeRandomAccess<>( source, this );
	}

	@Override
	public ReshapeRandomAccess< T > randomAccess( final Interval interval )
	{
		return randomAccess();
	}

	private static boolean minIsZero( final Interval dim )
	{
		for ( int d = 0; d < dim.numDimensions(); ++d )
			if ( dim.min( d ) != 0 )
				return false;
		return true;
	}

	public static void main( final String[] args )
	{
		final ArrayImg<DoubleType,DoubleArray> img = ArrayImgs.doubles( new double[] {
				1.0, 2.0, 3.0, 4.0, 5.0, 6.0
		}, 2, 3 );
		final ReshapeView< DoubleType > reshaped = new ReshapeView<>( Views.translate( img, 4, 5 ), 3, 2 );

		final ArrayCursor< DoubleType > c1 = img.cursor();
		final Cursor< DoubleType > c2 = Views.interval( reshaped, reshaped ).cursor();

		while( c1.hasNext() ) {
			c1.fwd();
			c2.fwd();
			System.out.println( new Point( c1 ) + " " + new Point( c2 ) + " " + c1.get() + " " + c2.get() );
		}

	}

}