package universalelectricity;

import net.minecraft.server.MathHelper;


/**
 * The Class Vector2.
 */
public class Vector2
{
    
    /** The x. */
    public double x;
    
    /** The y. */
    public double y;

    /**
     * Instantiates a new vector2.
     */
    public Vector2()
    {
        this(0, 0);
    }

    /**
     * Instantiates a new vector2.
     *
     * @param i the i
     * @param j the j
     */
    public Vector2(int i, int j)
    {
        x = i;
        y = j;
    }

    /**
     * Instantiates a new vector2.
     *
     * @param d the d
     * @param d1 the d1
     */
    public Vector2(double d, double d1)
    {
        x = d;
        y = d1;
    }

    /**
     * Int x.
     *
     * @return the int
     */
    public int intX()
    {
        return (int)Math.floor(x);
    }

    /**
     * Int y.
     *
     * @return the int
     */
    public int intY()
    {
        return (int)Math.floor(y);
    }

    /**
     * Checks if is point in region.
     *
     * @param vector3 the vector3
     * @param vector3_1 the vector3_1
     * @param vector3_2 the vector3_2
     * @return true, if is point in region
     */
    public static boolean isPointInRegion(Vector2 vector2, Vector2 vector2_1, Vector2 vector2_2)
    {
        return vector2.x > vector2_1.x && vector2.x < vector2_2.x && vector2.y > vector2_1.y && vector2.y < vector2_2.y;
    }

    /**
     * Distance.
     *
     * @param vector2 the vector2
     * @param vector2_1 the vector2_1
     * @return the double
     */
    public static double distance(Vector2 vector2, Vector2 vector2_1)
    {
        double d = vector2.x - vector2_1.x;
        double d1 = vector2.y - vector2_1.y;
        return (double)MathHelper.sqrt(d * d + d1 * d1);
    }

    /**
     * Slope.
     *
     * @param vector2 the vector2
     * @param vector2_1 the vector2_1
     * @return the double
     */
    public static double slope(Vector2 vector2, Vector2 vector2_1)
    {
        double d = vector2.x - vector2_1.x;
        double d1 = vector2.y - vector2_1.y;
        return d1 / d;
    }

    /**
     * Adds the.
     *
     * @param vector2 the vector2
     */
    public void add(Vector2 vector2)
    {
        x += vector2.x;
        y += vector2.y;
    }

    /**
     * Adds the.
     *
     * @param d the d
     */
    public void add(double d)
    {
        x += d;
        y += d;
    }

    /**
     * Round.
     *
     * @return the vector2
     */
    public Vector2 round()
    {
        return new Vector2(Math.round(x), Math.round(y));
    }

    /**
     * Floor.
     *
     * @return the vector2
     */
    public Vector2 floor()
    {
        return new Vector2(Math.floor(x), Math.floor(y));
    }

    /**
     * Output.
     *
     * @return the string
     */
    public String output()
    {
        return (new StringBuilder()).append("Vector2: ").append(x).append(",").append(y).toString();
    }

    /**
     * Op.
     */
    public void op()
    {
        System.out.println(output());
    }
}
