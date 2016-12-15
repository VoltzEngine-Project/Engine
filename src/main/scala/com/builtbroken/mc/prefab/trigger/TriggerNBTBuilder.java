package com.builtbroken.mc.prefab.trigger;

import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.event.TriggerCauseRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/15/2016.
 */
public class TriggerNBTBuilder implements TriggerCauseRegistry.TriggerNBTBuilder
{
    public final String id;

    public TriggerNBTBuilder(String id)
    {
        this.id = id;
    }

    @Override
    public TriggerCause buildCause(NBTTagCompound tag, World world)
    {
        switch (id)
        {
            case "fire":
                return new TriggerCause.TriggerCauseFire(ForgeDirection.getOrientation(tag.getByte("side")));
            case "redstone":
                return new TriggerCause.TriggerCauseRedstone(ForgeDirection.getOrientation(tag.getByte("side")), tag.getInteger("str"));
            case "explosion":
                Entity source = toEntity(tag.getCompoundTag("source"), world);
                double xx = tag.getDouble("xx");
                double yy = tag.getDouble("yy");
                double zz = tag.getDouble("zz");
                float size = tag.getFloat("float");
                return new TriggerCause.TriggerCauseExplosion(new Explosion(world, source, xx, yy, zz, size));
            case "entityImpactEntity":
                Entity source2 = toEntity(tag.getCompoundTag("source"), world);
                Entity entityHit = toEntity(tag.getCompoundTag("hit"), world);
                float vel = tag.getFloat("vel");
                return new TriggerCause.TriggerEntityImpact(entityHit, source2, vel);
            case "entityImpactBlock":
                Entity source3 = toEntity(tag.getCompoundTag("source"), world);
                float vel2 = tag.getFloat("vel");
                Block block = Block.getBlockById(tag.getInteger("block"));
                return new TriggerCause.TriggerBlockImpact(block, source3, vel2);
            case "entityImpact":
                Entity source4 = toEntity(tag.getCompoundTag("source"), world);
                float vel3 = tag.getFloat("vel");
                return new TriggerCause.TriggerCauseImpact(source4, vel3);
            case "entity":
                Entity source5 = toEntity(tag.getCompoundTag("source"), world);
                return new TriggerCause.TriggerCauseEntity(source5);
        }
        return null;
    }

    @Override
    public NBTTagCompound cache(NBTTagCompound tag, TriggerCause cause)
    {
        if (cause instanceof TriggerCause.TriggerCauseFire)
        {
            tag.setByte("side", (byte) ((TriggerCause.TriggerCauseFire) cause).triggeredSide.ordinal());
        }
        else if (cause instanceof TriggerCause.TriggerCauseRedstone)
        {
            tag.setByte("side", (byte) ((TriggerCause.TriggerCauseRedstone) cause).triggeredSide.ordinal());
            tag.setInteger("str", ((TriggerCause.TriggerCauseRedstone) cause).strength);
        }
        else if (cause instanceof TriggerCause.TriggerCauseExplosion)
        {
            tag.setTag("source", toNBT(((TriggerCause.TriggerCauseExplosion) cause).source.exploder));
            tag.setDouble("xx", ((TriggerCause.TriggerCauseExplosion) cause).source.explosionX);
            tag.setDouble("yy", ((TriggerCause.TriggerCauseExplosion) cause).source.explosionX);
            tag.setDouble("zz", ((TriggerCause.TriggerCauseExplosion) cause).source.explosionX);
            tag.setFloat("size", ((TriggerCause.TriggerCauseExplosion) cause).source.explosionSize);
        }
        else if (cause instanceof TriggerCause.TriggerEntityImpact)
        {
            tag.setTag("source", toNBT(((TriggerCause.TriggerEntityImpact) cause).source));
            tag.setTag("hit", toNBT(((TriggerCause.TriggerEntityImpact) cause).entityHit));
            tag.setFloat("vel", ((TriggerCause.TriggerEntityImpact) cause).velocity);
        }
        else if (cause instanceof TriggerCause.TriggerBlockImpact)
        {
            tag.setTag("source", toNBT(((TriggerCause.TriggerBlockImpact) cause).source));
            tag.setInteger("block", Block.getIdFromBlock(((TriggerCause.TriggerBlockImpact) cause).impactBlock));
            tag.setFloat("vel", ((TriggerCause.TriggerBlockImpact) cause).velocity);
        }
        else if (cause instanceof TriggerCause.TriggerCauseImpact)
        {
            tag.setTag("source", toNBT(((TriggerCause.TriggerCauseImpact) cause).source));
            tag.setFloat("vel", ((TriggerCause.TriggerCauseImpact) cause).velocity);
        }
        else if (cause instanceof TriggerCause.TriggerCauseEntity)
        {
            tag.setTag("source", toNBT(((TriggerCause.TriggerCauseEntity) cause).source));
        }
        return tag;
    }

    private NBTTagCompound toNBT(Entity entity)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("id", entity.getEntityId());
        return tag;
    }

    private Entity toEntity(NBTTagCompound tag, World world)
    {
        int id = tag.getInteger("id");
        return world.getEntityByID(id);
    }
}
