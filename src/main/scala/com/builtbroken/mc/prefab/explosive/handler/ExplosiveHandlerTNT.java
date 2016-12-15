package com.builtbroken.mc.prefab.explosive.handler;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.prefab.explosive.AbstractExplosiveHandler;
import com.builtbroken.mc.prefab.explosive.blast.BlastBasic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Preloaded handler for {@link net.minecraft.init.Blocks#tnt} based explosives. Also used for
 * gunpowder explosives as well. But both can be considered the same thing in vanilla minecraft code.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/12/2016.
 */
public class ExplosiveHandlerTNT extends AbstractExplosiveHandler
{
    public ExplosiveHandlerTNT()
    {
        super("tnt");
    }

    @Override
    public IWorldChangeAction createBlastForTrigger(World world, double x, double y, double z, TriggerCause triggerCause, double size, NBTTagCompound tag)
    {
        return new BlastBasic(this).setLocation(world, x, y, z).setCause(triggerCause).setYield(size).setAdditionBlastData(tag);
    }
}
