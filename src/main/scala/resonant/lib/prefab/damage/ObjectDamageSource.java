package resonant.lib.prefab.damage;

import resonant.lib.prefab.CustomDamageSource;

/** Damage source classed designed for things that are not entity based.
 * 
 * @author Darkguardsman */
@Deprecated
public class ObjectDamageSource extends CustomDamageSource
{
    public ObjectDamageSource(String name, Object attacker)
    {
        super(name, attacker);
    }

    public static ObjectDamageSource doBulletDamage(Object source)
    {
        return (ObjectDamageSource) new ObjectDamageSource("Bullet", source).setProjectile();
    }

    public static ObjectDamageSource doLaserDamage(Object source)
    {
        return (ObjectDamageSource) new ObjectDamageSource("Laser", source).setProjectile();
    }
}
