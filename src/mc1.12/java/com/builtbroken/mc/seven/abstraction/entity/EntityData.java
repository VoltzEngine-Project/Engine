package com.builtbroken.mc.seven.abstraction.entity;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.abstraction.entity.IEntityData;
import com.builtbroken.mc.api.abstraction.world.IPosWorld;
import com.builtbroken.mc.api.abstraction.world.IWorld;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.imp.transform.vector.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.UUID;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public class EntityData implements IEntityData
{
    private Entity entity; //TODO store in weak ref

    public EntityData(Entity entity)
    {
        this.entity = entity;
    }

    public Entity getEntity()
    {
        return entity;
    }

    public boolean isValid()
    {
        return getEntity() != null && getEntity().isEntityAlive();
    }

    @Override
    public double x()
    {
        return getEntity() != null ? getEntity().posX : 0;
    }

    @Override
    public double y()
    {
        return getEntity() != null ? getEntity().posY : 0;
    }

    @Override
    public double z()
    {
        return getEntity() != null ? getEntity().posZ : 0;
    }

    @Override
    public IWorld world()
    {
        return Engine.getWorld(entity.world.provider.getDimension());
    }

    @Override
    public IPos3D toPosition()
    {
        return new Pos(x(), y(), z());
    }

    @Override
    public IPosWorld toWorldPosition()
    {
        return null;
    }

    @Override
    public boolean isPlayer()
    {
        return getEntity() instanceof EntityPlayer;
    }

    @Override
    public String getUniqueName()
    {
        return getEntity().getName();
    }

    @Override
    public UUID getUniqueID()
    {
        if (isPlayer())
        {
            return ((EntityPlayer) getEntity()).getGameProfile().getId();
        }
        return getEntity().getUniqueID();
    }

    @Override
    public double getYOffset()
    {
        return getEntity().getYOffset();
    }

    @Override
    public ItemStack getRightClickItem()
    {
        if(getEntity() instanceof EntityPlayer)
        {
            return ((EntityPlayer) getEntity()).getHeldItem(EnumHand.OFF_HAND);
        }
        return null;
    }

    @Override
    public double yaw()
    {
        return getEntity().rotationYaw;
    }

    @Override
    public double pitch()
    {
        return getEntity().rotationPitch;
    }

    @Override
    public double roll()
    {
        return 0;
    }

    @Override
    public Entity unwrap()
    {
        return entity;
    }
}
