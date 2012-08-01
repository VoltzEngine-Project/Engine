package net.minecraft.src.universalelectricity;

import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3D;

/**
 * Vector3 Class is used for defining objects in a 3D space. Vector3 makes it easier to handle the coordinates of objects. Instead of
 * fumbling with x, y and z variables, all x, y and z variables are stored in one class. Vector3.x, Vector3.y, Vector3.z.
 * @author Calclavia
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

    /**
     * Returns the coordinates as integers
     */
    public int intX()
    {
        return (int)Math.floor(this.x);
    }
    public int intY()
    {
        return (int)Math.floor(this.y);
    }
    public int intZ()
    {
        return (int)Math.floor(this.z);
    }

    /**
     * Converts a TileEntity's position into Vector3
     */
    public static Vector3 get(Entity entity)
    {
        return new Vector3(entity.posX, entity.posY, entity.posZ);
    }

    /**
     * Converts an entity's position into Vector3
     */
    public static Vector3 get(TileEntity entity)
    {
        return new Vector3(entity.xCoord, entity.yCoord, entity.zCoord);
    }

    /**
     * Converts from Vec3D into a Vector3
     */
    public static Vector3 get(Vec3D par1)
    {
        return new Vector3(par1.xCoord, par1.yCoord, par1.zCoord);
    }

    /**
     * Converts this Vector3 into a Vector2 by dropping the Y axis.
     */
    public Vector2 toVector2()
    {
        return new Vector2(this.x, this.z);
    }

    /**
     * Converts this vector three into a Minecraft Vec3D object
     */
    public Vec3D toVec3D()
    {
        return Vec3D.createVector(this.x, this.y, this.z);
    }

    /**
     * Checks if a Vector3 point is located inside a region
     */
    public static boolean isPointInRegion(Vector3 point, Vector3 minPoint, Vector3 maxPoint)
    {
        return (point.x > minPoint.x && point.x < maxPoint.x) && (point.y > minPoint.y && point.y < maxPoint.y) && (point.z > minPoint.z && point.z < maxPoint.z);
    }

    /**
     * Compares two vectors and see if they are equal. True if so.
     */
    public boolean isEqual(Vector3 vector3)
    {
        return (this.x == vector3.x && this.y == vector3.y && this.z == vector3.z);
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
        tempVector.x = par1NBTTagCompound.getDouble(prefix + "X");
        tempVector.y = par1NBTTagCompound.getDouble(prefix + "Y");
        tempVector.z = par1NBTTagCompound.getDouble(prefix + "Z");
        return tempVector;
    }

    /**
     * Saves this Vector3 to disk
     * @param prefix - The prefix of this save. Use some unique string.
     * @param par1NBTTagCompound - The NBT compound object to save the data in
     */
    public void writeToNBT(String prefix, NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setDouble(prefix + "X", this.x);
        par1NBTTagCompound.setDouble(prefix + "Y", this.y);
        par1NBTTagCompound.setDouble(prefix + "Z", this.z);
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

    /**
	 * Gets a position relative to another position's side
	 * @param position - The position
	 * @param side - The side. 0-5
	 * @return The position relative to the original position's side
	 */
	public void modifyPositionFromSide(byte side)
	{
	    switch (side)
	    {
	        case 0: this.y -= 1; break;
	        case 1: this.y += 1; break;
	        case 2: this.z += 1; break;
	        case 3: this.z -= 1; break;
	        case 4: this.x += 1; break;
	        case 5: this.x -= 1; break;
	    }	
	}

	/**
	 * Finds the side of a block depending on it's facing direction from the given side.
	 * The side numbers are compatible with the function"getBlockTextureFromSideAndMetadata".
	 *
	 *  Bottom: 0;
	 *  Top: 1;
	 *	Back: 2;
	 *	Front: 3;
	 *	Left: 4;
	 *	Right: 5;
	 * @param front - The direction in which this block is facing/front. Use a number between 0 and 5. Default is 3.
	 * @param side - The side you are trying to find. A number between 0 and 5.
	 * @return The side relative to the facing direction.
	 */
	
	public static byte getOrientationFromSide(byte front, byte side)
	{
	    switch (front)
	    {
	        case 0:
	            switch (side)
	            {
	                case 0: return 3;
	                case 1: return 4;
	                case 2: return 1;
	                case 3: return 0;
	                case 4: return 4;
	                case 5: return 5;
	            }
	
	        case 1:
	            switch (side)
	            {
	                case 0: return 4;
	                case 1: return 3;
	                case 2: return 0;
	                case 3: return 1;
	                case 4: return 4;
	                case 5: return 5;
	            }
	
	        case 2:
	            switch (side)
	            {
	                case 0: return 0;
	                case 1: return 1;
	                case 2: return 3;
	                case 3: return 2;
	                case 4: return 5;
	                case 5: return 4;
	            }
	
	        case 3:
	            return side;
	
	        case 4:
	            switch (side)
	            {
	                case 0: return 0;
	                case 1: return 1;
	                case 2: return 5;
	                case 3: return 4;
	                case 4: return 3;
	                case 5: return 2;
	            }
	
	        case 5:
	            switch (side)
	            {
	                case 0: return 0;
	                case 1: return 1;
	                case 2: return 4;
	                case 3: return 5;
	                case 4: return 2;
	                case 5: return 3;
	            }
	    }
	
	    return -1;
	}
}