package com.builtbroken.mc.core.content.blast.holiday;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/24/2017.
 */
public class ExGiftClient extends ExGift
{
    @Override
    protected BlastGift newBlast()
    {
        return new BlastGiftClient(this);
    }
}
