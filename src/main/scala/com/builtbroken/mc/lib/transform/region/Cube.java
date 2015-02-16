package com.builtbroken.mc.lib.transform.region;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.lib.transform.vector.Point;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by robert on 2/16/2015.
 */
public class Cube extends Shape3D implements Cloneable
{
    private IPos3D min;
    private IPos3D max;

    public Cube()
    {
        this(new Pos(), new Pos());
    }

    public Cube(IPos3D min, IPos3D max)
    {
        super((Pos) null);
        set(min, max);
    }

    public Cube(double x, double y, double z, double i, double j, double k)
    {
        this(new Pos(x, y, z), new Pos(i, j, k));
    }

    public Cube(AxisAlignedBB bb)
    {
        this(new Pos(bb.minX, bb.minY, bb.minZ), new Pos(bb.maxX, bb.maxY, bb.maxZ));
    }

    public Cube(NBTTagCompound nbt)
    {
        super(nbt);
        if (nbt.hasKey("min_pos"))
            min = new Pos(nbt.getCompoundTag("min_pos"));
        if (nbt.hasKey("max_pos"))
            max = new Pos(nbt.getCompoundTag("max_pos"));
    }

    //////////////////////
    ///Conversion methods
    ///////////////////////

    public AxisAlignedBB toAABB()
    {
        return isValid() ? AxisAlignedBB.getBoundingBox(min.x(), min.y(), min.z(), max.x(), max.y(), max.z()) : null;
    }

    public Rectangle toRectangle()
    {
        return isValid() ? new Rectangle(new Point(min), new Point(max)) : null;
    }


    //////////////////////
    ///Math methods
    /////////////////////

    public Cube add(IPos3D vec)
    {
        return add(vec.x(), vec.y(), vec.z());
    }

    public Cube add(double x, double y, double z)
    {
        if (isValid())
        {
            min = new Pos(min.x() + x, min.y() + y, min.z() + z);
            max = new Pos(max.x() + x, max.y() + y, max.z() + z);
            validate();
        }
        return this;
    }

    public Cube subtract(IPos3D vec)
    {
        return subtract(vec.x(), vec.y(), vec.z());
    }

    public Cube subtract(double x, double y, double z)
    {
        if (isValid())
        {
            min = new Pos(min.x() - x, min.y() - y, min.z() - z);
            max = new Pos(max.x() - x, max.y() - y, max.z() - z);
            validate();
        }
        return this;
    }

    //////////////////////
    /// Collision Detection
    /////////////////////
    @Override
    public boolean isWithin(double x, double y, double z)
    {
        return false;
    }

    public boolean intersects(Vec3 v)
    {
        return intersects(v.xCoord, v.yCoord, v.zCoord);
    }

    public boolean intersects(IPos3D v)
    {
        return intersects(v.x(), v.y(), v.z());
    }

    public boolean intersects(double x, double y, double z)
    {
        return isWithinX(x) && isWithinY(y) && isWithinZ(z);
    }

