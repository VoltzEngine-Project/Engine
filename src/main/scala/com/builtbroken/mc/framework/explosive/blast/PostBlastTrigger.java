package com.builtbroken.mc.framework.explosive.blast;

import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to trigger blast code after a blast has finished running
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2017.
 */
public class PostBlastTrigger
{
    public final IExplosiveHandler handler;
    public final NBTTagCompound data;
    public final TriggerCause cause;

    public final double size;

    public final List<PostBlastTrigger> postsToAdd = new ArrayList();

    public PostBlastTrigger(IExplosiveHandler handler, double size, TriggerCause triggerCause, NBTTagCompound data)
    {
        this.handler = handler;
        this.cause = triggerCause;
        this.data = data;
        this.size = size;
    }

    public void triggerExplosive(Location location)
    {
        ExplosiveRegistry.triggerExplosive(location, handler, cause, size, data);
    }

    public PostBlastTrigger addPostTriggerExplosive(String explosiveID, double v, TriggerCause triggerCause, NBTTagCompound tagCompound)
    {
        IExplosiveHandler handler = ExplosiveRegistry.get(explosiveID);
        if (handler != null)
        {
            PostBlastTrigger blastTrigger = new PostBlastTrigger(handler, size, triggerCause, data);
            postsToAdd.add(blastTrigger);
            return blastTrigger;
        }
        return null;
    }
}
