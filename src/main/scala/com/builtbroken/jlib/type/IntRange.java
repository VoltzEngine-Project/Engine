package com.builtbroken.jlib.type;

/** Integer based range between two numbers. Mainly used for min max returns to cut down
 * on needing a separate method for both.
 * Created by robert on 2/14/2015.
 */
public class IntRange extends Pair<Integer, Integer>
{
    public IntRange(int min, int max)
    {
        //Sanity check to ensure min is always min and max is always max
        super((min < max ? min : max), (min > max ? min : max));
    }

    public int min()
    {
        return left();
    }

    public int max()
    {
        return right();
    }
}
