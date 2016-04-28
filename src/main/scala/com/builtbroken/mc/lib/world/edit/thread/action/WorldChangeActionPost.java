package com.builtbroken.mc.lib.world.edit.thread.action;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.process.IWorldAction;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.world.World;

/**
 * Only used to call post methods for a world change action after it has finished placing blocks in it's threaded environment.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class WorldChangeActionPost implements IWorldAction
{
    private boolean done = false;
    private final IWorldChangeAction action;

    public WorldChangeActionPost(IWorldChangeAction action)
    {
        this.action = action;
    }

    @Override
    public void runQue(World world, Side side)
    {
        if (!world.isRemote)
        {
            action.doEffectOther(false);
            done = true;
        }
    }

    @Override
    public boolean isQueDone()
    {
        return done;
    }
}
