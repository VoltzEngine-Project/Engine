package com.builtbroken.lib.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import com.builtbroken.api.TriggerCause;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import com.builtbroken.lib.transform.vector.VectorWorld;
import com.builtbroken.lib.world.edit.IWorldChangeAction;
import com.builtbroken.lib.world.edit.BlockEdit;

import java.util.Collection;

/**
 * Prefab for all events triggered by IWorldChangeAction
 * Created by robert on 12/2/2014.
 */
public abstract class WorldChangeActionEvent extends WorldEvent
{
    public final TriggerCause triggerCause;
    public final IWorldChangeAction worldChangeAction;

    public WorldChangeActionEvent(World world, IWorldChangeAction worldChangeAction, TriggerCause triggerCause)
    {
        super(world);
        this.triggerCause = triggerCause;
        this.worldChangeAction = worldChangeAction;
    }

    /**
     * Called when the action is created and has yet to be triggered. This is the time
     * to change the params of the action, or cancel the action.
     */
    @Cancelable
    public static class ActionCreated extends WorldChangeActionEvent
    {
        public final VectorWorld startingPoint;
        public ActionCreated(VectorWorld v, IWorldChangeAction worldChangeAction, TriggerCause triggerCause)
        {
            super(v.world(), worldChangeAction, triggerCause);
            startingPoint = v;
        }
    }

    /**
     * Called after all changes have been made to the world and the action is done
     */
    public static class PostWorldChangeEvent extends WorldChangeActionEvent
    {
        public final VectorWorld startingPoint;
        public PostWorldChangeEvent(VectorWorld v, IWorldChangeAction worldChangeAction, TriggerCause triggerCause)
        {
            super(v.world(), worldChangeAction, triggerCause);
            startingPoint = v;
        }
    }

    /**
     * Event to allow mods to override or remove changed blocks from the list of
     * blocks that will be effected by IWorldChangeAction instance
     */
    public static class FinishedCalculatingEffectEvent extends WorldChangeActionEvent
    {
        public final VectorWorld startingPoint;
        public final Collection<BlockEdit> blocks;
        public FinishedCalculatingEffectEvent(VectorWorld v, Collection<BlockEdit> blocks, IWorldChangeAction worldChangeAction, TriggerCause triggerCause)
        {
            super(v.world(), worldChangeAction, triggerCause);
            this.startingPoint = v;
            this.blocks = blocks;
        }
    }
}
