package com.builtbroken.mc.framework.blast.emp;

import com.builtbroken.mc.prefab.explosive.AbstractExplosiveHandler;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/30/2016.
 */
public class ExEmp extends AbstractExplosiveHandler<BlastEMP>
{
    public ExEmp()
    {
        super("Emp", 10);
    }

    @Override
    protected BlastEMP newBlast()
    {
        return new BlastEMP(this);
    }
}
