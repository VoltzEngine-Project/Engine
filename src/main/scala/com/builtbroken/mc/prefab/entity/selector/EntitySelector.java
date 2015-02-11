package com.builtbroken.mc.prefab.entity.selector;

import com.builtbroken.mc.lib.transform.region.Cuboid;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
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
     * Selects any entity that implements IMob
     */
    public static final EntitySelector MOB_SELECTOR = new EntitySelector().selectMobs().lock();
    /**
     * Selects all instances of EntityLivngBase
     */
    public static final EntitySelector LIVING_SELECTOR = new EntitySelector().selectLiving().lock();
    /**
     * Selects any entity that implements IAnimals
     */
    public static final EntitySelector ANIMAL_SELECTOR = new EntitySelector().selectAnimals().lock();
    /**
     * Selects all non-creative players, and fake players
     */
    public static final EntitySelector PLAYER_SELECTOR = new EntitySelector().selectPlayers().lock();
    /**
     * Selects all players including creative mode players, and fake players
     */
    public static final EntitySelector PLAYER_CREATIVE_SELECTOR = new EntitySelector().selectPlayers().selectCreative().lock();
    /**
     * Selects all instances of EntityMinecraft
     */
    public static final EntitySelector CART_SELECTOR = new EntitySelector().selectCarts().lock();

    protected boolean select_mobs = false;
    protected boolean select_animals = false;
    protected boolean select_players = false;
    protected boolean select_creative = false;
    protected boolean select_living = false;
    protected boolean select_carts = false;
    protected boolean ignore_can_damage = false;

    /**
     * Prevents values from changing when called from the setters
     */
    private boolean lock = false;

    @Override
    public boolean isEntityApplicable(Entity entity)
    {
        if (!entity.isDead && (ignore_can_damage || !entity.isEntityInvulnerable()))
        {
            if (select_living && entity instanceof EntityLivingBase)
            {
                return true;
            }

            if (select_mobs && entity instanceof IMob)
            {
                return true;
            }

            if (select_animals && entity instanceof IAnimals)
            {
                return true;
            }

            if (select_players && entity instanceof EntityPlayer)
            {
                return !select_creative || ((EntityPlayer) entity).capabilities.isCreativeMode;
            }

            if (select_carts && entity instanceof EntityMinecart)
            {
                return true;
            }
        }
        return false;
    }

    public EntitySelector selectMobs()
    {
        if (!lock)
            this.select_mobs = true;
        return this;
    }

    public EntitySelector selectAnimals()
    {
        if (!lock)
            this.select_animals = true;
        return this;
    }

    public EntitySelector selectPlayers()
    {
        if (!lock)
            this.select_players = true;
        return this;
    }

    public EntitySelector selectCreative()
    {
        if (!lock)
            this.select_creative = true;
        return this;
    }

    public EntitySelector selectLiving()
    {
        if (!lock)
            this.select_living = true;
        return this;
    }

    public EntitySelector selectCarts()
    {
        if (!lock)
            this.select_carts = true;
        return this;
    }

    /**
     * Ignore if the entity can't be damaged
     */
    public EntitySelector ignoreDamage()
    {
        if (!lock)
            this.ignore_can_damage = true;
        return this;
    }

    /**
     * Prevents mistakenly changing the prefab static selectors
     */
    public EntitySelector lock()
    {
        this.lock = true;
        return this;
    }

    public List<Entity> getEntities(World world, Cuboid cube)
    {
        return world == null || cube == null ? new ArrayList() : getEntities(world, cube.toAABB());
    }

    public List<Entity> getEntities(World world, AxisAlignedBB bb)
    {
        return world.selectEntitiesWithinAABB(Entity.class, bb, this);
    }
}
