package com.builtbroken.mc.framework.blast.emp;

import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/5/2017.
 */
public class BlastEMPClient extends BlastEMP
{
    public BlastEMPClient(IExplosiveHandler handler)
    {
        super(handler);
    }

    @Override
    public void doStartDisplay()
    {
        //TODO emp wave, light and hard to see as second wave will be the main wave
    }

    @Override
    public void doEndDisplay()
    {
        //TODO second emp wave, but faster
    }

    @Override
    public void displayEffectForEdit(IWorldEdit blocks)
    {
        if (!world.isRemote)
        {
            //TODO render sparks around machine that was drained
            //TODO render parts flying around machine that was destroyed
        }
    }
}
