package com.builtbroken.mc.prefab.entity;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketEntity;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.lib.helper.DamageUtility;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.transform.vector.Pos;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Base entity class to be shared by most entities
 * Created by robert on 1/24/2015.
 */
public abstract class EntityBase extends Entity implements IPacketIDReceiver
{
    /** Does the entity have HP to take damage. */
    protected boolean hasHealth = false;
    /** What is the default max HP of the entity. */
    protected float maxHealth = 5;
    /** The current HP of the entity. */
    private float _health = 0;

    public EntityBase(World world)
    {
        super(world);
    }

    @Override
    protected void entityInit()
    {
        if (hasHealth)
        {
            this._health = getMaxHealth();
        }
        this.dataWatcher.addObject(6, _health);
    }

    public void setHealth(float hp)
    {
        //TODO recode to just use the data watcher
        _health = hp;
        this.dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(hp, 0.0F, this.getMaxHealth())));
    }

    public float getHealth()
    {
        if (worldObj == null || !worldObj.isRemote)
        {
            return _health;
        }
        return this.dataWatcher.getWatchableObjectFloat(6);
    }

    public float getMaxHealth()
    {
        return maxHealth;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        if (hasHealth && DamageUtility.canHarm(this, source, damage))
        {
            this.setHealth(Math.max(getHealth() - damage, 0));
            if (getHealth() <= 0)
            {
                onDestroyedBy(source, damage);
            }
            return true;
        }
        return false;
    }

    /**
     * Called when the entity is killed
     */
    protected void onDestroyedBy(DamageSource source, float damage)
    {
        this.setDead();
    }

    /**
     * Sets the position based on the bounding box
     */
    protected void alignToBounds()
    {
        this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
        this.posY = this.boundingBox.minY + (double) this.yOffset - (double) this.ySize;
        this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
    }

    /**
     * Gets the predicted position
     *
     * @param t - number of ticks to predicted
     * @return predicted position of the project
     */
    public Pos getPredictedPosition(int t)
    {
        Pos newPos = new Pos(this);

        for (int i = 0; i < t; i++)
        {
            newPos.add(motionX, motionY, motionZ);
        }

        return newPos;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        _health = nbt.getFloat("health");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setFloat("health", this.getHealth());
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if (worldObj.isRemote)
        {
            //Updates client if cargo changes
            if (id == -1)
            {
                readDescData(buf);
                return true;
            }
        }
        return false;
    }

    /**
     * Sends basic data that describes the entity
     */
    protected void sentDescriptionPacket()
    {
        final PacketEntity entity = new PacketEntity(this, -1);
        writeDescData(entity.data());
        Engine.instance.packetHandler.sendToAllAround(entity, new Location(this), 64);
    }

    /**
     * Writes desc data to packet
     *
     * @param buffer - write area
     */
    public void writeDescData(ByteBuf buffer)
    {
        if (this instanceof IEntityAdditionalSpawnData)
        {
            ((IEntityAdditionalSpawnData) this).writeSpawnData(buffer);
        }
    }

    /**
     * Reads desc data from packet
     *
     * @param buffer - data
     */
    public void readDescData(ByteBuf buffer)
    {
        if (this instanceof IEntityAdditionalSpawnData)
        {
            ((IEntityAdditionalSpawnData) this).readSpawnData(buffer);
        }
    }
}
