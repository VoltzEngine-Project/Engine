package com.builtbroken.mc.framework.explosive.blast;

import com.builtbroken.jlib.data.network.IByteBufReader;
import com.builtbroken.jlib.data.network.IByteBufWriter;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldChangeAudio;
import com.builtbroken.mc.api.edit.IWorldChangeGraphics;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.event.blast.BlastEventBlockEdit;
import com.builtbroken.mc.api.event.blast.BlastEventBlockRemoved;
import com.builtbroken.mc.api.event.blast.BlastEventBlockReplaced;
import com.builtbroken.mc.api.explosive.IBlast;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import com.builtbroken.mc.prefab.explosive.blast.BlastBasic;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.WorldEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Prefab for implement explosive blast actions
 */
public abstract class Blast<B extends Blast> implements IWorldChangeAction, IWorldPosition, IWorldChangeAudio, IWorldChangeGraphics, IBlast, IByteBufReader, IByteBufWriter
{
    /** Current world */
    public World world;
    /** Center coords of the blast */
    public double x, y, z;

    /** Center of the blast by block location, is not exact center but is used for block pathing to get distance */
    public Location blockCenter;

    /** Size of the explosive */
    public double size = 1;
    /** Energy cost to damage each block */
    public float eUnitPerBlock = 5F;
    /** Used to stop the explosion mid action */
    public boolean killExplosion = false;

    /** Cause of the explosion */
    public TriggerCause cause = new TriggerCause.TriggerCauseRedstone(ForgeDirection.UNKNOWN, 15);
    private NBTTagCompound additionBlastData;

    public final IExplosiveHandler explosiveHandler;

    /**
     * Entity to pass into methods when destroying blocks or attacking entities
     */
    public Entity explosionBlameEntity;
    /**
     * Explosion wrapper for block methods
     */
    public Explosion wrapperExplosion;

    public List<PostBlastTrigger> explosivesToTriggerAfter = new ArrayList();

