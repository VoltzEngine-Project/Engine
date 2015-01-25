package com.builtbroken.mc.prefab.entity;

import com.builtbroken.mc.lib.helper.DamageUtility;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by robert on 1/24/2015.
 */
public abstract class EntityBase extends Entity
{
    protected boolean hasHealth = false;
    protected float maxHealth = 5;

    private float _health = 0;

    public EntityBase(World world)
    {
        super(world);
    }

    @Override
    protected void entityInit()
    {
        if(hasHealth)
            this._health = getMaxHealth();
        this.dataWatcher.addObject(6, _health);
    }

    public void setHealth(float hp)
    {
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
}
