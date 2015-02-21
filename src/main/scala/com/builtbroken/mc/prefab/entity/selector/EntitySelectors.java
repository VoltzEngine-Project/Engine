package com.builtbroken.mc.prefab.entity.selector;

import net.minecraft.command.IEntitySelector;

/** Enum of simple and commonly used selectors when sorting entities lists
 * Created by robert on 2/10/2015.
 */
public enum EntitySelectors
{
    /*
    * Selects any entity that implements IMob
    */
    MOB_SELECTOR(new EntityLivingSelector().selectMobs()),
    /**
     * Selects all instances of EntityLivngBase
     */
    LIVING_SELECTOR(new EntityLivingSelector().selectLiving()),
    /**
     * Selects any entity that implements IAnimals
     */
    ANIMAL_SELECTOR(new EntityLivingSelector().selectAnimals()),
    /**
     * Selects all non-creative players, and fake players
     */
    PLAYER_SELECTOR(new EntityLivingSelector().selectPlayers()),
    /**
     * Selects all players including creative mode players, and fake players
     */
    PLAYER_CREATIVE_SELECTOR(new EntityLivingSelector().selectPlayers().selectCreative().ignoreDamage()),
    /**
     * Selects all instances of EntityMinecraft
     */
    CART_SELECTOR(new EntityLivingSelector().selectCarts()),
    /**
     * Selects all instances of EntityItem
     */
    ITEM_SELECTOR(new EntityItemSelector()),
    /**
     * Selects all instances of IProjectile
     */
    PROJECTILE_SELECTOR(new EntityLivingSelector().selectProjectiles());

    private final EntitySelector selector;

    private EntitySelectors(EntitySelector selector)
    {
        this.selector = selector;
        this.selector.lock();
    }

    public EntitySelector selector()
    {
        return selector;
    }

    public static EntitySelectors get(int selector)
    {
        if(selector >= 0 && selector < values().length)
        {
            return values()[selector];
        }
        return LIVING_SELECTOR;
    }
}
