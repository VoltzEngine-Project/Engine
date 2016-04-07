package com.builtbroken.mc.prefab.explosive.blast;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldChangeAudio;
import com.builtbroken.mc.api.edit.IWorldChangeGraphics;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import com.builtbroken.mc.lib.world.edit.BlockEditResult;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Prefab for implement explosive blast actions
 */
public abstract class Blast<B extends Blast> implements IWorldChangeAction, IWorldPosition, IWorldChangeAudio, IWorldChangeGraphics
{
    /** Current world */
    public World world;
    /** Center coords of the blast */
    public double x, y, z;
    /** Size of the explosive */
    public double size = 1;
    /** Energy cost to damage each block */
    public float eUnitPerBlock = 5F;
    /** Used to stop the explosion mid action */
    public boolean killExplosion = false;

    /** Cause of the explosion */
    public TriggerCause cause = new TriggerCause.TriggerCauseRedstone(ForgeDirection.UNKNOWN, 15);
    private NBTTagCompound additionBlastData;

    public Blast()
    {
    }

    public Blast(final World world, int x, int y, int z, int size)
    {
        setLocation(world, x, y, z);
        setYield(size);
    }

    public B setLocation(final World world, double x, double y, double z)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        return (B) this;
    }

    public B setYield(double size)
    {
        this.size = size;
        return (B) this;
    }

    public B setEnergyPerBlock(float f)
    {
        this.eUnitPerBlock = f;
        return (B) this;
    }

    public B setCause(final TriggerCause cause)
    {
        this.cause = cause;
        return (B) this;
    }

    /**
     * Sets the custome NBT data to be used by the explosive
     *
     * @param additionBlastData
     */
    public B setAdditionBlastData(NBTTagCompound additionBlastData)
    {
        this.additionBlastData = additionBlastData;
        return (B) this;
    }

    /** Custom NBT data provided by the explosive */
    public NBTTagCompound getAdditionBlastData()
    {
        return additionBlastData;
    }

    @Override
    public int shouldThreadAction()
    {
        return size > 4 ? 20 : -1;
    }

    @Override
    public List<IWorldEdit> getEffectedBlocks()
    {
        List<IWorldEdit> list = new ArrayList<IWorldEdit>();
        getEffectedBlocks(list);
        return list;
    }

    /**
     * Called by {@link #getEffectedBlocks()} to make processing
     * a list of blocks eaiser.
     *
     * @param list - list to add changes too.
     */
    public void getEffectedBlocks(final List<IWorldEdit> list)
    {

    }

    @Override
    public void handleBlockPlacement(final IWorldEdit vec)
    {
        if (vec != null && vec.hasChanged())
        {
            if (vec instanceof BlockEdit && ((BlockEdit) vec).doItemDrop)
            {
                //Get drops before setting blocks due
                List<ItemStack> items = ((BlockEdit) vec).getDrops(getFortuneModifierForBlockDrop());
                if (vec.place() == BlockEditResult.PLACED)
                {
                    //Drop items only if block was set correctly
                    for (ItemStack item : items)
                    {
                        //sanity checks, rare but do trigger sometimes on poorly coded mods
                        if (item != null && item.getItem() != null && item.stackSize > 0)
                        {
                            InventoryUtility.dropItemStack(world, ((BlockEdit) vec).xi(), ((BlockEdit) vec).yi(), ((BlockEdit) vec).zi(), item, 2, 1); //TODO increase random by power of the blast
                        }
                    }
                }
            }
            else
            {
                vec.place();
            }
        }
    }

    /**
     * Amount of fortune bonus for items harvest from blocks
     *
     * @return value equal to or greater than zero
     */
    protected int getFortuneModifierForBlockDrop()
    {
        return 0;
    }

    @Override
    public void doEffectOther(boolean beforeBlocksPlaced)
    {

    }

    protected void damageEntities(List<Entity> entities, DamageSource source)
    {
        damageEntities(entities, source, 1);
    }

    protected void damageEntities(List<Entity> entities, DamageSource source, float damageScale)
    {
        Vec3 vec3 = Vec3.createVectorHelper(x, y, z);
        for (Entity entity : entities)
        {
            double distanceScaled = entity.getDistance(x, y, z) / size;

            if (distanceScaled <= 1.0D)
            {
                double deltaX = entity.posX - x;
                double deltaY = entity.posY + (double) entity.getEyeHeight() - y;
                double deltaZ = entity.posZ - z;
                double mag = (double) MathHelper.sqrt_double(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                if (mag != 0.0D)
                {
                    deltaX /= mag;
                    deltaY /= mag;
                    deltaZ /= mag;
                    double blockDensity = (double) this.world.getBlockDensity(vec3, entity.boundingBox);
                    double force = (1.0D - distanceScaled) * blockDensity;
                    if (source != null)
                    {
                        entity.attackEntityFrom(source, (float) (((force * force + force) / 16 * size) * damageScale + 1.0D));
                    }
                    double pushPercentage = EnchantmentProtection.func_92092_a(entity, force);
                    entity.motionX += deltaX * pushPercentage;
                    entity.motionY += deltaY * pushPercentage;
                    entity.motionZ += deltaZ * pushPercentage;
                }
            }
        }
    }

    @Override
    public void killAction(boolean willSave)
    {
        this.killExplosion = true;
    }

    /**
     * Checks if the world action should be killed
     *
     * @return true if the action should be killed
     */
    protected boolean shouldKillAction()
    {
        return killExplosion || world == null || world.provider == null || DimensionManager.getWorld(world.provider.dimensionId) == null;
    }

    @Override
    public World world()
    {
        return world;
    }

    @Override
    public double x()
    {
        return x;
    }

    @Override
    public double y()
    {
        return y;
    }

    @Override
    public double z()
    {
        return z;
    }


    @Override
    public void playAudioForEdit(IWorldEdit blocks)
    {
        //TODO randomly play block audio
    }

    @Override
    public void doStartAudio()
    {
        //TODO get custom audio
        if (!world.isRemote)
        {
            world.playSoundEffect(x, y, z, "random.explode", 4.0F, (float) ((1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * size));
        }
    }

    @Override
    public void doEndAudio()
    {
    }

    /** See {@link RenderGlobal#doSpawnParticle(String, double, double, double, double, double, double)} for a list of vanilla particles */
    @Override
    public void displayEffectForEdit(IWorldEdit blocks)
    {

        //TODO randomize for large explosives to reduce lag
        //TODO add config to disable effect spawning on both server and client
        //TODO add config syncing to ensure server doesn't send render packets when not used by client
        if (!world.isRemote)
        {
            //Generate random position near block
            double posX = (double) ((float) blocks.x() + world.rand.nextFloat());
            double posY = (double) ((float) blocks.y() + world.rand.nextFloat());
            double posZ = (double) ((float) blocks.z() + world.rand.nextFloat());

            Pos pos = randomMotion(posX, posY, posZ);
            //Spawn particles
            Engine.proxy.spawnParticle("explode", world, (posX + x * 1.0D) / 2.0D, (posY + y * 1.0D) / 2.0D, (posZ + z * 1.0D) / 2.0D, pos.x(), pos.y(), pos.z());
            Engine.proxy.spawnParticle("smoke", world, posX, posY, posZ, pos.x(), pos.y(), pos.z());
        }
    }

    /**
     * Generates a randomized heading from the center of the blast away from
     * the blast.
     *
     * @param posX
     * @param posY
     * @param posZ
     * @return
     */
    protected final Pos randomMotion(double posX, double posY, double posZ)
    {
        //Get change in distance from explosive to block
        double deltaX = posX - x;
        double deltaY = posY - y;
        double deltaZ = posZ - z;

        //Convert the distance into a vector
        double mag = (double) MathHelper.sqrt_double(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        deltaX /= mag;
        deltaY /= mag;
        deltaZ /= mag;

        //Randomize speed
        double speedScale = 0.5D / (mag / size + 0.1D);
        speedScale *= (double) (world.rand.nextFloat() * world.rand.nextFloat() + 0.3F);

        //Scale direction by speed, turning it into velocity
        deltaX *= speedScale;
        deltaY *= speedScale;
        deltaZ *= speedScale;
        return new Pos(deltaX, deltaY, deltaZ);
    }

    @Override
    public void doStartDisplay()
    {
        //TODO get custom effects
        if (!world.isRemote)
        {
            if (this.size >= 2.0F)
            {
                Engine.proxy.spawnParticle("hugeexplosion", world, x, y, z, 1.0D, 0.0D, 0.0D);
            }
            else
            {
                Engine.proxy.spawnParticle("largeexplode", world, x, y, z, 1.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void doEndDisplay()
    {
        //TODO get custom effects
        if (world.isRemote)
        {
            if (this.size >= 2.0F)
            {
                Engine.proxy.spawnParticle("hugeexplosion", world, x, y, z, 1.0D, 0.0D, 0.0D);
            }
            else
            {
                Engine.proxy.spawnParticle("largeexplode", world, x, y, z, 1.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (obj.getClass() == getClass() && obj instanceof Blast)
        {
            if (((Blast) obj).world != world)
            {
                return false;
            }
            else if (((Blast) obj).x != x)
            {
                return false;
            }
            else if (((Blast) obj).y != y)
            {
                return false;
            }
            else if (((Blast) obj).z != z)
            {
                return false;
            }
            else if (((Blast) obj).size != size)
            {
                return false;
            }
            return true;
        }
        return false;
    }
}
