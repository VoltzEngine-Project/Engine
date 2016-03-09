package com.builtbroken.mc.lib.transform.rotation;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.jlib.helpers.MathHelper;
import com.builtbroken.mc.core.network.IByteBufWriter;
import com.builtbroken.mc.lib.transform.ITransform;
import com.builtbroken.mc.lib.transform.vector.Pos;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * This object is not immutable like other vector objects. It is designed to take the player of storing 3 separate variables for rotation. Thus it will
 * also be setup to allow adjustments to rotation.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/8/2016.
 * <p/>
 * Original version by Calclavia
 */
public class EulerAngle implements Cloneable, ITransform, IByteBufWriter
{
    protected double yaw = 0;
    protected double pitch = 0;
    protected double roll = 0;

    /**
     * Creates a new EulerAngle from yaw, pitch, and roll
     *
     * @param yaw   - value for yaw
     * @param pitch - value for pitch
     * @param roll  - value for roll
     */
    public EulerAngle(double yaw, double pitch, double roll)
    {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    /**
     * Creates a new EulerAngle from yaw and pitch
     *
     * @param yaw   - value for yaw
     * @param pitch - value for pitch
     */
    public EulerAngle(double yaw, double pitch)
    {
        this(yaw, pitch, 0);
    }

    /**
     * Creats a new EulerAngle from NBT
     *
     * @param tag - save
     */
    public EulerAngle(NBTTagCompound tag)
    {
        readFromNBT(tag);
    }

    /**
     * Creates a new EulerAngle from data
     *
     * @param data - data, needs to have 3 doubles or will crash
     */
    public EulerAngle(ByteBuf data)
    {
        readByteBuf(data);
    }

    /**
     * Creates a new EulerAngle from a {@link ForgeDirection}
     *
     * @param direction - direction
     */
    public EulerAngle(ForgeDirection direction)
    {
        switch (direction)
        {
            case DOWN:
                pitch = -90;
                break;
            case UP:
                pitch = 90;
                break;
            case NORTH:
                yaw = 0;
                break;
            case SOUTH:
                yaw = 180;
                break;
            case EAST:
                yaw = -90;
                break;
            case WEST:
                yaw = 90;
                break;
        }
    }

    /**
     * Sets the values of yaw, pitch and roll
     *
     * @param yaw   - value to set yaw
     * @param pitch - value to set pitch
     * @param roll  - value to set roll
     */
    public void set(double yaw, double pitch, double roll)
    {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    /**
     * Sets the value of the index angle to the value provided
     * Legacy code
     *
     * @param index - index 0 yaw, 1 pitch, 2 roll
     * @param value - value to set
     */
    public void set(int index, double value)
    {
        if (index == 0)
        {
            this.yaw = value;
        }
        if (index == 1)
        {
            this.pitch = value;
        }
        if (index == 2)
        {
            this.roll = value;
        }
    }

    /**
     * Sets the angle value of the provided angle as this angle
     *
     * @param other - values to use
     * @return this
     */
    public EulerAngle set(EulerAngle other)
    {
        yaw = other.yaw;
        pitch = other.pitch;
        roll = other.roll;
        return this;
    }

    //=========================================================================================
    //===================================Operations============================================
    //=========================================================================================


    /**
     * Adds the value to each angle value
     *
     * @param v - value to add
     * @return this
     */
    public EulerAngle add(double v)
    {
        this.yaw += v;
        this.pitch += v;
        this.roll += v;
        return this;
    }

    /**
     * Adds the angles together
     *
     * @param other - angle to add
     * @return this
     */
    public EulerAngle add(EulerAngle other)
    {
        this.yaw += other.yaw;
        this.pitch += other.pitch;
        this.roll += other.roll;
        return this;
    }

    /**
     * Multiply all the angles by v
     *
     * @param v - value to multiply
     * @return this
     */
    public EulerAngle multiply(double v)
    {
        this.yaw *= v;
        this.pitch *= v;
        this.roll *= v;
        return this;
    }

    /**
     * Multiply all the angles by v
     *
     * @param v - value to multiply
     * @return this
     */
    public EulerAngle multiply(float v)
    {
        this.yaw *= v;
        this.pitch *= v;
        this.roll *= v;
        return this;
    }

    /**
     * Multiply the angle by the other
     *
     * @param other
     * @return this
     */
    public EulerAngle multiply(EulerAngle other)
    {
        this.yaw *= other.yaw;
        this.pitch *= other.pitch;
        this.roll *= other.roll;
        return this;
    }

    /**
     * 1 / angle
     *
     * @return this
     */
    public EulerAngle reciprocal()
    {
        this.yaw = 1 / yaw;
        this.pitch = 1 / pitch;
        this.roll = 1 / roll;
        return this;
    }

    /**
     * Rounds the angles up
     *
     * @return this
     */
    public EulerAngle ceil()
    {
        {
            this.yaw = Math.ceil(yaw);
            this.pitch = Math.ceil(pitch);
            this.roll = Math.ceil(roll);
            return this;
        }
    }

    /**
     * Sends the angles to the lowest rounded value
     *
     * @return this
     */
    public EulerAngle floor()
    {
        this.yaw = Math.floor(yaw);
        this.pitch = Math.floor(pitch);
        this.roll = Math.floor(roll);
        return this;
    }

    /**
     * Rounds the angles
     *
     * @return this
     */
    public EulerAngle round()
    {
        this.yaw = Math.round(yaw);
        this.pitch = Math.round(pitch);
        this.roll = Math.round(roll);
        return this;
    }

    /**
     * Gets the bigger values from the two angles
     *
     * @param other - angle to compare
     * @return new EulerAngle containing the larger values
     */
    public EulerAngle max(EulerAngle other)
    {
        return new EulerAngle(Math.max(yaw, other.yaw), Math.max(pitch, other.pitch), Math.max(roll, other.roll));
    }


    /**
     * Gets the smaller values from the two angles
     *
     * @param other - angle to compare
     * @return new EulerAngle containing the smallest values
     */
    public EulerAngle min(EulerAngle other)
    {
        return new EulerAngle(Math.min(yaw, other.yaw), Math.min(pitch, other.pitch), Math.min(roll, other.roll));
    }

    /**
     * Gets the different between the two angles
     *
     * @param other - angle to compare
     * @return new EulerAngle containing the differences between the two angles
     */
    public EulerAngle absoluteDifference(EulerAngle other)
    {
        return new EulerAngle(Math.abs(yaw - other.yaw), Math.abs(pitch - other.pitch), Math.abs(roll - other.roll));
    }

    /**
     * Checks if the angle is within a margin of the other angle
     *
     * @param other  - angle to check against
     * @param margin - room for error
     * @return true if all 3 angles are near the margin value of error
     */
    public boolean isWithin(EulerAngle other, double margin)
    {
        final EulerAngle angle = absoluteDifference(other);
        return angle.yaw < margin && angle.pitch < margin && angle.roll < margin;
    }

    @Override
    public IPos3D transform(IPos3D vector)
    {
        return new Pos(vector).transform(toQuaternion());
    }

    @Deprecated
    public IPos3D toVector()
    {
        return new Pos(-Math.sin(yaw) * Math.cos(pitch), Math.sin(pitch), -Math.cos(yaw) * Math.cos(pitch));
    }

    /**
     * Converts object to {@link Pos}
     *
     * @return new {@link Pos}
     */
    public Pos toPos()
    {
        return new Pos(-Math.sin(yaw) * Math.cos(pitch), Math.sin(pitch), -Math.cos(yaw) * Math.cos(pitch));
    }

    /**
     * Converts object to {@link AngleAxis}
     *
     * @return new {@link AngleAxis}
     */
    public AngleAxis toAngleAxis()
    {
        double c1 = Math.cos(yaw / 2);
        double s1 = Math.sin(yaw / 2);
        double c2 = Math.cos(pitch / 2);
        double s2 = Math.sin(pitch / 2);
        double c3 = Math.cos(roll / 2);
        double s3 = Math.sin(roll / 2);
        double c1c2 = c1 * c2;
        double s1s2 = s1 * s2;
        double w = c1c2 * c3 - s1s2 * s3;
        double x = c1c2 * s3 + s1s2 * c3;
        double y = s1 * c2 * c3 + c1 * s2 * s3;
        double z = c1 * s2 * c3 - s1 * c2 * s3;

        double angle = 2 * Math.acos(w);
        Pos axis = new Pos(x, y, z);

        if (axis.magnitudeSquared() < 0.001)
        {
            axis = new Pos(0, 0, -1);
        }
        else
        {
            axis = axis.normalize();
        }

        return new AngleAxis(angle, axis);
    }

    /**
     * Converts object to new {@link Quaternion}
     *
     * @return new {@link Quaternion}
     */
    public Quaternion toQuaternion()
    {
        // Assuming the angles are in radians.
        double c1 = Math.cos(Math.toRadians(yaw) / 2);
        double s1 = Math.sin(Math.toRadians(yaw) / 2);
        double c2 = Math.cos(Math.toRadians(pitch) / 2);
        double s2 = Math.sin(Math.toRadians(pitch) / 2);
        double c3 = Math.cos(Math.toRadians(roll) / 2);
        double s3 = Math.sin(Math.toRadians(roll) / 2);
        double c1c2 = c1 * c2;
        double s1s2 = s1 * s2;
        double w = c1c2 * c3 - s1s2 * s3;
        double x = c1c2 * s3 + s1s2 * c3;
        double y = s1 * c2 * c3 + c1 * s2 * s3;
        double z = c1 * s2 * c3 - s1 * c2 * s3;
        return new Quaternion(w, x, y, z);
    }

    /**
     * Converts object into an array of doubles(yaw, pitch, roll).
     * More or less legacy code...
     *
     * @return 3 size array
     */
    public double[] toArray()
    {
        return new double[]{yaw, pitch, roll};
    }

    @Override
    public EulerAngle clone()
    {
        return new EulerAngle(yaw, pitch, roll);
    }

    @Override
    public String toString()
    {
        return "EulerAngle[" + yaw + "," + pitch + "," + roll + "]";
    }

    /**
     * @Deprecated {@link #writeBytes(ByteBuf)}
     * @param data
     */
    @Deprecated
    public void writeByteBuf(ByteBuf data)
    {
        data.writeDouble(yaw);
        data.writeDouble(pitch);
        data.writeDouble(roll);
    }


    @Override
    public ByteBuf writeBytes(ByteBuf data)
    {
        data.writeDouble(yaw);
        data.writeDouble(pitch);
        data.writeDouble(roll);
        return data;
    }

    public void readByteBuf(ByteBuf data)
    {
        yaw = data.readDouble();
        pitch = data.readDouble();
        roll = data.readDouble();
    }

    public NBTTagCompound writeNBT(NBTTagCompound nbt)
    {
        nbt.setDouble("yaw", yaw);
        nbt.setDouble("pitch", pitch);
        nbt.setDouble("roll", roll);
        return nbt;
    }

    public NBTTagCompound toNBT()
    {
        return writeNBT(new NBTTagCompound());
    }

    public EulerAngle readFromNBT(NBTTagCompound nbt)
    {
        yaw = nbt.getDouble("yaw");
        pitch = nbt.getDouble("pitch");
        roll = nbt.getDouble("roll");
        return this;
    }


    /**
     * Clamps all 3 angles to 360 degrees
     *
     * @return this
     */
    public EulerAngle clampTo360()
    {
        this.yaw = clampAngleTo360(yaw);
        this.pitch = clampAngleTo360(pitch);
        this.roll = clampAngleTo360(roll);
        return this;
    }

    /**
     * Moves this angle to the selected angle over time
     *
     * @param aim       - aim to move towards
     * @param deltaTime - percent to move by
     * @return this
     */
    public EulerAngle lerp(EulerAngle aim, double deltaTime)
    {
        this.yaw = MathHelper.lerp(yaw, aim.yaw, deltaTime);
        this.pitch = MathHelper.lerp(pitch, aim.pitch, deltaTime);
        this.roll = MathHelper.lerp(roll, aim.roll, deltaTime);
        return this;
    }

    private final double clampAngleTo360(double value)
    {
        return clampAngle(value, -360, 360);
    }

    private final double clampAngle(double value, double min, double max)
    {
        double result = value;
        while (result < min)
        {
            result += 360;
        }
        while (result > max)
        {
            result -= 360;
        }
        return result;
    }

    public double yaw()
    {
        return yaw;
    }

    public double pitch()
    {
        return pitch;
    }

    public double roll()
    {
        return roll;
    }

    @Deprecated
    public void yaw_$eq(double v)
    {
        yaw = v;
    }

    @Deprecated
    public void pitch_$eq(double v)
    {
        pitch = v;
    }

    @Deprecated
    public void roll_$eq(double v)
    {
        roll = v;
    }

    public void setYaw(double v)
    {
        yaw = v;
    }

    public void setPitch(double v)
    {
        pitch = v;
    }

    public void setRoll(double v)
    {
        roll = v;
    }
}
