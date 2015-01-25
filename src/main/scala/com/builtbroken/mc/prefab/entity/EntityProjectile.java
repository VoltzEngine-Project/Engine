package com.builtbroken.mc.prefab.entity;

import com.builtbroken.mc.lib.helper.DamageUtility;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.List;

/**
 * Basic projectile like entity that moves in a strait line without gravity.
 * Handles basic collision and provides methods for changing behavior during
 * impact and movement.
 * <p/>
 * Created by robert on 11/30/2014.
 */
public abstract class EntityProjectile extends EntityBase implements IProjectile
{
    public Pos sourceOfProjectile = null;
    public Entity firedByEntity = null;

    protected boolean isNoMotionInX;
    protected boolean isNoMotionInY;
    protected boolean isNoMotionInZ;

    protected int kill_ticks = 240 /* 2 mins */; /* 144000  2 hours */

    private int _ticksInAir = -1;
    private int _ticksNotMoving = -1;
    private int _ticksInGround = -1;


    public EntityProjectile(World w)
    {
        super(w);
        this.setSize(1F, 1F);
        this.renderDistanceWeight = 3;
        this.isImmuneToFire = true;
        this.ignoreFrustumCheck = true;
    }

    @Deprecated
    public static Pos getEntityAim(EntityLivingBase entity)
    {
        //float f1 = MathHelper.cos(-entity.rotationYaw * 0.017453292F - (float) Math.PI);
        //float f2 = MathHelper.sin(-entity.rotationYaw * 0.017453292F - (float) Math.PI);
        //float f3 = -MathHelper.cos(-entity.rotationPitch * 0.017453292F);
        //float f4 = MathHelper.sin(-entity.rotationPitch * 0.017453292F);
        //new Pos((double) (f2 * f3), (double) f4, (double) (f1 * f3))
        return Pos.getLook(entity, 1);
    }

    public EntityProjectile(EntityLivingBase entity)
    {
        this(entity.worldObj);

        Pos launcher = new Pos(entity).add(new Pos(0, 1, 0));
        Pos playerAim = getEntityAim(entity);
        Pos start = launcher.add(playerAim.multiply(2));

        this.firedByEntity = entity;
        this.sourceOfProjectile = start;
        this.setPosition(start.x(), start.y(), start.z());
        this.rotationYaw = entity.rotationYaw - 180;
        this.rotationPitch = entity.rotationPitch;
    }

    public EntityProjectile(World w, Pos startAndSource)
    {
        this(w);
        this.sourceOfProjectile = startAndSource;
        this.setPosition(startAndSource.x(), startAndSource.y(), startAndSource.z());
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(17, _ticksInAir);
        this.dataWatcher.addObject(16, _ticksInGround);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        //Safety to make sure vars are inited if null
        if (sourceOfProjectile == null)
        {
            sourceOfProjectile = new Pos((Entity) this);
        }
        if (firedByEntity == null)
        {
            firedByEntity = this;
        }

        if (!this.worldObj.isRemote && this.getTicksInAir() != -1)
        {

            isNoMotionInX = this.motionX <= 0.01 && this.motionX >= -0.01;
            isNoMotionInY = this.motionY <= 0.01 && this.motionY >= -0.01;
            isNoMotionInZ = this.motionZ <= 0.01 && this.motionZ >= -0.01;

            //Update on ground tick tracker
            if (this.onGround)
            {
                this.setTicksInGround(this.getTicksInGround() + 1);
                this.setTicksInAir(0);
            }
            else
            {
                setTicksInAir(getTicksInAir() + 1);
                this.setTicksInGround(0);
            }

            //Update movement logic
            if (!checkAndHandleEntityCollisions())
            {
                if (isNoMotionInY)
                {
                    this.addVelocity(0, -0.5, 0);
                }
                updateMotion();
                this.moveFlying((float) this.motionX, (float) this.motionY, (float) this.motionZ);
            }
            if (isNoMotionInX && isNoMotionInY && isNoMotionInZ)
            {
                onStoppedMoving();
            }
        }
    }

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();

