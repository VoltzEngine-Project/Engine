package com.builtbroken.mc.framework.entity.effect;

import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.abstraction.world.IWorld;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Similar to MC potion system allowing for time based effects to be applied to an entity
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/30/2017.
 */
public abstract class EntityEffect implements ISave
{
    /** Unique id of the effect, normally prefix with mod ID */
    public final String id;
    /** mod that owns the effect */
    public final String mod;

    /** Is the effect alive */
    public boolean isAlive = true;
    /** Current life tick of the effect */
    public int tick = 0;

    /** Entity to modify for the effect */
    public Entity entity;
    /** Entity's world */
    public IWorld world;

    public EntityEffect(String mod, String id)
    {
        this.mod = mod;
        this.id = id;
    }


    /**
     * Called each tick on the entity
     *
     * @return true to remove the effect
     */
    public boolean onWorldTick()
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
        this.world = Engine.getWorld(world.provider.dimensionId);
    }

    /**
     * Called to merge two effects. Only
     * merged effects if they can actually stack.
     * If they can't stack store effect as sub-object
     * and tick via {@link #onWorldTick()}
     *
     * @param entityEffect
     */
    public abstract void merge(EntityEffect entityEffect);

    /**
     * Unlocalized name of the effect
     *
     * @return
     */
    public String getUnlocalizedName()
    {
        return "entity.effect." + mod + ":" + id + ".name";
    }

    /**
     * Display name of the effect
     *
     * @return
     */
    public String getDisplayName()
    {
        String translation = LanguageUtility.getLocal(getUnlocalizedName());
        if (translation != null)
        {
            translation = translation.trim();
            return !translation.isEmpty() ? translation : getUnlocalizedName();
        }
        return getUnlocalizedName();
    }

    protected boolean isServer()
    {
        return !world.isServer();
    }

    protected boolean isClient()
    {
        return world.isClient();
    }
}
