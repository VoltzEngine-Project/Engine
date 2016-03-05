package com.builtbroken.mc.lib.world.radar;


import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/5/2016.
 */
public class RadarTile extends RadarObject<TileEntity>
{
    public RadarTile(TileEntity referent)
    {
        super(referent);
    }

    @Override
    public World world()
    {
        if (reference != null && reference.get() != null)
        {
            return reference.get().getWorldObj();
        }
        return null;
    }

    @Override
    public double x()
    {
        if (reference != null && reference.get() != null)
        {
            return reference.get().xCoord + 0.5;
        }
        return 0;
    }

    @Override
    public double y()
    {
        if (reference != null && reference.get() != null)
        {
            return reference.get().yCoord + 0.5;
        }
        return 0;
    }

    @Override
    public double z()
    {
        if (reference != null && reference.get() != null)
        {
            return reference.get().zCoord + 0.5;
        }
        return 0;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof TileEntity && reference.get() != null)
        {
            //No need to compare world as radar contacts should only be stored per world
            return new Pos((TileEntity) object).equals(new Pos(reference.get()));
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        if (reference.get() != null)
        {
            return reference.get().hashCode();
        }
        return super.hashCode();
    }
}