    public Blast(IExplosiveHandler handler)
    {
        this.explosiveHandler = handler;
        this.wrapperExplosion = new BlastBasic.WrapperExplosion(this);
        if (!Engine.isJUnitTest())
        {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public Blast(IExplosiveHandler handler, final World world, int x, int y, int z, int size)
    {
        this(handler);
        setLocation(world, x, y, z);
        setYield(size);
    }

    public PostBlastTrigger addPostTriggerExplosive(String explosiveID, double size, TriggerCause triggerCause, NBTTagCompound data)
    {
        IExplosiveHandler handler = ExplosiveRegistry.get(explosiveID);
        if (handler != null)
        {
            PostBlastTrigger blastTrigger = new PostBlastTrigger(handler, size, triggerCause, data);
            explosivesToTriggerAfter.add(blastTrigger);
            return blastTrigger;
        }
        return null;
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        if (event.world == world)
        {
            killAction(false);
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    public B setLocation(final World world, double x, double y, double z)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        blockCenter = new Location(world, Math.floor(x) + 0.5, Math.floor(y) + 0.5, Math.floor(z) + 0.5);
        return (B) this;
    }

    @Override
    public B setYield(double size)
    {
        this.size = size;
        return (B) this;
    }

    @Override
    public double getYield()
    {
        return size;
    }

    @Override
    public B setEnergyPerBlock(float f)
    {
        this.eUnitPerBlock = f;
        return (B) this;
    }

    @Override
    public B setCause(TriggerCause cause)
    {
        this.cause = cause;
        if (cause != null)
        {
            //Create entity to check for blast resistance values on blocks
            if (cause instanceof TriggerCause.TriggerCauseEntity)
            {
                explosionBlameEntity = ((TriggerCause.TriggerCauseEntity) cause).source;
            }
            if (explosionBlameEntity == null)
            {
                explosionBlameEntity = new EntityTNTPrimed(world);
                explosionBlameEntity.setPosition(x, y, z);
            }
        }
        return (B) this;
    }

    @Override
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
        return size > 4 ? -2 : -1;
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
        if (vec != null && vec.hasChanged() && prePlace(vec))
        {
            vec.place();
            postPlace(vec);
        }
    }

    /**
     * Called to do post block placement events
     *
     * @param vec
     */
    protected void postPlace(final IWorldEdit vec)
    {
        if (vec.getNewBlock() == Blocks.air)
        {
            MinecraftForge.EVENT_BUS.post(new BlastEventBlockRemoved.Post(this, world, vec.getBlock(), vec.getBlockMetadata(), (int) vec.x(), (int) vec.y(), (int) vec.z()));
        }
        else
        {
            MinecraftForge.EVENT_BUS.post(new BlastEventBlockReplaced.Post(this, world, vec.getBlock(), vec.getBlockMetadata(), vec.getNewBlock(), vec.getNewMeta(), (int) vec.x(), (int) vec.y(), (int) vec.z()));
        }
    }

    /**
     * Called before block placement to check that
     * the placement is not canceled by an event
     *
     * @param vec - edit data, null and has changed checked
     * @return true if should continue
     */
    protected boolean prePlace(final IWorldEdit vec)
    {
        BlastEventBlockEdit event;
        if (vec.getNewBlock() == Blocks.air)
        {
            event = new BlastEventBlockRemoved.Pre(this, world, vec.getBlock(), vec.getBlockMetadata(), (int) vec.x(), (int) vec.y(), (int) vec.z());
        }
        else
        {
            event = new BlastEventBlockReplaced.Pre(this, world, vec.getBlock(), vec.getBlockMetadata(), vec.getNewBlock(), vec.getNewMeta(), (int) vec.x(), (int) vec.y(), (int) vec.z());
        }

        boolean result = MinecraftForge.EVENT_BUS.post(event);
        if (vec instanceof BlockEdit && event instanceof BlastEventBlockReplaced.Pre)
        {
            ((BlockEdit) vec).set(((BlastEventBlockReplaced.Pre) event).newBlock, ((BlastEventBlockReplaced.Pre) event).newMeta);
        }
        return !result;
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
        if (!beforeBlocksPlaced)
        {
            for (PostBlastTrigger handler : explosivesToTriggerAfter)
            {
                if (handler != null)
                {
                    handler.triggerExplosive(toLocation());
                }
            }
        }
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
        return killExplosion || world == null || world.provider == null || DimensionManager.getWorld(world.provider.dimensionId) == null || !toLocation().isChunkLoaded();
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
            Engine.minecraft.spawnParticle("explode", world, (posX + x * 1.0D) / 2.0D, (posY + y * 1.0D) / 2.0D, (posZ + z * 1.0D) / 2.0D, pos.x(), pos.y(), pos.z());
            Engine.minecraft.spawnParticle("smoke", world, posX, posY, posZ, pos.x(), pos.y(), pos.z());
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
                Engine.minecraft.spawnParticle("hugeexplosion", world, x, y, z, 1.0D, 0.0D, 0.0D);
            }
            else
            {
                Engine.minecraft.spawnParticle("largeexplode", world, x, y, z, 1.0D, 0.0D, 0.0D);
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
                Engine.minecraft.spawnParticle("hugeexplosion", world, x, y, z, 1.0D, 0.0D, 0.0D);
            }
            else
            {
                Engine.minecraft.spawnParticle("largeexplode", world, x, y, z, 1.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean isCompleted()
    {
        return world == null || killExplosion;
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

    @Override
    public B readBytes(ByteBuf buf)
    {
        //size and explosive data are already synced
        return (B) this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf buf)
    {
        //size and explosive data are already synced
        return buf;
    }


    /**
     * Used to wrapper the blast into a minecraft explosion data object
     */
    public static class WrapperExplosion extends Explosion
    {
        public final Blast blast;

        public WrapperExplosion(Blast blast)
        {
            super(blast.world(), blast.explosionBlameEntity, blast.x(), blast.y(), blast.z(), (float) blast.size);
            this.blast = blast;
        }
    }
}
