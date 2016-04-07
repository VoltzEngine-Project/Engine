package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldChangeAudio;
import com.builtbroken.mc.api.edit.IWorldChangeGraphics;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.core.Engine;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Que of block edits to be applied to the world.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/30/2015.
 */
public class WorldEditQueue extends LinkedList<IWorldEdit>
{
    /** World to modify */
    public final World world;
    /** Handler for that created the change list, and is responsible for apply changes. */
    protected final IWorldChangeAction action;
    /** Number of edits per tick, keep it low to reduce block lag */
    protected final int editsPerTick; //TODO add a check to see if is near a player, if not accelerate block placement as there will be no packets to cause lag

    /**
     * Creates a new edit que
     *
     * @param world  - world to edit
     * @param action - handler for changes made to the world
     * @param edits  - number of edits to do per tick
     */
    public WorldEditQueue(World world, IWorldChangeAction action, int edits)
    {
        this.world = world;
        this.action = action;
        this.editsPerTick = edits;
    }

    /**
     * Creates a new edit que
     *
     * @param world  - world to edit
     * @param action - handler for changes made to the world
     * @param edits  - number of edits to do per tick
     * @param c      - list of edits to apply to the world
     */
    public WorldEditQueue(World world, IWorldChangeAction action, int edits, Collection<IWorldEdit> c)
    {
        super(c);
        this.world = world;
        this.editsPerTick = edits;
        this.action = action;
    }

    /**
     * Runs the world edit que. Called each tick for each world
     *
     * @param world - world to edit blocks in, make sure it matches the attended world
     * @param side  - is client or is server
     */
    public void runQue(World world, Side side)
    {
        //TODO fix so this is not called for each world
        if (world == this.world)
        {
            try
            {
                Iterator<IWorldEdit> it = iterator();
                int c = 0;
                while (it.hasNext() && c++ <= editsPerTick)
                {
                    IWorldEdit edit = it.next();
                    if (edit != null)
                    {
                        try
                        {
                            if (!world.isRemote)
                            {
                                action.handleBlockPlacement(edit);
                            }
                            if (action instanceof IWorldChangeAudio)
                            {
                                ((IWorldChangeAudio) action).playAudioForEdit(edit);
                            }
                            if (action instanceof IWorldChangeGraphics)
                            {
                                ((IWorldChangeGraphics) action).displayEffectForEdit(edit);
                            }
                        }
                        catch (Exception e)
                        {
                            Engine.instance.logger().error("Failed to place block for change action"
                                            + "\nSide: " + side
                                            + "\nChangeAction: " + action
                                            + "\nEdit: " + edit
                                    , e);
                        }
                    }
                    it.remove();
                }
                if (isEmpty())
                {
                    if (action instanceof IWorldChangeAudio)
                    {
                        ((IWorldChangeAudio) action).doEndAudio();
                    }
                    if (action instanceof IWorldChangeGraphics)
                    {
                        ((IWorldChangeGraphics) action).doEndDisplay();
                    }
                }

            }
            catch (Exception e)
            {
                Engine.instance.logger().error("Crash while processing world change " + action, e);
            }
        }
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        //Worst case o^2
        if (object instanceof WorldEditQueue)
        {
            if (((WorldEditQueue) object).size() == size())
            {
                for (IWorldEdit edit : this)
                {
                    if (!((WorldEditQueue) object).contains(edit))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "WorldEditQueue[" + action + ", " + editsPerTick + ", " + hashCode() + "]";
    }
}
