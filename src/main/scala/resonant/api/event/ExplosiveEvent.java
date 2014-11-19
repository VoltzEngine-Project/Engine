package resonant.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import resonant.api.explosion.IExplosive;
import resonant.api.explosion.IExplosiveBlast;
import resonant.api.explosion.Trigger;
import resonant.lib.transform.vector.Vector3;

import java.util.Collection;

/**
 * Created by robert on 11/18/2014.
 */
public class ExplosiveEvent extends WorldEvent
{
    public final Vector3 vec;
    public final IExplosive ex;

    public ExplosiveEvent(World world, Vector3 vec, IExplosive ex)
    {
        super(world);
        this.vec = vec;
        this.ex = ex;
    }

    /**
     * Called before a blast instance is created. Used to cancel the blast before its even created
     */
    @Cancelable
    public static class OnExplosiveTriggeredEvent extends ExplosiveEvent
    {
        public final Trigger trigger;

        public OnExplosiveTriggeredEvent(World world, Vector3 vec, IExplosive ex, Trigger trigger)
        {
            super(world, vec, ex);
            this.trigger = trigger;
        }
    }

    /**
     * Called after the blast is created but before anything is calculated
     */
    @Cancelable
    public static class OnBlastCreatedEvent extends ExplosiveEvent
    {
        public final Trigger trigger;
        public final IExplosiveBlast blast;

        public OnBlastCreatedEvent(World world, Vector3 vec, IExplosive ex, Trigger trigger, IExplosiveBlast blast)
        {
            super(world, vec, ex);
            this.trigger = trigger;
            this.blast = blast;
        }
    }

    /**
     * Make sure your usage of the event is thread safe as this may be called
     * inside the world or outside of the world thread
     *
     * Called after the blast has calculated its effect area. Populates a list
     * of vector3 that can be modified.
     */
    public static class BlocksEffectedExplosiveEvent extends ExplosiveEvent
    {
        public final Trigger trigger;
        public final IExplosiveBlast blast;
        public final Collection<Vector3> list;

        public BlocksEffectedExplosiveEvent(World world, Vector3 vec, IExplosive ex, Trigger trigger, IExplosiveBlast blast, Collection<Vector3> list)
        {
            super(world, vec, ex);
            this.trigger = trigger;
            this.blast = blast;
            this.list = list;
        }
    }
}
