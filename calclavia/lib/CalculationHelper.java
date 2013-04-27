package calclavia.lib;

import universalelectricity.core.vector.Vector3;

/**
 * Some functions to help with mathematical calculations.
 * 
 * @author Calclavia
 * 
 */
public class CalculationHelper
{
	public static void rotateByAngle(Vector3 vector, double yaw)
	{
		double yawRadians = Math.toRadians(yaw);

		double x = vector.x;
		double z = vector.z;

		if (yaw != 0)
		{
			vector.x = x * Math.cos(yawRadians) - z * Math.sin(yawRadians);
			vector.z = x * Math.sin(yawRadians) + z * Math.cos(yawRadians);
		}
	}

	/**
	 * Rotates a point by a yaw and pitch around the anchor 0,0 by a specific angle.
	 */
	public static void rotateByAngle(Vector3 vector, double yaw, double pitch)
	{
		rotateByAngle(vector, yaw, pitch, 0);
	}

	public static void rotateByAngle(Vector3 vector, double yaw, double pitch, double roll)
	{
		double yawRadians = Math.toRadians(yaw);
		double pitchRadians = Math.toRadians(pitch);
		double rollRadians = Math.toRadians(roll);

		double x = vector.x;
		double y = vector.y;
		double z = vector.z;

		vector.x = x * Math.cos(yawRadians) * Math.cos(pitchRadians) + z * (Math.cos(yawRadians) * Math.sin(pitchRadians) * Math.sin(rollRadians) - Math.sin(yawRadians) * Math.cos(rollRadians)) + y * (Math.cos(yawRadians) * Math.sin(pitchRadians) * Math.cos(rollRadians) + Math.sin(yawRadians) * Math.sin(rollRadians));
		vector.z = x * Math.sin(yawRadians) * Math.cos(pitchRadians) + z * (Math.sin(yawRadians) * Math.sin(pitchRadians) * Math.sin(rollRadians) + Math.cos(yawRadians) * Math.cos(rollRadians)) + y * (Math.sin(yawRadians) * Math.sin(pitchRadians) * Math.cos(rollRadians) - Math.cos(yawRadians) * Math.sin(rollRadians));
		vector.y = -x * Math.sin(pitchRadians) + z * Math.cos(pitchRadians) * Math.sin(rollRadians) + y * Math.cos(pitchRadians) * Math.cos(rollRadians);
	}
}
