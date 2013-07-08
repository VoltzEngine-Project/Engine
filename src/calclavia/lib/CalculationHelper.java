package calclavia.lib;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
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

	/**
	 * Gets the delta look position based on the rotation yaw and pitch. Minecraft coordinates are
	 * messed up. Y and Z are flipped. Yaw is displaced by 90 degrees. Pitch is inversed.
	 * 
	 * @param rotationYaw
	 * @param rotationPitch
	 */
	public static Vector3 getDeltaPositionFromRotation(float rotationYaw, float rotationPitch)
	{
		rotationYaw = rotationYaw + 90;
		rotationPitch = -rotationPitch;
		return new Vector3(Math.cos(Math.toRadians(rotationYaw)), Math.sin(Math.toRadians(rotationPitch)), Math.sin(Math.toRadians(rotationYaw)));
	}

	/**
	 * RayTrace Codde
	 * 
	 * @author MachineMuse
	 */
	public static MovingObjectPosition raytraceEntities(World world, Vector3 startPosition, float rotationYaw, float rotationPitch, boolean collisionFlag, double reachDistance)
	{
		MovingObjectPosition pickedEntity = null;
		Vec3 startingPosition = startPosition.toVec3();
		Vec3 look = getDeltaPositionFromRotation(rotationYaw, rotationPitch).toVec3();
		Vec3 reachPoint = Vec3.createVectorHelper(startingPosition.xCoord + look.xCoord * reachDistance, startingPosition.yCoord + look.yCoord * reachDistance, startingPosition.zCoord + look.zCoord * reachDistance);

		double playerBorder = 1.1 * reachDistance;
		AxisAlignedBB boxToScan = AxisAlignedBB.getAABBPool().getAABB(-playerBorder, -playerBorder, -playerBorder, playerBorder, playerBorder, playerBorder);

		@SuppressWarnings("unchecked")
		List<Entity> entitiesHit = world.getEntitiesWithinAABBExcludingEntity(null, boxToScan);
		double closestEntity = reachDistance;

		if (entitiesHit == null || entitiesHit.isEmpty())
		{
			return null;
		}
		for (Entity entityHit : entitiesHit)
		{
			if (entityHit != null && entityHit.canBeCollidedWith() && entityHit.boundingBox != null)
			{
				float border = entityHit.getCollisionBorderSize();
				AxisAlignedBB aabb = entityHit.boundingBox.expand(border, border, border);
				MovingObjectPosition hitMOP = aabb.calculateIntercept(startingPosition, reachPoint);

				if (hitMOP != null)
				{
					if (aabb.isVecInside(startingPosition))
					{
						if (0.0D < closestEntity || closestEntity == 0.0D)
						{
							pickedEntity = new MovingObjectPosition(entityHit);
							if (pickedEntity != null)
							{
								pickedEntity.hitVec = hitMOP.hitVec;
								closestEntity = 0.0D;
							}
						}
					}
					else
					{
						double distance = startingPosition.distanceTo(hitMOP.hitVec);

						if (distance < closestEntity || closestEntity == 0.0D)
						{
							pickedEntity = new MovingObjectPosition(entityHit);
							pickedEntity.hitVec = hitMOP.hitVec;
							closestEntity = distance;
						}
					}
				}
			}
		}
		return pickedEntity;
	}

	public static MovingObjectPosition raytraceBlocks(World world, Vector3 startPosition, float rotationYaw, float rotationPitch, boolean collisionFlag, double reachDistance)
	{
		Vector3 lookVector = getDeltaPositionFromRotation(rotationYaw, rotationPitch);
		Vector3 reachPoint = Vector3.add(startPosition, Vector3.multiply(lookVector, reachDistance));
		return world.rayTraceBlocks_do_do(startPosition.toVec3(), reachPoint.toVec3(), collisionFlag, !collisionFlag);
	}

	public static MovingObjectPosition doCustomRayTrace(World world, Vector3 startPosition, float rotationYaw, float rotationPitch, boolean collisionFlag, double reachDistance)
	{
		// Somehow this destroys the playerPosition vector -.-
		MovingObjectPosition pickedBlock = raytraceBlocks(world, startPosition, rotationYaw, rotationPitch, collisionFlag, reachDistance);
		MovingObjectPosition pickedEntity = raytraceEntities(world, startPosition, rotationYaw, rotationPitch, collisionFlag, reachDistance);

		if (pickedBlock == null)
		{
			return pickedEntity;
		}
		else if (pickedEntity == null)
		{
			return pickedBlock;
		}
		else
		{
			double dBlock = startPosition.distanceTo(new Vector3(pickedBlock.hitVec));
			double dEntity = startPosition.distanceTo(new Vector3(pickedEntity.hitVec));

			if (dEntity < dBlock)
			{
				return pickedEntity;
			}
			else
			{
				return pickedBlock;
			}
		}
	}
}
