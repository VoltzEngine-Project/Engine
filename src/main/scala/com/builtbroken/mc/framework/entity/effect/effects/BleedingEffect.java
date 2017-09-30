package com.builtbroken.mc.framework.entity.effect.effects;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.entity.effect.EntityEffect;
import com.builtbroken.mc.prefab.entity.damage.DamageBleeding;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

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

    public BleedingEffect(Object source, float damage, int duration)
    {
        super(References.DOMAIN, "bleeding");
        this.source = source;
        this.damage = damage;
        this.duration = duration;
    }

    @Override
    public boolean update()
    {
        boolean kill = super.update();

        //Damage
        entity.attackEntityFrom(new DamageBleeding(source), damage);

        //Iterator over sub effects
        Iterator<BleedingEffect> it = subEffects.iterator();
        while (it.hasNext())
        {
            BleedingEffect next = it.next();
            //Trigger sub effect
            if (next.update())
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
        //TODO render blood drops
        return kill || duration <= tick;
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
