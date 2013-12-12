/**
 * 
 */
package universalelectricity.core;

import java.lang.reflect.Method;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.compatibility.Compatibility;

/**
 * @author Calclavia
 * 
 */
public class UniversalMethods
{
	/**
	 * Thermal Expansion Methods.
	 */
	public static int receiveEnergy(Class clazz, ForgeDirection from, int maxReceive, boolean simulate)
	{
		try
		{
			Method m = clazz.getMethod("receiveElectricity", ForgeDirection.class, Float.TYPE, Boolean.TYPE);
			return (int) ((float) (m.invoke(maxReceive * Compatibility.TE_RATIO, !simulate)) * Compatibility.TO_TE_RATIO);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	public static int extractEnergy(Class clazz, ForgeDirection from, int maxExtract, boolean simulate)
	{
		try
		{
			Method m = clazz.getMethod("provideElectricity", ForgeDirection.class, Float.TYPE, Boolean.TYPE);
			return (int) ((float) (m.invoke(maxExtract * Compatibility.TE_RATIO, !simulate)) * Compatibility.TO_TE_RATIO);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	public static boolean canInterface(Class clazz, ForgeDirection from)
	{
		try
		{
			Method m = clazz.getMethod("canConnect", ForgeDirection.class);
			return (boolean) (m.invoke(from));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Returns the amount of energy currently stored.
	 */
	public static int getEnergyStored(Class clazz, ForgeDirection from)
	{
		try
		{
			Method m = clazz.getMethod("getEnergyStored", ForgeDirection.class);
			return (int) ((float) (m.invoke(from)) * Compatibility.TO_TE_RATIO);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * Returns the maximum amount of energy that can be stored.
	 */
	public static int getMaxEnergyStored(Class clazz, ForgeDirection from)
	{
		try
		{
			Method m = clazz.getMethod("getMaxEnergyStored", ForgeDirection.class);
			return (int) ((float) (m.invoke(from)) * Compatibility.TO_TE_RATIO);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return 0;
	}
}
