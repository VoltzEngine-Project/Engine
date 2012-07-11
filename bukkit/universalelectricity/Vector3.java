package universalelectricity;

import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Vec3D;


/**
 * The Class Vector3.
 */
public class Vector3 extends Vector2
{
    
    /** The z. */
    public double z;
    
    /** The Constant side. */
    public static final Vector3 side[] =
    {
        new Vector3(0, 1, 0), new Vector3(0, -1, 0), new Vector3(0, 0, -1), new Vector3(0, 0, 1), new Vector3(1, 0, 0), new Vector3(-1, 0, 0)
    };

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
     * @param i the i
     * @param j the j
     * @param k the k
     */
    public Vector3(int i, int j, int k)
    {
        x = i;
        y = j;
        z = k;
    }

    /**
     * Instantiates a new vector3.
     *
     * @param d the d
     * @param d1 the d1
     * @param d2 the d2
     */
    public Vector3(double d, double d1, double d2)
    {
        x = d;
        y = d1;
        z = d2;
    }

    /* (non-Javadoc)
     * @see universalelectricity.Vector2#intX()
     */
    public int intX()
    {
        return (int)Math.floor(x);
    }

    /* (non-Javadoc)
     * @see universalelectricity.Vector2#intY()
     */
    public int intY()
    {
        return (int)Math.floor(y);
    }

    /**
     * Int z.
     *
     * @return the int
     */
    public int intZ()
    {
        return (int)Math.floor(z);
    }

    /**
     * Checks if is point in region.
     *
     * @param vector3 the vector3
     * @param vector3_1 the vector3_1
     * @param vector3_2 the vector3_2
     * @return true, if is point in region
     */
    public static boolean isPointInRegion(Vector3 vector3, Vector3 vector3_1, Vector3 vector3_2)
    {
        return vector3.x > vector3_1.x && vector3.x < vector3_2.x && vector3.y > vector3_1.y && vector3.y < vector3_2.y && vector3.z > vector3_1.z && vector3.z < vector3_2.z;
    }

    /**
     * Convert.
     *
     * @param vec3d the vec3d
     * @return the vector3
     */
    public static Vector3 convert(Vec3D vec3d)
    {
        return new Vector3(vec3d.a, vec3d.b, vec3d.c);
    }

    /**
     * Checks if is equal.
     *
     * @param vector3 the vector3
     * @return true, if is equal
     */
    public boolean isEqual(Vector3 vector3)
    {
        return x == vector3.x && y == vector3.y && z == vector3.z;
    }

    /**
     * To vector2.
     *
     * @return the vector2
     */
    public Vector2 toVector2()
    {
        return new Vector2(x, z);
    }

    /**
     * To vec3 d.
     *
     * @return the vec3 d
     */
    public Vec3D toVec3D()
    {
        return Vec3D.create(x, y, z);
    }

    /**
     * Distance.
     *
     * @param vector3 the vector3
     * @param vector3_1 the vector3_1
     * @return the double
     */
    public static double distance(Vector3 vector3, Vector3 vector3_1)
    {
        double d = vector3.x - vector3_1.x;
        double d1 = vector3.y - vector3_1.y;
        double d2 = vector3.z - vector3_1.z;
        return (double)MathHelper.sqrt(d * d + d1 * d1 + d2 * d2);
    }

    /**
     * Distance to.
     *
     * @param vector3 the vector3
     * @return the double
     */
    public double distanceTo(Vector3 vector3)
    {
        double d = vector3.x - x;
        double d1 = vector3.y - y;
        double d2 = vector3.z - z;
        return (double)MathHelper.sqrt(d * d + d1 * d1 + d2 * d2);
    }

    /**
     * Subtract.
     *
     * @param vector3 the vector3
     * @param vector3_1 the vector3_1
     * @return the vector3
     */
    public static Vector3 subtract(Vector3 vector3, Vector3 vector3_1)
    {
        return new Vector3(vector3.x - vector3_1.x, vector3.y - vector3_1.y, vector3.z - vector3_1.z);
    }

    /**
     * Adds the.
     *
     * @param vector3 the vector3
     * @param vector3_1 the vector3_1
     * @return the vector3
     */
    public static Vector3 add(Vector3 vector3, Vector3 vector3_1)
    {
        return new Vector3(vector3.x + vector3_1.x, vector3.y + vector3_1.y, vector3.z + vector3_1.z);
    }

    /**
     * Adds the.
     *
     * @param vector3 the vector3
     * @param d the d
     * @return the vector3
     */
    public static Vector3 add(Vector3 vector3, double d)
    {
        return new Vector3(vector3.x + d, vector3.y + d, vector3.z + d);
    }

    /**
     * Adds the.
     *
     * @param vector3 the vector3
     */
    public void add(Vector3 vector3)
    {
        x += vector3.x;
        y += vector3.y;
        z += vector3.z;
    }

    /* (non-Javadoc)
     * @see universalelectricity.Vector2#add(double)
     */
    public void add(double d)
    {
        x += d;
        y += d;
        z += d;
    }

    /**
     * Multiply.
     *
     * @param vector3 the vector3
     * @param vector3_1 the vector3_1
     * @return the vector3
     */
    public static Vector3 multiply(Vector3 vector3, Vector3 vector3_1)
    {
        return new Vector3(vector3.x * vector3_1.x, vector3.y * vector3_1.y, vector3.z * vector3_1.z);
    }

    /**
     * Multiply.
     *
     * @param vector3 the vector3
     * @param d the d
     * @return the vector3
     */
    public static Vector3 multiply(Vector3 vector3, double d)
    {
        return new Vector3(vector3.x * d, vector3.y * d, vector3.z * d);
    }

    /**
     * Read from nbt.
     *
     * @param s the s
     * @param nbttagcompound the nbttagcompound
     * @return the vector3
     */
    public static Vector3 readFromNBT(String s, NBTTagCompound nbttagcompound)
    {
        Vector3 vector3 = new Vector3();
        vector3.x = nbttagcompound.getDouble((new StringBuilder()).append(s).append("X").toString());
        vector3.y = nbttagcompound.getDouble((new StringBuilder()).append(s).append("Y").toString());
        vector3.z = nbttagcompound.getDouble((new StringBuilder()).append(s).append("Z").toString());
        return vector3;
    }

    /**
     * Write to nbt.
     *
     * @param s the s
     * @param nbttagcompound the nbttagcompound
     */
    public void writeToNBT(String s, NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setDouble((new StringBuilder()).append(s).append("X").toString(), x);
        nbttagcompound.setDouble((new StringBuilder()).append(s).append("Y").toString(), y);
        nbttagcompound.setDouble((new StringBuilder()).append(s).append("Z").toString(), z);
    }

    /* (non-Javadoc)
     * @see universalelectricity.Vector2#round()
     */
    public Vector3 round()
    {
        return new Vector3(Math.round(x), Math.round(y), Math.round(z));
    }

    /* (non-Javadoc)
     * @see universalelectricity.Vector2#floor()
     */
    public Vector3 floor()
    {
        return new Vector3(Math.floor(x), Math.floor(y), Math.floor(z));
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
    public String output()
    {
        return (new StringBuilder()).append("Vector3: ").append(x).append(",").append(y).append(",").append(z).toString();
    }
}
