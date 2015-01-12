package com.builtbroken.jlib.data.vector;

/**
 * Created by robert on 1/11/2015.
 */
public abstract class Pos3D<R extends Pos3D> extends Pos2D<R>
{
    final double z;

    public Pos3D(double x, double y, double z)
    {
        super(x, y);
        this.z = z;
    }

    public Pos3D()
    {
        this(0, 0, 0);
    }

    public R add(double x, double y, double z)
    {
        return newPos(x + x(), y + y(), z + z());
    }

    public R add(IPos3D other)
    {
        return add(other.x(), other.y(), other.z());
    }

    @Override
    public R add(double a)
    {
        return add(a, a, a);
    }



    public R sub(double x, double y, double z)
    {
        return add(-x, -y, -z);
    }

    public R sub(IPos3D other)
    {
        return sub(other.x(), other.y(), other.z());
    }

    @Override
    public R sub(double a)
    {
        return sub(a, a, a);
    }



    public R multiply(IPos3D other)
    {
        return multiply(other.x(), other.y(), other.z());
    }

    public R multiply(double x, double y, double z)
    {
        return newPos(x * x(), y * y(), z * z());
    }

    @Override
    public R multiply(double a)
    {
        return multiply(a, a, a);
    }



    public R divide(IPos3D other)
    {
        return divide(other.x(), other.y(), other.z());
    }

    public R divide(double x, double y, double z)
    {
        return newPos(x() / x, y() / y, z() / z);
    }

    @Override
    public R divide(double a)
    {
        return divide(a, a, a);
    }

    public double dotProduct(IPos3D other)
    {
        return x() * other.x() + y() * other.y() + z() * other.z();
    }

    public R crossProduct(IPos3D other)
    {
        return newPos(y() * other.z() - z * other.y(), z * other.x() - x() * other.z(), x() * other.y() - y() * other.x());
    }

    public R midPoint(IPos3D pos)
    {
        return newPos((x() + pos.x()) / 2, (y() + pos.y()) / 2, (z + pos.z()) / 2);
    }

    @Override
    public boolean isZero()
    {
        return x() == 0 && y() == 0 && z() == 0;
    }

    @Override
    public double magnitudeSquared()
    {
        return x() * x() + y() * y() + z() * z();
    }

    public double z()
    {
        return z;
    }

    public double zf()
    {
        return (float) z;
    }

    public double zi()
    {
        return (int) z;
    }

    @Override
    public R clone()
    {
        return newPos(x(), y(), z());
    }

    @Override
    public R newPos(double x, double y)
    {
        return newPos(x, y , z);
    }

    public abstract R newPos(double x, double y, double z);

    @Override
    public int hashCode()
    {
        long x = Double.doubleToLongBits(this.x());
        long y = Double.doubleToLongBits(this.y());
        long z = Double.doubleToLongBits(this.z());
        long hash = (x ^ (x >>> 32));
        hash = 31 * hash + y ^ (y >>> 32);
        hash = 31 * hash + z ^ (z >>> 32);
        return (int)hash;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof IPos3D)
        {
            return ((IPos3D)o).x() == x() && ((IPos3D)o).y() == y() && ((IPos3D)o).z() == z();
        }
        return false;
    }

    public int compare(IPos3D that)
    {
        if (x() < that.x() || y() < that.y() || z < that.z())
            return -1;

        if (x() > that.x() || y() > that.y() || z > that.z())
            return 1;

        return 0;
    }
}
