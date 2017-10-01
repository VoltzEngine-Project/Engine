package com.builtbroken.mc.framework.entity.effect.effects;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.entity.effect.EntityEffect;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.prefab.entity.damage.DamageBleeding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Causes the player to slowly lose HP
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/30/2017.
 */
public class BleedingEffect extends EntityEffect
{
    public float damage;
    public int duration;
    public Object source;
    public List<BleedingEffect> subEffects = new ArrayList();

    public BleedingEffect(Entity entity)
    {
        super(References.DOMAIN, "bleeding");
        this.entity = entity;
        this.world = Engine.getWorld(entity.worldObj.provider.dimensionId);
    }

    public void setDamageValues(Object source, float damage, int duration)
    {
        this.source = source;
        this.damage = damage;
        this.duration = duration;
    }

    @Override
    public boolean onWorldTick()
    {
        if (this.entity instanceof EntityLivingBase && this.entity.isEntityAlive() && !this.entity.isEntityInvulnerable())
        {
            EntityLivingBase entity = (EntityLivingBase) this.entity;
            boolean kill = super.onWorldTick();

            //Damage
            float hp = entity.getHealth();
            DamageSource damageSource = source != null ? new DamageBleeding(source) : new DamageBleeding();

            //Remove HP silently to bypass resistance checks and avoid hurt animation being triggered
            if (hp - damage > 0)
            {
                //Fire event allow damage to be blocked
                if (ForgeHooks.onLivingAttack(entity, damageSource, damage))
                {
                    return false;
                }
                entity.setHealth(hp - damage);
            }
            //If entity is almost dead, fire normal damage to trigger death correctly
            else
            {
                entity.hurtResistantTime = 0;
                entity.attackEntityFrom(damageSource, damage);
            }

            //Iterator over sub effects
            Iterator<BleedingEffect> it = subEffects.iterator();
            while (it.hasNext())
            {
                BleedingEffect next = it.next();
                //Trigger sub effect
                if (next.onWorldTick())
                {
                    //done
                    it.remove();
                }
                //Replace current with effect
                else if (kill)
                {
                    kill = false;
                    damage = next.damage;
                    duration = next.duration;
                    source = next.source;
                    //sub effects should not have nested effects
                    it.remove(); //Remove as it has become current
                }
            }

            //TODO trigger event to render blood drops and play audio
            world.newEffect("bleeding", new Pos(entity.posX, entity.posY + (entity.height / 2), entity.posZ)).send(); //TODO get wound location for better effect
            return kill || duration <= tick;
        }
        return true;
    }

    @Override
    public void merge(EntityEffect entityEffect)
    {
        if (entityEffect.getClass() == getClass())
        {
            BleedingEffect effect = (BleedingEffect) entityEffect;
            subEffects.add(effect);
        }
    }

    @Override
    public void init(Entity entity, World world)
    {
        super.init(entity, world);
        for (BleedingEffect effect : subEffects)
        {
            effect.init(entity, world);
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound compound)
    {
        compound.setInteger("duration", duration);
        compound.setFloat("damage", damage);
        //TODO save sub effects
        //TODO save source
        return super.save(compound);
    }

    @Override
    public void load(NBTTagCompound compound)
    {
        super.load(compound);
        duration = compound.getInteger("duration");
        damage = compound.getFloat("damage");
    }
}
