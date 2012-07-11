package universalelectricity;

import net.minecraft.server.Entity;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;


/**
 * The Class Vector3.
 */
public class Vector3 extends Vector2
{
    
    /** The z. */
    public double z;

    /** The Constant side. */
    public static final Vector3[] side = {new Vector3(0, 1, 0), new Vector3(0, -1, 0), new Vector3(0, 0, -1), new Vector3(0, 0, 1), new Vector3(1, 0, 0), new Vector3(-1, 0, 0)};

    /**
     * Instantiates a new vector3.
     */
    public Vector3()
    {
        this(0, 0, 0);
    }

    /**
     * Instantiates a new vector3.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public Vector3(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Instantiates a new vector3.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public Vector3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Returns the coordinates as integers.
     *
     * @return the int
     */
    public int intX() { return (int)Math.floor(this.x); }
    
    /* (non-Javadoc)
     * @see universalelectricity.Vector2#intY()
     */
    public int intY() { return (int)Math.floor(this.y); }
    
    /**
     * Int z.
     *
     * @return the int
     */
    public int intZ() { return (int)Math.floor(this.z); }

    /**
     * Converts a TileEntity's position into Vector3.
     *
     * @param entity the entity
     * @return the vector3
     */
    public static Vector3 get(Entity entity)
    {
    	return new Vector3(entity.locX, entity.locY, entity.locZ);
    }
    
    /**
     * Converts an entity's position into Vector3.
     *
     * @param entity the entity
     * @return the vector3
     */
    public static Vector3 get(TileEntity entity)
    {
    	return new Vector3(entity.x, entity.y, entity.z);
    }
    
    /**
     * Converts from Vec3D into a Vector3.
     *
     * @param par1 the par1
     * @return the vector3
     */
    public static Vector3 get(Vec3D par1)
    {
        return new Vector3(par1.a, par1.b, par1.c);
    }

    /**
     * Converts this Vector3 into a Vector2 by dropping the Y axis.
     *
     * @return the vector2
     */
    public Vector2 toVector2()
    {
        return new Vector2(this.x, this.z);
    }

    /**
     * Converts this vector three into a Minecraft Vec3D object.
     *
     * @return the vec3 d
     */
    public Vec3D toVec3D()
    {
        return Vec3D.a(this.x, this.y, this.z);
    }
    
    /**
     * Checks if a Vector3 point is located inside a region.
     *
     * @param point the point
     * @param minPoint the min point
     * @param maxPoint the max point
     * @return true, if is point in region
     */
    public static boolean isPointInRegion(Vector3 point, Vector3 minPoint, Vector3 maxPoint)
    {
        return (point.x > minPoint.x && point.x < maxPoint.x) && (point.y > minPoint.y && point.y < maxPoint.y) && (point.z > minPoint.z && point.z < maxPoint.z);
    }
    
    /**
     * Compares two vectors and see if they are equal. True if so.
     *
     * @param vector3 the vector3
     * @return true, if is equal
     */
    public boolean isEqual(Vector3 vector3)
    {
    	return (this.x == vector3.x && this.y == vector3.y && this.z == vector3.z);
    }
   

    /**
     * Gets the distance between two vectors.
     *
     * @param par1 the par1
     * @param par2 the par2
     * @return The distance
     */
    public static double distance(Vector3 par1, Vector3 par2)
    {
        double var2 = par1.x - par2.x;
        double var4 = par1.y - par2.y;
        double var6 = par1.z - par2.z;
        return MathHelper.sqrt(var2 * var2 + var4 * var4 + var6 * var6);
    }
    
    /**
     * Distance to.
     *
     * @param vector3 the vector3
     * @return the double
     */
    public double distanceTo(Vector3 vector3)
    {
        double var2 = vector3.x - this.x;
        double var4 = vector3.y - this.y;
        double var6 = vector3.z - this.z;
        return MathHelper.sqrt(var2 * var2 + var4 * var4 + var6 * var6);
    }
    
    /**
     * Subtract.
     *
     * @param par1 the par1
     * @param par2 the par2
     * @return the vector3
     */
    public static Vector3 subtract(Vector3 par1, Vector3 par2)
    {
        return new Vector3(par1.x - par2.x, par1.y - par2.y, par1.z - par2.z);
    }
    
