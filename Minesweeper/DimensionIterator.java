package Minesweeper;

import java.security.InvalidParameterException;
import java.util.Iterator;

/**
 * Iterates over an array representing any number of dimensions using a range selector lambda
 * Note: This only works for euclidean space, not hyperbolic
 * @author Patrick Kosmider
 */
public class DimensionIterator<D> implements Iterator<D> {
    D[] data;
    int[] dimensions;

    int[] indices;
    int[] starts;
    int[] ends;

    public DimensionIterator(D[] data, int[] dimensions, AxisRangeSelector axisRangeSelector, Object context){
        if (dimensions.length == 0)
            throw new InvalidParameterException("0-dimensional space not supported");

        int volume = 1;
        for (int i = 0; i < dimensions.length; i++){
            volume *= dimensions[i];
        }
        if (volume != data.length)
            throw new InvalidParameterException("Volume described by dimensions != amount of data");

        //Init
        this.data = data;
        this.dimensions = dimensions;

        indices = new int[dimensions.length];
        starts = new int[dimensions.length];
        ends = new int[dimensions.length];

        for (int a = 0; a < dimensions.length; a++){
            int length = dimensions[a];
            int[] range = axisRangeSelector.selectRange(a, length, context);
            indices[a] = range[0];
            starts[a] = range[0];
            ends[a] = range[1];
        }
    }

    @Override
    /**
     * Checks if there is another element in the iteration
     * @return true if there is another element, false otherwise
     */
    public boolean hasNext() {
        return indices[indices.length - 1] < ends[ends.length - 1];
    }

    @Override
    /**
     * Gets the next element in the iteration
     * @return the next element in the iteration
     */
    public D next() {
        D current = null;

        //Get the data at the current index
        int index = 0;
        int partialVolume = 1;
        for (int i = 0; i < dimensions.length; i++){
            index += indices[i] * partialVolume;
            partialVolume *= dimensions[i];
        }
        current = data[index];

        //Update the index
        for (int i = 0; i < indices.length; i++){
            indices[i] += 1;
            //Wrap, but avoid wrapping the last dimension so the iterator knows it's done
            if (i < indices.length - 1 && indices[i] >= ends[i])
                indices[i] = starts[i]; //reset the the dimension's starting bound
            else
                break;
        }

        return current;
    }

    //Test
    // public static void main(String[] args){
    //     Integer[] d = new Integer[9];
    //     for (int i = 0; i < d.length; i++) {
    //         d[i] = i;
    //     }
    //     int[] dim = {3,3};

    //     DimensionIterator<Integer> iter = new DimensionIterator<>(d, dim, (axis, length, context) -> {
    //         return new int[] {1, length};
    //     }, null);

    //     while (iter.hasNext()) {
    //         System.out.println(iter.next());
    //     }
    // }
}

interface AxisRangeSelector {
    /**
     * Used to select the range for each axis
     * @param axis The axis being decided for
     * @param length How long that dimension is
     * @param context Arbitrary context
     * @return A 2d array [low, high] representing [low, high)
     */
    int[] selectRange(int axis, int length, Object context);
}
