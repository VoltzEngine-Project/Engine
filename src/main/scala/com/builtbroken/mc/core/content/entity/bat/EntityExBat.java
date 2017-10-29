package com.builtbroken.mc.core.content.entity.bat;

import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.world.World;

/**
 * Explosive version of the bat for use in {@link com.builtbroken.mc.api.explosive.IExplosiveHandler} for mods like ICBM
 * and Armory mod. That introduce odd weapons modeled after real life weapons developed by USA during WWII. That used
 * bats with fire bombs to catch buildings on fire. https://en.wikipedia.org/wiki/Bat_bomb
 * <p>
 * This is included as part of Voltz Engine for reuse by other mods. This way each mod is not recreating the entity
 * wasting resources.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2017.
 */
public class EntityExBat extends EntityBat implements IMob
{
    /** Point for the bat to fly towards */
    public IPos3D target;

    public EntityExBat(World p_i1680_1_)
    {
        super(p_i1680_1_);
    }
}