    /**
     * Adds the.
     *
     * @param par1 the par1
     * @param par2 the par2
     * @return the vector3
     */
    public static Vector3 add(Vector3 par1, Vector3 par2)
    {
        return new Vector3(par1.x + par2.x, par1.y + par2.y, par1.z + par2.z);
    }
    
    /**
     * Adds the.
     *
     * @param par1 the par1
     * @param par2 the par2
     * @return the vector3
     */
    public static Vector3 add(Vector3 par1, double par2)
    {
        return new Vector3(par1.x + par2, par1.y + par2, par1.z + par2);
    }
    
    /**
     * Adds the.
     *
     * @param par1 the par1
     */
    public void add(Vector3 par1)
    {
        this.x += par1.x;
        this.y += par1.y;
        this.z += par1.z;
    }
    
    /* (non-Javadoc)
     * @see universalelectricity.Vector2#add(double)
     */
    @Override
	public void add(double par1)
    {
        this.x += par1;
        this.y += par1;
        this.z += par1;
    }
    
    /**
     * Multiply.
     *
     * @param par1 the par1
     * @param par2 the par2
     * @return the vector3
     */
    public static Vector3 multiply(Vector3 par1, Vector3 par2)
    {
        return new Vector3(par1.x * par2.x, par1.y * par2.y, par1.z * par2.z);
    }
    
    /**
     * Multiply.
     *
     * @param par1 the par1
     * @param par2 the par2
     * @return the vector3
     */
    public static Vector3 multiply(Vector3 par1, double par2)
    {
        return new Vector3(par1.x * par2, par1.y * par2, par1.z * par2);
    }
    
    
    /**
     * Read from nbt.
     *
     * @param prefix the prefix
     * @param par1NBTTagCompound the par1 nbt tag compound
     * @return the vector3
     */
    public static Vector3 readFromNBT(String prefix, NBTTagCompound par1NBTTagCompound)
    {
    	Vector3 tempVector = new Vector3();
    	tempVector.x = par1NBTTagCompound.getDouble(prefix+"X");
    	tempVector.y = par1NBTTagCompound.getDouble(prefix+"Y");
    	tempVector.z = par1NBTTagCompound.getDouble(prefix+"Z");
    	
    	return tempVector;
    }
    
    /**
     * Saves this Vector3 to disk.
     *
     * @param prefix - The prefix of this save. Use some unique string.
     * @param par1NBTTagCompound - The NBT compound object to save the data in
     */
    public void writeToNBT(String prefix, NBTTagCompound par1NBTTagCompound)
    {
    	par1NBTTagCompound.setDouble(prefix+"X", this.x);
    	par1NBTTagCompound.setDouble(prefix+"Y", this.y);
    	par1NBTTagCompound.setDouble(prefix+"Z", this.z);
    }
    
    /* (non-Javadoc)
     * @see universalelectricity.Vector2#round()
     */
    @Override
	public Vector3 round()
    {
        return new Vector3(Math.round(this.x), Math.round(this.y), Math.round(this.z));
    }
    
    /* (non-Javadoc)
     * @see universalelectricity.Vector2#floor()
     */
    @Override
	public Vector3 floor()
    {
        return new Vector3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }
    
    /**
     * Top.
     *
     * @return the vector3
     */
    public Vector3 top()
    {
        return add(this, side[0]);
    }
    
    /**
     * Bottom.
     *
     * @return the vector3
     */
    public Vector3 bottom()
    {
        return add(this, side[1]);
    }
    
    /**
     * Front.
     *
     * @return the vector3
     */
    public Vector3 front()
    {
        return add(this, side[2]);
    }
    
    /**
     * Back.
     *
     * @return the vector3
     */
    public Vector3 back()
    {
        return add(this, side[3]);
    }
    
    /**
     * Left.
     *
     * @return the vector3
     */
    public Vector3 left()
    {
        return add(this, side[4]);
    }
    
    /**
     * Right.
     *
     * @return the vector3
     */
    public Vector3 right()
    {
        return add(this, side[5]);
    }
    
    /* (non-Javadoc)
     * @see universalelectricity.Vector2#output()
     */
    @Override
	public String output()
    {
        return "Vector3: " + this.x + "," + this.y + "," + this.z;
    }
}