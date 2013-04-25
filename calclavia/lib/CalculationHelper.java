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
		double pitchRadians = Math.toRadians(pitch);

		double x = vector.x;
		double y = vector.y;
		double z = vector.z;

		if (yaw != 0)
		{
			vector.x = x * Math.cos(yawRadians) - z * Math.sin(yawRadians);
			vector.z = x * Math.sin(yawRadians) + z * Math.cos(yawRadians);
		}

		if (pitch != 0)
		{
			vector.y = x * Math.cos(pitchRadians) - y * Math.sin(pitchRadians);
		}
	}

	public static void rotateByAngle(Vector3 vector, double yaw, double pitch, double roll)
	{
		double yawRadians = Math.toRadians(yaw);
		double pitchRadians = Math.toRadians(pitch);
		double rollRadians = Math.toRadians(roll);

		double x = vector.x;
		double y = vector.y;
		double z = vector.z;

		// Rotate around the x axis
		double xy = Math.cos(pitchRadians) * y - Math.sin(pitchRadians) * z;
		double xz = Math.sin(pitchRadians) * y + Math.cos(pitchRadians) * z;

		// Rotation around the Y axis
		double yz = Math.cos(yawRadians) * xz - Math.sin(yawRadians) * x;
		double yx = Math.sin(yawRadians) * xz + Math.cos(yawRadians) * x;

		// Rotation around the Z axis
		double zx = Math.cos(rollRadians) * yx - Math.sin(rollRadians) * xy;
		double zy = Math.sin(rollRadians) * yx + Math.cos(rollRadians) * xy;

		vector.x = zx;
		vector.y = zy;
		vector.z = yz;
	}
}
