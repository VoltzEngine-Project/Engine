package calclavia.lib;

import universalelectricity.core.vector.Vector3;

public class CalculationHelper
{
	/**
	 * Rotates a point by a yaw and pitch around the anchor 0,0 by a specific angle.
	 */
	public static void rotateByAngle(Vector3 vector, double yaw, double pitch)
	{
		double yawRadians = Math.toRadians(yaw);
		double pitchRadians = Math.toRadians(yaw);

		double originalX = vector.x;
		double originalY = vector.y;
		double originalZ = vector.z;

		vector.x = originalX * Math.cos(yawRadians) - originalZ * Math.sin(yawRadians);
		vector.z = originalX * Math.sin(yawRadians) + originalZ * Math.cos(yawRadians);
		vector.y = originalX * Math.cos(pitchRadians) - originalY * Math.sin(pitchRadians);
	}
}
