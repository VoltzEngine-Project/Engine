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
    public final World world;
    /** Handler for changes */
    protected final IWorldChangeAction blast;
    /** Number of edits per tick */
    protected final int editsPerTick;

    public WorldEditQueue(World world, IWorldChangeAction blast, int edits)
    {
        this.world = world;
        this.blast = blast;
        this.editsPerTick = edits;
    }

    public WorldEditQueue(World world, IWorldChangeAction blast, int edits, Collection<IWorldEdit> c)
    {
        super(c);
        this.world = world;
        this.editsPerTick = edits;
        this.blast = blast;
    }

    public void runQue(World world, Side side)
    {
        if (world == this.world)
        {
            try
            {
                Iterator<IWorldEdit> it = iterator();
                int c = 0;
                while (it.hasNext() && c++ <= editsPerTick)
                {
                    IWorldEdit edit = it.next();
                    if(edit != null)
                    {
                        try
                        {
                            if (!world.isRemote)
                            {
                                blast.handleBlockPlacement(edit);
                            }
                            if (blast instanceof IWorldChangeAudio)
                            {
                                ((IWorldChangeAudio) blast).playAudioForEdit(edit);
                            }
                            if (blast instanceof IWorldChangeGraphics)
                            {
                                ((IWorldChangeGraphics) blast).displayEffectForEdit(edit);
                            }
                        }
                        catch (Exception e)
                        {
                            Engine.instance.logger().error("Failed to place block for change action"
                                            + "\nSide: " + side
                                            + "\nChangeAction: " + blast
                                            + "\nEdit: " + edit
                                    , e);
                        }
                    }
                    it.remove();
                }
                if (isEmpty())
                {
                    if (blast instanceof IWorldChangeAudio)
                    {
                        ((IWorldChangeAudio) blast).doEndAudio();
                    }
                    if (blast instanceof IWorldChangeGraphics)
                    {
                        ((IWorldChangeGraphics) blast).doEndDisplay();
                    }
                }

            }
            catch (Exception e)
            {
                Engine.instance.logger().error("Crash while processing world change " + blast, e);
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
        return "WorldEditQueue[" + blast + ", " + editsPerTick + ", " + hashCode() + "]";
    }
}
