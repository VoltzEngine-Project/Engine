package calclavia.lib.prefab.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;

/** Damage source classed designed for things that are not entity based.
 * 
 * @author Darkguardsman */
public class ObjectDamageSource extends DamageSource
{
    protected Object damageSource;

    public ObjectDamageSource(String damageName, Object attacker)
    {
        super(damageName);
        this.damageSource = attacker;
    }

    @Override
    public Entity getEntity()
    {
        return damageSource instanceof Entity ? ((Entity) damageSource) : null;
    }

    public TileEntity getTileEntity()
    {
        return damageSource instanceof TileEntity ? ((TileEntity) damageSource) : null;
    }

    public Object attacker()
    {
        return damageSource;
    }

    @Override
    public boolean isDifficultyScaled()
    {
        return this.damageSource != null && this.damageSource instanceof EntityLiving && !(this.damageSource instanceof EntityPlayer);
    }

    @Override
    public ObjectDamageSource setProjectile()
    {
        super.setProjectile();
        return this;
    }

    public static ObjectDamageSource doBulletDamage(Object source)
    {
        return new ObjectDamageSource("Bullet", source).setProjectile();
    }

    public static ObjectDamageSource doLaserDamage(Object source)
    {
        return new ObjectDamageSource("Laser", source).setProjectile();
    }
}
