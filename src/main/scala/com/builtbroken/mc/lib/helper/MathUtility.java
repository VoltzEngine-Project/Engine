package com.builtbroken.mc.lib.helper;

import com.builtbroken.mc.data.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class MathUtility
{
    public static final Random rand = new Random();


    public static short randomShort()
    {
        return (short)(rand.nextInt(Short.MAX_VALUE * 2) - Short.MAX_VALUE);
    }

	/**
	 * Clamps the angles to a min max by adding or subtracting the min max. This way it maintanes
	 * the change in angle in the chance it goes out of bounds
	 */
	public static float clampAngle(float var, float min, float max)
	{
		while (var < min)
		{
			var += 360;
		}

		while (var > max)
		{
			var -= 360;
		}

		return var;
	}

	public static double clampAngle(double var, double min, float max)
	{
		while (var < min)
		{
			var += max;
		}
		while (var > max)
		{
			var -= max;
		}
		return var;
	}

	public static double clampAngleTo180(double var)
	{
		return MathUtility.clampAngle(var, -180, 180);
	}

    /** Gets the volume of a sphere
     *
     * @param radius - distance from center
     * @return exact volume
     */
    public static double getSphereVolume(double radius)
    {
        return (4 * Math.PI * (radius * radius * radius)) / 3;
    }


    /**
     * MC method that has been copies to remove the @SideOnly(Side.CLIENT)
     * @param p_154353_0_
     * @return
     */
    public static int func_154353_e(double p_154353_0_)
    {
        return (int)(p_154353_0_ >= 0.0D ? p_154353_0_ : -p_154353_0_ + 1.0D);
    }


	/**
	 * gets the facing direction using the yaw angle
	 */
	public static Direction getFacingDirectionFromAngle(float yaw)
	{
		float angle = MathHelper.wrapDegrees(yaw);
		if (angle >= -45 && angle <= 45)
		{
			return Direction.SOUTH;
		}
		else if (angle >= 45 && angle <= 135)
		{

			return Direction.WEST;
		}
		else if (angle >= 135 && angle <= -135)
		{

			return Direction.NORTH;
		}
		else
		{
			return Direction.EAST;
		}
	}

	/**
	 * gets the facing direction using the yaw angle
	 */
	public static Direction getFacingDirectionFromAngle(double yaw)
	{
		return getFacingDirectionFromAngle((float) yaw);
	}

}
