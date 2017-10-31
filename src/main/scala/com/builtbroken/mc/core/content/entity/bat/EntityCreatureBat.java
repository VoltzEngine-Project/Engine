package com.builtbroken.mc.core.content.entity.bat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Mirror of Minecraft' EntityBat with minor adjustments to allow
 * for more complex logic and behavior
 */
public class EntityCreatureBat extends EntityCreature
{
    public static int DATA_BAT_FLAG = 16;

    /** Coordinates of where the bat spawned. */
    private ChunkCoordinates spawnPosition;

    public EntityCreatureBat(World p_i1680_1_)
    {
        super(p_i1680_1_);
        this.setSize(0.5F, 0.9F);
        this.setIsBatHanging(true);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(DATA_BAT_FLAG, new Byte((byte) 0));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
    }

    @Override
    protected float getSoundVolume()
    {
        return 0.1F;
    }

    @Override
    protected float getSoundPitch()
    {
        return super.getSoundPitch() * 0.95F;
    }

    @Override
    protected String getLivingSound()
    {
        return this.isBatHanging() && this.rand.nextInt(4) != 0 ? null : "mob.bat.idle";
    }

    @Override
    protected String getHurtSound()
    {
        return "mob.bat.hurt";
    }

    @Override
    protected String getDeathSound()
    {
        return "mob.bat.death";
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entity)
    {
        //No need to push entities
    }

    @Override
    protected void collideWithNearbyEntities()
    {
        //No need to collide
    }

    public boolean isBatHanging()
    {
        return (this.dataWatcher.getWatchableObjectByte(DATA_BAT_FLAG) & 1) != 0;
    }

    public void setIsBatHanging(boolean hang)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(DATA_BAT_FLAG);

        if (hang)
        {
            this.dataWatcher.updateObject(DATA_BAT_FLAG, Byte.valueOf((byte) (b0 | 1)));
        }
        else
        {
            this.dataWatcher.updateObject(DATA_BAT_FLAG, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.isBatHanging())
        {
            this.motionX = this.motionY = this.motionZ = 0.0D;
            this.posY = (double) MathHelper.floor_double(this.posY) + 1.0D - (double) this.height;
        }
        else
        {
            this.motionY *= 0.6000000238418579D;
        }
    }

    public void doBatLogic()
    {
        if (this.isBatHanging())
        {
            //Stop hanging if block is no longer there
            if (!this.worldObj.getBlock(MathHelper.floor_double(this.posX), (int) this.posY + 1, MathHelper.floor_double(this.posZ)).isNormalCube())
            {
                this.setIsBatHanging(false);
                this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1015, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
            }
            else
            {
                //Rotate head randomly
                if (this.rand.nextInt(200) == 0)
                {
                    this.rotationYawHead = (float) this.rand.nextInt(360);
                }
            }
        }
        else
        {
            doMovementToSpawn();
        }
    }

    protected void doMovementToSpawn()
    {
        if (this.spawnPosition != null && (!this.worldObj.isAirBlock(this.spawnPosition.posX, this.spawnPosition.posY, this.spawnPosition.posZ) || this.spawnPosition.posY < 1))
        {
            this.spawnPosition = null;
        }

        if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.getDistanceSquared((int) this.posX, (int) this.posY, (int) this.posZ) < 4.0F)
        {
            this.spawnPosition = new ChunkCoordinates((int) this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int) this.posY + this.rand.nextInt(6) - 2, (int) this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
        }

        double deltaX = (double) this.spawnPosition.posX + 0.5D - this.posX;
        double deltaY = (double) this.spawnPosition.posY + 0.1D - this.posY;
        double deltaZ = (double) this.spawnPosition.posZ + 0.5D - this.posZ;

        this.motionX += (Math.signum(deltaX) * 0.5D - this.motionX) * 0.10000000149011612D;
        this.motionY += (Math.signum(deltaY) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
        this.motionZ += (Math.signum(deltaZ) * 0.5D - this.motionZ) * 0.10000000149011612D;

        float yaw = (float) (Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;
        float deltaYaw = MathHelper.wrapAngleTo180_float(yaw - this.rotationYaw);
        this.moveForward = 0.5F;
        this.rotationYaw += deltaYaw;

        if (this.rand.nextInt(100) == 0 && this.worldObj.getBlock(MathHelper.floor_double(this.posX), (int) this.posY + 1, MathHelper.floor_double(this.posZ)).isNormalCube())
        {
            this.setIsBatHanging(true);
        }
    }

    @Override
    protected void updateAITasks()
    {
        super.updateAITasks();
        doBatLogic();
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void fall(float p_70069_1_)
    {
    }

    @Override
    protected void updateFallState(double p_70064_1_, boolean p_70064_3_)
    {
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            if (!this.worldObj.isRemote && this.isBatHanging())
            {
                this.setIsBatHanging(false);
            }

            return super.attackEntityFrom(p_70097_1_, p_70097_2_);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.dataWatcher.updateObject(DATA_BAT_FLAG, Byte.valueOf(nbt.getByte("BatFlags")));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(DATA_BAT_FLAG));
    }
}