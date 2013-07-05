package universalelectricity.compatiblity;

public class Compatiblity
{

	/**
	 * Multiply this to convert foreign energy into UE Joules.
	 */
	public static double IC2_RATIO = 50;
	public static double BC3_RATIO = 125;
	/**
	 * Multiply this to convert UE Joules into foreign energy.
	 */
	public static double TO_IC2_RATIO = 1 / IC2_RATIO;
	public static double TO_BC_RATIO = 1 / BC3_RATIO;

}
