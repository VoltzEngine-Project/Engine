package net.minecraft.src.universalelectricity;

import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Vec3D;

/**
 * Vector3 Class is used for defining objects in a 3D space. Vector3 makes it easier to handle the coordinates of objects. Instead of
 * fumbling with x, y and z variables, all x, y and z variables are stored in one class. Vector3.x, Vector3.y, Vector3.z.
 *
 * Constructor: int x, int y, int z
 * Constructor: () - default would set x, y and z to 0.
 * @author Henry
 */

public class Vector3 extends Vector2
{
    public double z;

    public static final Vector3[] side = {new Vector3(0, 1, 0), new Vector3(0, -1, 0), new Vector3(0, 0, -1), new Vector3(0, 0, 1), new Vector3(1, 0, 0), new Vector3(-1, 0, 0)};

    public Vector3()
    {
        this(0, 0, 0);
    }

    public Vector3(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    //Returns the values as an int
    public int intX() { return (int)Math.floor(this.x); }
    public int intY() { return (int)Math.floor(this.y); }
    public int intZ() { return (int)Math.floor(this.z); }

    public static boolean isPointInRegion(Vector3 point, Vector3 minPoint, Vector3 maxPoint)
    {
        return (point.x > minPoint.x && point.x < maxPoint.x) && (point.y > minPoint.y && point.y < maxPoint.y) && (point.z > minPoint.z && point.z < maxPoint.z);
    }
    
    /**
     * Converts from Vec3D into a Vector3
     */
    public static Vector3 convert(Vec3D par1)
    {
        return new Vector3(par1.xCoord, par1.yCoord, par1.zCoord);
    }
    
    /**
     * Compares two vectors and see if they are equal
     * @param vector3 - The vector3 you are comparing with
     * @return True if it is equal
     */
    public boolean isEqual(Vector3 vector3)
    {
    	return (this.x == vector3.x && this.y == vector3.y && this.z == vector3.z);
    }
    
    /**
     * Conver the vector3 into a vector2
     * @param par1
     * @return
     */
    public Vector2 toVector2()
    {
        return new Vector2(this.x, this.z);
    }

    /**
     * Converts this vector three into a Minecraft Vec3D object
     * @return
     */
    public Vec3D toVec3D()
    {
        return Vec3D.createVector(this.x, this.y, this.z);
    }

    /**
     * Gets the distance between two vectors
     * @return The distance
     */
    public static double distance(Vector3 par1, Vector3 par2)
    {
        double var2 = par1.x - par2.x;
        double var4 = par1.y - par2.y;
        double var6 = par1.z - par2.z;
        return MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
    }
    
    public double distanceTo(Vector3 vector3)
    {
        double var2 = vector3.x - this.x;
        double var4 = vector3.y - this.y;
        double var6 = vector3.z - this.z;
        return MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
    }
    
    public static Vector3 subtract(Vector3 par1, Vector3 par2)
    {
        return new Vector3(par1.x - par2.x, par1.y - par2.y, par1.z - par2.z);
    }
    
    public static Vector3 add(Vector3 par1, Vector3 par2)
    {
        return new Vector3(par1.x + par2.x, par1.y + par2.y, par1.z + par2.z);
    }
    
    public static Vector3 add(Vector3 par1, double par2)
    {
        return new Vector3(par1.x + par2, par1.y + par2, par1.z + par2);
    }
    
    public void add(Vector3 par1)
    {
        this.x += par1.x;
        this.y += par1.y;
        this.z += par1.z;
    }
    
    @Override
	public void add(double par1)
    {
        this.x += par1;
        this.y += par1;
        this.z += par1;
    }
    
    public static Vector3 multiply(Vector3 par1, Vector3 par2)
    {
        return new Vector3(par1.x * par2.x, par1.y * par2.y, par1.z * par2.z);
    }
    
    public static Vector3 multiply(Vector3 par1, double par2)
    {
        return new Vector3(par1.x * par2, par1.y * par2, par1.z * par2);
    }
    
    
    public static Vector3 readFromNBT(String prefix, NBTTagCompound par1NBTTagCompound)
    {
    	Vector3 tempVector = new Vector3();
    	tempVector.x = par1NBTTagCompound.getDouble(prefix+"X");
    	tempVector.y = par1NBTTagCompound.getDouble(prefix+"Y");
    	tempVector.z = par1NBTTagCompound.getDouble(prefix+"Z");
    	
    	return tempVector;
    }
    
    /**
     * Saves this Vector3 to disk
     * @param prefix - The prefix of this save. Use some unique string.
     * @param par1NBTTagCompound - The NBT compound object to save the data in
     */
    public void writeToNBT(String prefix, NBTTagCompound par1NBTTagCompound)
    {
    	par1NBTTagCompound.setDouble(prefix+"X", this.x);
    	par1NBTTagCompound.setDouble(prefix+"Y", this.y);
    	par1NBTTagCompound.setDouble(prefix+"Z", this.z);
    }
    
    @Override
	public Vector3 round()
    {
        return new Vector3(Math.round(this.x), Math.round(this.y), Math.round(this.z));
    }
    @Override
	public Vector3 floor()
    {
        return new Vector3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }
    public Vector3 top()
    {
        return add(this, side[0]);
    }
    public Vector3 bottom()
    {
        return add(this, side[1]);
    }
    public Vector3 front()
    {
        return add(this, side[2]);
    }
    public Vector3 back()
    {
        return add(this, side[3]);
    }
    public Vector3 left()
    {
        return add(this, side[4]);
    }
    public Vector3 right()
    {
        return add(this, side[5]);
    }
    @Override
	public String output()
    {
        return "Vector3: " + this.x + "," + this.y + "," + this.z;
    }
}