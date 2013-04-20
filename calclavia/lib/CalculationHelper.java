package calclavia.lib;

import universalelectricity.core.vector.Vector3;

public class CalculationHelper
{
	/**
	 * Rotates a point horizontally around the anchor 0,0 by a specific angle.
	 */
	public static void rotateXZByAngle(Vector3 vector, double angle)
	{
		double angleRadians = Math.toRadians(angle);

		double originalX = vector.x;
		double originalZ = vector.z;
		vector.x = originalX * Math.cos(angleRadians) - originalZ * Math.sin(angleRadians);
		vector.z = originalX * Math.sin(angleRadians) + originalZ * Math.cos(angleRadians);
	}

	/**
	 * Rotates a point vertically around the anchor 0,0 by a specific angle.
	 */
	public static void rotateYByAngle(Vector3 vector, int angle)
	{
		double angleRadians = Math.toRadians(angle);
		vector.y = Math.cos(angleRadians) * vector.y;
	}
}
