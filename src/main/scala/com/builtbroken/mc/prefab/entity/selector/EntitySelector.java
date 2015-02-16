package com.builtbroken.mc.prefab.entity.selector;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.lib.transform.region.Cube;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Very basic entity selector designed for use with World.selectEntitiesWithinAABB or Entity AI target selectors.
 * Created by robert on 2/10/2015.
 */
public class EntitySelector implements IEntitySelector
{
    /**
     * Prevents values from changing when called from the setters
     */
    protected boolean lock = false;

    @Override
    public boolean isEntityApplicable(Entity entity)
    {
        return entity != null;
    }

    /**
     * Prevents mistakenly changing the prefab static selectors
     */
    public EntitySelector lock()
    {
        this.lock = true;
        return this;
    }

    public List<Entity> getEntities(IWorldPosition location, double radius)
    {
        return getEntities(location.world(), location.x(), location.y(), location.z(), radius);
    }

    public List<Entity> getEntities(Entity entity, double radius)
    {
        //TODO maybe center based on entity collision bounds or size
        return getEntities(entity.worldObj, entity.posX, entity.posY, entity.posZ, radius);
    }

    public List<Entity> getEntities(World world, double x, double y, double z, double radius)
    {
        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
        return getEntities(world, bb);
    }

    public List<Entity> getEntities(World world, Cube cube)
    {
        return world == null || cube == null ? new ArrayList() : getEntities(world, cube.toAABB());
    }

    public List<Entity> getEntities(World world, AxisAlignedBB bb)
    {
        return world.selectEntitiesWithinAABB(Entity.class, bb, this);
    }
}
