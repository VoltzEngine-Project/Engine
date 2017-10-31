package com.builtbroken.mc.framework.thread.delay;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.world.World;

import java.util.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/28/2017.
 */
public class DelayedActionHandler
{
    public static Map<Integer, List<DelayedAction>> worldToActions = new HashMap();

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            final World world = event.world;
            final int dimID = world.provider.dimensionId;
            if (worldToActions.containsKey(dimID))
            {
                List<DelayedAction> actions = worldToActions.get(dimID);
                if (actions != null && actions.size() > 0)
                {
                    Iterator<DelayedAction> it = actions.iterator();
                    while (it.hasNext())
                    {
                        DelayedAction action = it.next();
                        if (action  != null && action.isAlive)
                        {
                            action.ticksToWait--;
                            if (action.ticksToWait <= 0)
                            {
                                if (action.trigger())
                                {
                                    it.remove();
                                }
                            }
                        }
                        else
                        {
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    public static <T extends DelayedAction> T add(T delayedAction)
    {
        if(delayedAction != null && delayedAction.world != null && delayedAction.world.provider != null)
        {
            List<DelayedAction> list = null;
            if (worldToActions.containsKey(delayedAction.world.provider.dimensionId))
            {
                list = worldToActions.get(delayedAction.world.provider.dimensionId);
            }

            if (list == null)
            {
                list = new ArrayList();
            }

            list.add(delayedAction);

            worldToActions.put(delayedAction.world.provider.dimensionId, list);
        }
        return delayedAction;
    }
}