    public boolean doesOverlap(AxisAlignedBB box)
    {
        return doesOverlap(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public boolean doesOverlap(Cube box)
    {
        return box.isValid() && doesOverlap(box.min.x(), box.min.y(), box.min.z(), box.max.x(), box.max.y(), box.max.z());
    }

    public boolean doesOverlap(double x, double y, double z, double i, double j, double k)
    {
        return !isOutSideX(x, i) || !isOutSideY(y, j) || !isOutSideZ(z, k);
    }

    public boolean isOutSideX(double x, double i)
    {
        return (min.x() > x || i > max.x());
    }

    public boolean isOutSideY(double y, double j)
    {
        return (min.y() > y || j > max.y());
    }

    public boolean isOutSideZ(double z, double k)
    {
        return (min.z() > z || k > max.z());
    }

    public boolean isInsideBounds(double x, double y, double z, double i, double j, double k)
    {
        return isWithin(min.x(), max.x(), x, i) && isWithin(min.y(), max.y(), y, j) && isWithin(min.z(), max.z(), z, k);
    }

    public boolean isInsideBounds(Cube other)
    {
        return isInsideBounds(other.min.x(), other.min.y(), other.min.z(), other.max.x(), other.max.y(), other.max.z());
    }

    public boolean isInsideBounds(AxisAlignedBB other)
    {
        return isInsideBounds(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }

    public boolean isVecInYZ(Vec3 v)
    {
        return isWithinY(v) && isWithinZ(v);
    }

    public boolean isVecInYZ(IPos3D v)
    {
        return isWithinY(v) && isWithinZ(v);
    }

    public boolean isWithinXZ(Vec3 v)
    {
        return isWithinX(v) && isWithinZ(v);
    }

    public boolean isWithinXZ(IPos3D v)
    {
        return isWithinX(v) && isWithinZ(v);
    }

    public boolean isWithinX(double v)
    {
        return isWithinRange(min.x(), max.x(), v);
    }

    public boolean isWithinX(Vec3 v)
    {
        return isWithinX(v.xCoord);
    }

    public boolean isWithinX(IPos3D v)
    {
        return isWithinX(v.x());
    }

    public boolean isWithinY(double v)
    {
        return isWithinRange(min.y(), max.y(), v);
    }

    public boolean isWithinY(Vec3 v)
    {
        return isWithinY(v.yCoord);
    }

    public boolean isWithinY(IPos3D v)
    {
        return isWithinY(v.y());
    }

    public boolean isWithinZ(double v)
    {
        return isWithinRange(min.z(), max.z(), v);
    }

    public boolean isWithinZ(Vec3 v)
    {
        return isWithinZ(v.zCoord);
    }

    public boolean isWithinZ(IPos3D v)
    {
        return isWithinZ(v.z());
    }

    public boolean isWithinRange(double min, double max, double v)
    {
        return v >= min + 1E-5 && v <= max - 1E-5;
    }

    /**
     * Checks to see if a line segment is within the defined line. Assume the lines overlap each other.
     *
     * @param min - min point
     * @param max - max point
     * @param a   - min line point
     * @param b   - max line point
     * @return true if the line segment is within the bounds
     */
    public boolean isWithin(double min, double max, double a, double b)
    {
        return a + 1E-5 >= min && b - 1E-5 <= max;
    }

    public static IPos3D[] getCorners(Cube box)
    {
        IPos3D[] array = new IPos3D[8];
        if (box.isValid())
        {
            double l = box.max.x() - box.min.x();
            double w = box.max.z() - box.min.z();
            double h = box.max.y() - box.min.y();
            array[0] = new Pos(box.min.x(), box.min.y(), box.min.z());
            array[1] = new Pos(box.min.x(), box.min.y() + h, box.min.z());
            array[2] = new Pos(box.min.x(), box.min.y() + h, box.min.z() + w);
            array[3] = new Pos(box.min.x(), box.min.y(), box.min.z() + w);
            array[4] = new Pos(box.min.x() + l, box.min.y(), box.min.z());
            array[5] = new Pos(box.min.x() + l, box.min.y() + h, box.min.z());
            array[6] = new Pos(box.min.x() + l, box.min.y() + h, box.min.z() + w);
            array[7] = new Pos(box.min.x() + l, box.min.y(), box.min.z() + w);
        }
        return array;
    }

    public static IPos3D[] getCorners(AxisAlignedBB box)
    {
        return getCorners(new Cube(box));
    }


    //////////////////////
    ///Accessors
    /////////////////////

    public double radius()
    {
        double m = 0;
        if (getSizeX() > m)
            m = getSizeX();
        if (getSizeY() > m)
            m = getSizeY();
        if (getSizeZ() > m)
            m = getSizeZ();
        return m;
    }

    @Override
    public double getVolume()
    {
        return getSizeX() * getSizeY() * getSizeZ();
    }

    @Override
    public double getArea()
    {
        double area = 0;
        area += getSizeX() * getSizeZ() * 2;
        area += getSizeY() * getSizeZ() * 2;
        area += getSizeX() * getSizeY() * 2;
        return area;
    }

    @Override
    public double getSizeX()
    {
        if (isValid())
        {
            return max.x() - min.x();
        }
        return 0;
    }

    @Override
    public double getSizeY()
    {
        if (isValid())
        {
            return max.y() - min.y();
        }
        return 0;
    }

    @Override
    public double getSizeZ()
    {
        if (isValid())
        {
            return max.z() - min.z();
        }
        return 0;
    }

    public IPos3D min()
    {
        return min;
    }

    public IPos3D max()
    {
        return max;
    }

    public boolean isSquared()
    {
        return getSizeX() == getSizeY() && getSizeY() == getSizeZ();
    }

    public boolean isSquaredInt()
    {
        return (int) getSizeX() == (int) getSizeY() && (int) getSizeY() == (int) getSizeZ();
    }

    public double distance(Vec3 v)
    {
        return center.distance(v);
    }

    public double distance(IPos3D v)
    {
        return center.distance(v);
    }

    public double distance(Cube box)
    {
        return distance(box.center);
    }

    public double distance(AxisAlignedBB box)
    {
        return distance(new Cube(box));
    }

    public double distance(double xx, double yy, double zz)
    {
        if (this.isValid())
        {
            IPos3D center = this.center;
            double x = center.x() - xx;
            double y = center.y() - yy;
            double z = center.z() - zz;
            return Math.sqrt(x * x + y * y + z * z);
        }
        return 0;
    }

    /**
     * Returns all entities in this region.
     */
    public List<Entity> getEntities(World world)
    {
        return getEntities(world, Entity.class);
    }

    public List getEntities(World world, Class<? extends Entity> entityClass)
    {
        return world.getEntitiesWithinAABB(entityClass, toAABB());
    }


    //////////////////////
    ///Setters
    /////////////////////

    public void setMin(IPos3D pos)
    {
        this.min = pos;
        validate();
    }

    public void setMax(IPos3D pos)
    {
        this.max = pos;
        validate();
    }

    public void set(IPos3D min, IPos3D max)
    {
        this.min = min;
        this.max = max;
        validate();
    }

    public Cube set(Cube cube)
    {
        this.set(cube.min != null ? new Pos(cube.min()) : null, cube.max() != null ? new Pos(cube.max()) : null);
        return this;
    }

    public Cube setBlockBounds(Block block)
    {
        block.setBlockBounds((float) min.x(), (float) min.y(), (float) min.z(), (float) max.x(), (float) max.y(), (float) max.z());
        return this;
    }

    /**
     * Called after the cube's data has changed in order
     * to update any internal data.
     */
    protected void validate()
    {
        if (isValid())
            this.center = new Pos(min.x() + (getSizeX() / 2), min.y() + (getSizeY() / 2), min.z() + (getSizeZ() / 2));
        else
            this.center = null;
    }

    /**
     * Used to check if the cube is valid. Amounts to a few null checks
     */
    public boolean isValid()
    {
        return min != null && max != null;
    }

    @Override
    public Cube clone()
    {
        return new Cube(min, max);
    }

    @Override
    public String toString()
    {
        return "Cube[" + min + "  " + max + "]";
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof Cube)
        {
            return ((Cube) other).min == min && ((Cube) other).max == max;
        }
        return false;
    }
}
