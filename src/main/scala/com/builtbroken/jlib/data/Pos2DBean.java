package com.builtbroken.jlib.data;

/** Most basic version of Pos that only contains the data. Useful for
 * just passing data into and out of storage.
 *
 * Created by robert on 1/11/2015.
 */
public class Pos2DBean implements IPos2D, Cloneable
{
    private final double x;
    private final double y;

    public Pos2DBean(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public double x()
    {
        return x;
    }

    public double xf()
    {
        return (float)x;
    }

    public double xi()
    {
        return (int)x;
    }

    @Override
    public double y()
    {
        return y;
    }

    public double yf()
    {
        return (float)y;
    }

    public double yi()
    {
        return (int)y;
    }

    @Override
    public Pos2DBean clone()
    {
        return new Pos2DBean(x(), y());
    }

    @Override
    public int hashCode()
    {
        long x = Double.doubleToLongBits(this.x);
        long y = Double.doubleToLongBits(this.y);
        return 31 * (int)(x ^ (x >>> 32)) + (int)(y ^ (y >>> 32));
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof IPos2D)
        {
            return this.x == ((IPos2D)o).x() && this.y == ((IPos2D)o).y();
        }
        return false;
    }

    public int compare(IPos2D pos)
    {
        if (x < pos.y() || y < pos.y())
            return -1;

        if (x > pos.y() || y > pos.y())
            return 1;

        return 0;
    }

    @Override
    public String toString()
    {
        return "Pos2D [" + this.x + "," + this.y + "]";
    }
}