        if (shouldKillProjectile())
        {
            this.kill();
        }
    }

    public boolean shouldKillProjectile()
    {
        return this.posY < -640.0D || this.posY > 100000 || this.getTicksInAir() > kill_ticks;
    }

    /**
     * Checks if the projectile has collided with something
     * Then triggers methods saying the projectile has collided with something
     *
     * @return true if the collision stops the projectile from moving
     */
    protected boolean checkAndHandleEntityCollisions()
    {
        //TODO handle entity collision
        return false;
    }

    /**
     * Called each tick to update motion and angles of the entity
     */
    protected void updateMotion()
    {
        moveFlying((float) motionX, (float) motionY, (float) motionZ);
    }

    @Override
    public void moveFlying(float deltaX, float deltaY, float deltaZ)
    {
        move(deltaX, deltaY, deltaZ);
    }

    @Override
    public void moveEntity(double deltaX, double deltaY, double deltaZ)
    {
        move(deltaX, deltaY, deltaZ);
        if (!this.noClip && !isCollided)
        {
            this.updateFallState(motionY, this.onGround);
        }
    }

    protected void move(double deltaX, double deltaY, double deltaZ)
    {
        double originalMotionX = motionX;
        double originalMotionY = motionY;
        double originalMotionZ = motionZ;

        if (this.noClip)
        {
            this.boundingBox.offset(deltaX, deltaY, deltaZ);
            alignToBounds();
        }
        else
        {
            offsetForCollisions(deltaX, deltaY, deltaZ);

            //Update collision checks
            this.isCollidedHorizontally = originalMotionX != motionX || originalMotionZ != motionZ;
            this.isCollidedVertically = originalMotionY != motionY;
            this.onGround = originalMotionY != motionY && originalMotionY < 0.0D;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;

            //if motion changed then this means it hit something
            if (originalMotionX != motionX)
                this.motionX = 0.0D;
            if (isCollidedVertically)
                this.motionY = 0.0D;
            if (originalMotionZ != motionZ)
                this.motionZ = 0.0D;

            if (isCollided)
            {
                this.triggerBlockCollisions();
                motionX = 0;
                motionY = 0;
                motionZ = 0;
            }
        }
    }

    /**
     * Pushes the entity out of its collision so its not overlapping
     *
     * @param deltaX
     * @param deltaY
     * @param deltaZ
     */
    protected void offsetForCollisions(double deltaX, double deltaY, double deltaZ)
    {
        //Calculate Y Offset
        List list = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(deltaX, deltaY, deltaZ));

        for (int i = 0; i < list.size(); ++i)
        {
            motionY = ((AxisAlignedBB) list.get(i)).calculateYOffset(this.boundingBox, motionY);
        }

        //Calculate x offset
        for (int j = 0; j < list.size(); ++j)
        {
            motionX = ((AxisAlignedBB) list.get(j)).calculateXOffset(this.boundingBox, motionX);
        }

        //Calculate z offset
        for (int j = 0; j < list.size(); ++j)
        {
            motionZ = ((AxisAlignedBB) list.get(j)).calculateZOffset(this.boundingBox, motionZ);
        }

        this.boundingBox.offset(motionX, motionY, motionZ);

        alignToBounds();
    }

    /**
     * Called when the projectile collides with the collision box of a Block.
     * Air blocks are ignored to reduce method calls
     *
     * @param block - block hit
     * @param x     - xCoord of block
     * @param y     - xCoord of block
     * @param z     - xCoord of block
     */
    protected void onCollideWithBlock(Block block, int x, int y, int z)
    {
        if (block != null && !block.isAir(worldObj, x, y, z))
        {
            if(!canPassThrewBlock(block, x, y, z))
            {
                try
                {
                    block.onEntityCollidedWithBlock(this.worldObj, x, y, z, this);
                } catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
                    CrashReportCategory.func_147153_a(crashreportcategory, x, y, z, block, this.worldObj.getBlockMetadata(x, y, z));
                    throw new ReportedException(crashreport);
                }
                onImpact();
            }
            onPassThrewBlock(block, x, y, z);
        }
    }

    /**
     * Called to check if the projectile can pass threw a block
     * @return true if the projectile can pass threw or break threw the block
     */
    protected boolean canPassThrewBlock(Block block, int x, int y, int z)
    {
        if(block instanceof IFluidBlock)
        {
            Fluid fluid = ((IFluidBlock) block).getFluid();
            if(fluid == null || fluid.isGaseous())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Called for each block the projectile passes threw or collided with when it stops.
     * Air blocks are ignored by default
     */
    protected void onPassThrewBlock(Block block, int x, int y, int z)
    {

    }

    @Override
    @Deprecated /* redirects to triggerBlockCollisions */
    protected void func_145775_I()
    {
        triggerBlockCollisions();
    }

    /**
     * Checks for any block this overlaps with and handles collision calls for the block
     */
    protected void triggerBlockCollisions()
    {
        int min_x = MathHelper.floor_double(this.boundingBox.minX + 0.001D);
        int min_y = MathHelper.floor_double(this.boundingBox.minY + 0.001D);
        int min_z = MathHelper.floor_double(this.boundingBox.minZ + 0.001D);
        int max_x = MathHelper.ceiling_double_int(this.boundingBox.maxX - 0.001D);
        int max_y = MathHelper.ceiling_double_int(this.boundingBox.maxY - 0.001D);
        int max_z = MathHelper.ceiling_double_int(this.boundingBox.maxZ - 0.001D);

        if (this.worldObj.checkChunksExist(min_x, min_y, min_z, max_x, max_y, max_z))
        {
            for (int cx = min_x; cx <= max_x; ++cx)
            {
                for (int cy = min_y; cy <= max_y; ++cy)
                {
                    for (int cz = min_z; cz <= max_z; ++cz)
                    {
                        Block block = this.worldObj.getBlock(cx, cy, cz);
                        if (block != null)
                        {
                            onCollideWithBlock(block, cx, cy, cz);
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when the projectile stops moving
     */
    protected void onStoppedMoving()
    {
        _ticksNotMoving++;
        if (_ticksNotMoving > 200 && getTicksInAir() != -1)
        {
            setDead();
        }
    }

    /**
     * Called when the missile hit something and stopped
     */
    protected void onImpact()
    {
        this.setDead();
    }



    @Override
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        if (ignoreCollisionForEntity(entity))
            return null;

        return super.getCollisionBox(entity);
    }

    /**
     * Checks if this projectile should ignore collisions
     * if it hits the entity in question
     *
     * @param entity - entity that will collide with
     * @return true if collision should be ignored
     */
    protected boolean ignoreCollisionForEntity(Entity entity)
    {
        //Prevent collision with firing entity for first few ticks
        return entity == firedByEntity && getTicksInAir() <= 50;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("startPos"))
            sourceOfProjectile = new Pos(nbt.getCompoundTag("startPos"));
        _ticksInAir = nbt.getInteger("ticksInAir");
        _ticksInGround = nbt.getInteger("ticksInGround");

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        if (sourceOfProjectile != null)
            nbt.setTag("startPos", sourceOfProjectile.writeNBT(new NBTTagCompound()));
        nbt.setInteger("ticksInAir", this.getTicksInAir());
        nbt.setInteger("ticksInGround", this.getTicksInGround());
    }

    @Override
    public void setThrowableHeading(double motionX, double motionY, double motionZ, float power, float spread)
    {
        float square = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= (double) square;
        motionY /= (double) square;
        motionZ /= (double) square;

        //Calculate spread area
        motionX += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) spread;
        motionY += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) spread;
        motionZ += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) spread;

        //Apply power
        motionX *= (double) power;
        motionY *= (double) power;
        motionZ *= (double) power;

        //Set motion
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;

        //Set angles
        float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(motionY, (double) f3) * 180.0D / Math.PI);

        //Set default values
        this.setTicksInAir(0);
    }

    public void setMotion(int power)
    {
        motionX = (double) (-MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI));
        motionZ = (double) (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI));
        motionY = (double) (-MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI));

        motionX *= power;
        motionY *= power;
        motionZ *= power;
    }

    @Override
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch)
    {
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(yaw, pitch);
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    public int getTicksInGround()
    {
        if (worldObj == null || !worldObj.isRemote)
        {
            return _ticksInGround;
        }
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public void setTicksInGround(int ticks)
    {
        if (!this.worldObj.isRemote)
        {
            _ticksInGround = ticks;
            this.dataWatcher.updateObject(16, ticks);
        }
    }

    public int getTicksInAir()
    {
        if (worldObj == null || !worldObj.isRemote)
        {
            return _ticksInAir;
        }
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setTicksInAir(int ticks)
    {
        if (!this.worldObj.isRemote)
        {
            _ticksInAir = ticks;
            this.dataWatcher.updateObject(17, ticks);
        }
    }

    @Override
    public boolean canBePushed()
    {
        return true;
    }
}
