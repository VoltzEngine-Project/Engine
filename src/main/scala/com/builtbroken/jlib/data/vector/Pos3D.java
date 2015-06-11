package com.builtbroken.jlib.data.vector;

import com.builtbroken.mc.lib.helper.MathUtility;

import java.util.Random;

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

    public R addRandom(Random rand, double r)
    {
        double i = (rand.nextDouble() * r) - (rand.nextDouble() * r);
        double j = (rand.nextDouble() * r) - (rand.nextDouble() * r);
        double k = (rand.nextDouble() * r) - (rand.nextDouble() * r);
        return newPos(i + x(), j + y(), k + z());
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

    public R subtract(double x, double y, double z)
    {
        return add(-x, -y, -z);
    }

    public R sub(IPos3D other)
    {
        return sub(other.x(), other.y(), other.z());
    }

    public R subtract(IPos3D other)
    {
        return sub(other.x(), other.y(), other.z());
    }

    @Override
    public R sub(double a)
    {
        return sub(a, a, a);
    }

    public R subtract(double a)
    {
        return sub(a, a, a);
    }

    public double distance(IPos3D pos)
    {
        return sub(pos).magnitude();
    }

    public double distance(double x, double y, double z)
    {
        return sub(x, y, z).magnitude();
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

    public double dot(IPos3D other)
    {
        return dot(other.x(), other.y(), other.z());
    }

    public double dot(double x, double y, double z)
    {
        return x() * x + y() * y + z() * z;
    }

    public R cross(IPos3D other)
    {
        return newPos(other.x(), other.y(), other.z());
    }

    public R cross(double x, double y, double z)
    {
        return newPos(y() * z - z * y, z * x - x() * z, x() * y - y() * x);
    }

    //https://keithmaggio.wordpress.com/2011/02/15/math-magician-lerp-slerp-and-nlerp/
    public R lerp(IPos3D end, float percent)
    {
        return newPos(x() + percent * (end.x() - x()), y() + percent * (end.y() - y()), z() + percent * (end.z() - z()));
    }

    //TODO test before using as its been slightly butchered to fit the method calls of this class
    public R Slerp(IPos3D end, float percent)
    {
        // Dot product - the cosine of the angle between 2 vectors.
        double dot = MathUtility.dclamp(dot(end), -1.0f, 1.0f);

        // Acos(dot) returns the angle between start and end,
        // And multiplying that by percent returns the angle between
        // start and the final result.
        double theta = Math.acos(dot) * percent;
        R relativeVec = (R) (newPos((end.x() - x()) * dot, (end.y() - y()) * dot, (end.z() - z()) * dot).normalize());

        // The final result.
        double x = x() * Math.cos(theta) + relativeVec.x() * Math.sin(theta);
        double y = y() * Math.cos(theta) + relativeVec.y() * Math.sin(theta);
        double z = z() * Math.cos(theta) + relativeVec.z() * Math.sin(theta);

        return newPos(x, y, z);
    }

    public R nlerp(IPos3D end, float percent)
    {
        return (R) lerp(end, percent).normalize();
    }

    /**
     * Creates a new point that represents the max between the two points
     *
     * @return new Pos
     */
    public R max(IPos3D other)
    {
        return newPos(Math.max(x(), other.x()), Math.max(y(), other.y()), Math.max(z(), other.z()));
    }

    /**
     * Creates a new point that represents the min between the two points
     *
     * @return new Pos
     */
    public R min(IPos3D other)
    {
        return newPos(Math.min(x(), other.x()), Math.min(y(), other.y()), Math.max(z(), other.z()));
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

    public float zf()
    {
        return (float) z;
    }

    public int zi()
    {
        return (int) z;
    }

    /**
     * @return The perpendicular vector to the axis.
     */
    public R perpendicular()
    {
        if (this.z == 0.0F)
        {
            return this.zCross();
        }
        return this.xCross();
    }

    public R xCross()
    {
        return newPos(0.0D, this.z(), -this.y());
    }

    public R zCross()
    {
        return newPos(-this.y(), this.x(), 0.0D);
    }

    @Override
    public R clone()
    {
        return newPos(x(), y(), z());
    }

    @Override
    public R newPos(double x, double y)
    {
        return newPos(x, y, z);
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
        return (int) hash;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof IPos3D)
        {
            return ((IPos3D) o).x() == x() && ((IPos3D) o).y() == y() && ((IPos3D) o).z() == z();
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

    @Override
    public String toString()
    {
        return "Pos3D [" + this.x() + "," + this.y() + "," + this.z() + "]";
    }
}
