package com.builtbroken.mc.core.content.blast.holiday;

import com.builtbroken.mc.framework.explosive.handler.ExplosiveHandler;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/24/2017.
 */
public class ExGift extends ExplosiveHandler<BlastGift>
{
    public ExGift()
    {
        super("Gift", 1);
    }

    @Override
    protected BlastGift newBlast()
    {
        return new BlastGift(this);
    }
}
