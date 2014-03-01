package calclavia.lib.utility;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import calclavia.lib.network.IPacketLoad;
import calclavia.lib.prefab.IGyroMotor;
import calclavia.lib.prefab.IServo;
import calclavia.lib.utility.nbt.ISaveObj;

import com.google.common.io.ByteArrayDataInput;

/**
 * Modular way of dealing with yaw and pitch rotation of an object
 * 
 * @author DarkGuardsman
 */
public class RotationManager implements ISaveObj, IPacketLoad
{
	protected IGyroMotor motor;
	protected float targetYaw, targetPitch;

	public RotationManager(IGyroMotor motor)
	{
		this.motor = motor;
	}

	public void setTargetRotation(float yaw, float pitch)
	{
		this.targetYaw = yaw;
		this.targetPitch = pitch;
	}

	public void updateRotation(float speed)
	{
		this.updateRotation(speed, speed);
	}

	public void updateRotation(float speedYaw, float speedPitch)
	{
		// Clamp target angles
		this.targetYaw = (int) MathUtility.clampAngleTo360(this.targetYaw);
		this.targetPitch = (int) MathUtility.clampAngleTo360(this.targetPitch);
		updateRotation(this.motor.getYawServo(), speedYaw, this.targetYaw);
		updateRotation(this.motor.getPitchServo(), speedYaw, this.targetPitch);
	}

	/**
	 * Updates the rotation of a servo
	 * 
	 * @param servo - server to be rotated
	 * @param speed - limit of how much to rotate in this update
	 * @param targetRotation - target rotation
	 */
	public static void updateRotation(IServo servo, float speed, float targetRotation)
	{
		if (servo != null)
		{
			float actualRotation = servo.getRotation();
			if (Math.abs(actualRotation - targetRotation) > speed)
			{
				if (Math.abs(actualRotation - targetRotation) >= 180)
				{
					actualRotation += actualRotation > targetRotation ? speed : -speed;
				}
				else
				{
					actualRotation += actualRotation > targetRotation ? -speed : speed;
				}
			}
			else
			{
				actualRotation = targetRotation;
			}

			actualRotation = MathUtility.clampAngleTo180(actualRotation);

			if (actualRotation > servo.upperLimit())
			{
				actualRotation = servo.upperLimit();
			}
			else if (actualRotation < servo.lowerLimit())
			{
				actualRotation = servo.lowerLimit();
			}
			servo.setRotation(actualRotation);
		}
	}

	@Override
	public void save(NBTTagCompound nbt)
	{
		nbt.setFloat("targetYaw", this.targetYaw);
		nbt.setFloat("targetPitch", this.targetPitch);

	}

	@Override
	public void load(NBTTagCompound nbt)
	{
		this.targetYaw = nbt.getFloat("targetYaw");
		this.targetPitch = nbt.getFloat("targetPitch");

	}

	@Override
	public void readPacket(ByteArrayDataInput data)
	{
		this.targetYaw = data.readFloat();
		this.targetPitch = data.readFloat();
		this.motor.getYawServo().setRotation(data.readFloat());
		this.motor.getPitchServo().setRotation(data.readFloat());
	}

	@Override
	public void loadPacket(DataOutputStream data) throws IOException
	{
		data.writeFloat(this.targetYaw);
		data.writeFloat(this.targetPitch);
		data.writeFloat(this.motor.getYawServo().getRotation());
		data.writeFloat(this.motor.getPitchServo().getRotation());

	}
}
