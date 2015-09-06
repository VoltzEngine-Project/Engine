package com.builtbroken.mc.prefab.entity.selector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by robert on 2/10/2015.
 */
public class EntityLivingSelector extends EntitySelector
{
    protected boolean select_mobs = false;
    protected boolean select_animals = false;
    protected boolean select_players = false;
    protected boolean select_creative = false;
    protected boolean select_living = false;
    protected boolean select_living_base = false;
    protected boolean select_projectiles = false;
    protected boolean select_carts = false;
    protected boolean ignore_can_damage = false;

    @Override
    public boolean isEntityApplicable(Entity entity)
    {
        if (!entity.isDead && (ignore_can_damage || !entity.isEntityInvulnerable()))
        {
            if (select_living_base && entity instanceof EntityLivingBase)
            {
                return true;
            }

            if (select_living && entity instanceof EntityLiving)
            {
                return true;
            }

            if (select_mobs && entity instanceof IMob)
            {
                return true;
            }

            if(select_projectiles && entity instanceof IProjectile)
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

    public EntityLivingSelector selectMobs()
    {
        if (!lock)
            this.select_mobs = true;
        return this;
    }

    public EntityLivingSelector selectAnimals()
    {
        if (!lock)
            this.select_animals = true;
        return this;
    }

    public EntityLivingSelector selectPlayers()
    {
        if (!lock)
            this.select_players = true;
        return this;
    }

    public EntityLivingSelector selectCreative()
    {
        if (!lock)
            this.select_creative = true;
        return this;
    }

    public EntityLivingSelector selectLiving()
    {
        if (!lock)
            this.select_living = true;
        return this;
    }

    public EntityLivingSelector selectLivingBase()
    {
        if (!lock)
            this.select_living_base = true;
        return this;
    }

    public EntityLivingSelector selectProjectiles()
    {
        if (!lock)
            this.select_projectiles = true;
        return this;
    }

    public EntityLivingSelector selectCarts()
    {
        if (!lock)
            this.select_carts = true;
        return this;
    }

    /**
     * Ignore if the entity can't be damaged
     */
    public EntityLivingSelector ignoreDamage()
    {
        if (!lock)
            this.ignore_can_damage = true;
        return this;
    }
}
