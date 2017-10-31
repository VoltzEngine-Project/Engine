package com.builtbroken.mc.core.content.entity.bat.ex;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.api.explosive.IExplosiveHolder;
import com.builtbroken.mc.core.content.entity.bat.EntityCreatureBat;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import com.builtbroken.mc.imp.transform.vector.Location;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

/**
 * Explosive version of the bat for use in {@link com.builtbroken.mc.api.explosive.IExplosiveHandler} for mods like ICBM
 * and Armory mod. That introduce odd weapons modeled after real life weapons developed by USA during WWII. That used
 * bats with fire bombs to catch buildings on fire. https://en.wikipedia.org/wiki/Bat_bomb
 * <p>
 * This is included as part of Voltz Engine for reuse by other mods. This way each mod is not recreating the entity
 * wasting resources.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2017.
 */
public class EntityExBat extends EntityCreatureBat implements IExplosiveHolder, IEntityAdditionalSpawnData
{
    /** Datawather ID for the fuse timer */
    public static int DATA_FUSE_TIMER = 17;

    /** Sets the AI type to normal (Attacks players only) */
    public static int TYPE_NORMAL = 0;
    /** Sets the AI type to hostile (Attacks and Animals) */
    public static int TYPE_HOSTILE = 1;
    /** Sets the AI type to bomb for use with death timer (Attacks nothing) */
    public static int TYPE_BOMB = 2;

    //Timer for fuse to go off
    protected int fuseTicks = 10;
    //Enables the fuse
    protected boolean enableFuse = false;

    //Timer for explosive to trigger automaticly
    public int explosiveTimer = -1;

    /** Type of AI to use */
    public int ai_type = 0;
    private boolean isAISetup = false;

    //Explosive data
    protected double ex_size = 3;
    protected IExplosiveHandler ex;
    protected NBTTagCompound ex_data;

    /** Point for the bat to fly towards */
    public IPos3D target;

    public EntityExBat(World world)
    {
        super(world);
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    protected void setupAI()
    {
        if (ai_type == TYPE_NORMAL || ai_type == TYPE_HOSTILE)
        {
            this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
            if (ai_type == TYPE_HOSTILE)
            {
                this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityAnimal.class, 0, true));
            }
        }
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(DATA_FUSE_TIMER, -1);
    }

    @Override
    protected void jump()
    {
        this.motionY = 2;
        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }

    @Override
    public void onUpdate()
    {
        //Setup AI
        if (!isAISetup)
        {
            isAISetup = true;
            setupAI();
        }

        //Do action if alive
        if (this.isEntityAlive())
        {
            //Count down death timer
            if (explosiveTimer > 0)
            {
                explosiveTimer -= 1;
            }

            //Trigger fuse if there is a target
            enableFuse = this.getAttackTarget() != null && this.getAttackTarget().getDistanceSqToEntity(this) < 9;

            //----------------------------------------
            int fuse = this.getFuse();
            //Play audio if fuse is active
            if (fuse > 0)
            {
                this.playSound("creeper.primed", 1.0F, 0.5F);
            }

            //Tick timer up if its close enough
            if (isFuseActive())
            {
                if(worldObj.isRemote)
                {
                    worldObj.spawnParticle("smoke", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
                    worldObj.spawnParticle("flame", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
                }
                setFuse(fuse + 1);
            }
            //Tick timer down
            else if (fuse > 1)
            {
                setFuse(fuse - 1);
            }

            //If time has been reached detonate
            if (fuse >= fuseTicks)
            {
                this.explode();
            }
            //----------------------------------------
        }

        super.onUpdate();
    }

    @Override
    public void doBatLogic()
    {
        if (isBatHanging())
        {
            //Stop hanging if there is a target to attack
            this.setIsBatHanging(false);
            this.worldObj.playAuxSFXAtEntity(null, 1015, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
        }
    }


    protected boolean isFuseActive()
    {
        return enableFuse || explosiveTimer == 0;
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

    @Override
    public NBTTagCompound getAdditionalExplosiveData()
    {
        return ex_data;
    }

    @Override
    public double getExplosiveSize()
    {
        return ex_size;
    }

    protected void explode()
    {
        if (!this.worldObj.isRemote)
        {
            boolean allow_mob_damage = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
            if (ex == null)
            {
                this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float) ex_size, allow_mob_damage);
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
        nbt.setInteger("explosiveTimer", explosiveTimer);
        nbt.setShort("fuse", (short) this.fuseTicks);
        nbt.setInteger("ai_type", ai_type);
        if (ex != null)
        {
            nbt.setString("ex", ex.getID());
            nbt.setDouble("size", ex_size);
            if (ex_data != null)
            {
                nbt.setTag("ex_data", ex_data);
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("explosiveTimer"))
        {
            this.explosiveTimer = nbt.getInteger("explosiveTimer");
        }
        this.fuseTicks = nbt.getShort("fuse");
        this.ai_type = nbt.getInteger("ai_type");
        if (nbt.hasKey("ex"))
        {
            ex = ExplosiveRegistry.get(nbt.getString("ex"));
            ex_size = Math.max(nbt.getDouble("size"), 1.0);
            if (nbt.hasKey("ex_data"))
            {
                ex_data = nbt.getCompoundTag("ex_data");
            }
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeBoolean(ex != null);
        if (ex != null)
        {
            ByteBufUtils.writeUTF8String(buffer, ex.getID());
            if (ex_data != null)
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
        if (ex_exists)
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
        return this.dataWatcher.getWatchableObjectInt(DATA_FUSE_TIMER);
    }

    public void setFuse(int f)
    {
        this.dataWatcher.updateObject(DATA_FUSE_TIMER, f);
    }
}
