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
		vector.x = vector.x * Math.cos(angleRadians) - vector.y * Math.sin(angleRadians);
		vector.y = vector.x * Math.sin(angleRadians) + vector.y * Math.cos(angleRadians);
	}

	/**
	 * Rotates a point vertically around the anchor 0,0 by a specific angle.
	 */
	public static void rotateYByAngle(Vector3 vector, int angle)
	{
		double angleRadians = Math.toRadians(angle);
		vector.y = Math.cos(angleRadians) * vector.y;
		// vector.x = vector.x * Math.cos(angleRadians) - vector.y * Math.sin(angleRadians);
		// vector.y = vector.x * Math.sin(angleRadians) + vector.y * Math.cos(angleRadians);
	}
}
