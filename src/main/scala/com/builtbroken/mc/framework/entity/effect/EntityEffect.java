package com.builtbroken.mc.framework.entity.effect;

import com.builtbroken.mc.api.ISave;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Similar to MC potion system allowing for time based effects to be applied to an entity
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/30/2017.
 */
public class EntityEffect  implements ISave
{
    public final String id;

    public boolean isAlive = true;
    public int ticks = 0;

    public Entity entity;
    public World world;

    protected final List<EntityEffect> effects = new ArrayList();

    public EntityEffect(String id)
    {
        this.id = id;
    }


    /**
     * Called each tick on the entity
     *
     * @return true to remove the effect
     */
    public boolean update()
    {
        return !isAlive;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound compound)
    {
        return compound;
    }

    @Override
    public void load(NBTTagCompound compound)
    {

    }

    public void init(Entity entity, World world)
    {
        this.entity = entity;
        this.world = world;
    }
}
