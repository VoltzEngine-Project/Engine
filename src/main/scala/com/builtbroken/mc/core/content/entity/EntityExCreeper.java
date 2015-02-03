package com.builtbroken.mc.core.content.entity;

import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.api.explosive.IExplosiveHolder;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.world.explosive.ExplosiveRegistry;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import li.cil.repack.org.luaj.vm2.ast.Exp;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Simplified prefab that contains an explosive for triggering. Mimics a creeper by default
 * and can be used to make alternate creepers.
 * Created by robert on 1/31/2015.
 */
public class EntityExCreeper extends EntityMob implements IExplosiveHolder, IEntityAdditionalSpawnData
{
    protected int fuseTicks = 30;
    protected double ex_size = 3;
    protected IExplosiveHandler ex;
    protected NBTTagCompound ex_data;

    public EntityExCreeper(World w)
    {
        super(w);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    public EntityExCreeper(Location location, IExplosiveHandler ex, double size, NBTTagCompound tag)
    {
        this(location.world);
        this.setPosition(location.x(), location.y(), location.z());
        this.setExplosive(ex, size, tag);
    }

    public static void replaceCreeper(EntityCreeper creeper, IExplosiveHandler ex)
    {
        replaceCreeper(creeper, ex, 2, null);
    }

    public static void replaceCreeper(EntityCreeper creeper, IExplosiveHandler ex, double size, NBTTagCompound data)
    {
        EntityExCreeper cex = new EntityExCreeper(new Location(creeper), ex, size, data);
        creeper.setDead();
        creeper.worldObj.removeEntity(creeper);
        cex.worldObj.spawnEntityInWorld(cex);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) -1));
    }

    @Override
    public void onUpdate()
    {
        if (this.isEntityAlive())
        {
            int f = this.getFuse();
            //Play audio if fuse is active
            if (f > 0)
            {
                this.playSound("creeper.primed", 1.0F, 0.5F);
            }

            //Tick timer up if its close enough
            if (this.getAttackTarget() != null && this.getAttackTarget().getDistanceSqToEntity(this) < 9)
            {
                setFuse(f + 1);
            }
            //Tick timer down
            else if (f > 1)
            {
                setFuse(f - 1);
            }

            //If time has been reached detonate
            if (f >= fuseTicks)
            {
                this.explode();
            }
        }

        super.onUpdate();
    }

    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        return true;
    }

    @Override
    public IExplosiveHandler getExplosive()
    {
        return ex;
    }

    @Override
    public boolean setExplosive(IExplosiveHandler ex, double size, NBTTagCompound nbt)
    {
        this.ex_size = size;
        this.ex = ex;
        this.ex_data = nbt;
        return true;
    }

    protected void explode()
    {
        if (!this.worldObj.isRemote)
        {
            boolean allow_mob_damage = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
            if (ex == null)
            {
                this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3, allow_mob_damage);
            }
            else
            {
                ExplosiveRegistry.triggerExplosive(new Location(this), ex, new TriggerCause.TriggerCauseEntity(this), ex_size, ex_data);
            }
            this.setDead();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setShort("Fuse", (short) this.fuseTicks);
        if (ex != null)
        {
            nbt.setString("ex", ex.getID());
            nbt.setDouble("size", ex_size);
            if (ex_data != null)
                nbt.setTag("ex_data", ex_data);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.fuseTicks = nbt.getShort("Fuse");
        if (nbt.hasKey("ex"))
        {
            ex = ExplosiveRegistry.get(nbt.getString("ex"));
            ex_size = Math.max(nbt.getDouble("size"), 1.0);
            if (nbt.hasKey("ex_data"))
                ex_data = nbt.getCompoundTag("ex_data");
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeBoolean(ex != null);
        if(ex != null)
        {
            ByteBufUtils.writeUTF8String(buffer, ex.getID());
            if(ex_data != null)
            {
                ByteBufUtils.writeTag(buffer, ex_data);
            }
            else
            {
                ByteBufUtils.writeTag(buffer, new NBTTagCompound());
            }
        }
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        boolean ex_exists = buffer.readBoolean();
        if(ex_exists)
        {
            ex = ExplosiveRegistry.get(ByteBufUtils.readUTF8String(buffer));
            ex_data = ByteBufUtils.readTag(buffer);
        }
        else
        {
            ex = null;
            ex_data = null;
        }
    }

    public int getFuse()
    {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    public void setFuse(int f)
    {
        this.dataWatcher.updateObject(16, Byte.valueOf((byte) f));
    }

    @Override
    protected String getHurtSound()
    {
        return "mob.creeper.say";
    }

    @Override
    protected String getDeathSound()
    {
        return "mob.creeper.death";
    }

    @Override
    public boolean isAIEnabled()
    {
        return true;
    }

    @Override
    public int getMaxSafePointTries()
    {
        return this.getAttackTarget() == null ? 3 : 3 + (int) (this.getHealth() - 1.0F);
    }
}
