package com.builtbroken.mc.prefab.entity.selector;

/**
 * Enum of simple and commonly used selectors when sorting entities lists
 * Created by robert on 2/10/2015.
 */
public enum EntitySelectors
{
    /*
    * Selects any entity that implements IMob
    */
    MOB_SELECTOR("Monsters", new EntityLivingSelector().selectMobs()),
    /**
     * Selects all instances of EntityLivngBase
     */
    LIVING_SELECTOR("Living", new EntityLivingSelector().selectLiving()),
    /**
     * Selects any entity that implements IAnimals
     */
    ANIMAL_SELECTOR("Animals", new EntityLivingSelector().selectAnimals()),
    /**
     * Selects all non-creative players, and fake players
     */
    PLAYER_SELECTOR("Players", new EntityLivingSelector().selectPlayers()),
    /**
     * Selects all players including creative mode players, and fake players
     */
    PLAYER_CREATIVE_SELECTOR("Creative Players", new EntityLivingSelector().selectPlayers().selectCreative().ignoreDamage()),
    /**
     * Selects all instances of EntityMinecraft
     */
    CART_SELECTOR("Carts", new EntityLivingSelector().selectCarts()),
    /**
     * Selects all instances of EntityItem
     */
    ITEM_SELECTOR("Items", new EntityItemSelector()),
    /**
     * Selects all instances of IProjectile
     */
    PROJECTILE_SELECTOR("Projectiles", new EntityLivingSelector().selectProjectiles()),
    /**
     * Selects all XP orbs
     */
    XP_SELECTOR("Xp Orbs", new EntityXpSelector());

    private final EntitySelector selector;
    private final String DISPLAY_NAME;

    EntitySelectors(String name, EntitySelector selector)
    {
        this.DISPLAY_NAME = name;
        this.selector = selector;
        this.selector.lock();
    }

    public EntitySelector selector()
    {
        return selector;
    }

    public static EntitySelectors get(int selector)
    {
        if (selector >= 0 && selector < values().length)
        {
            return values()[selector];
        }
        return LIVING_SELECTOR;
    }

    public String displayName()
    {
        return DISPLAY_NAME;
    }
}
