package com.builtbroken.mc.framework.logic;

import com.builtbroken.mc.framework.logic.imp.ITileNodeHost;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.imp.transform.vector.Pos;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
public class TileNode implements ITileNode
{
    protected ITileNodeHost host;

    @Override
    public void setHost(ITileNodeHost host)
    {
        this.host = host;
    }

    @Override
    public ITileNodeHost getHost()
    {
        return host;
    }

    //=============================================
    //============== to string ====================
    //=============================================

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getClassDisplayName());
        builder.append("(");
        toStringData(builder);
        builder.append(")@");
        builder.append(hashCode());
        return builder.toString();
    }

    /**
     * Gets the debug display name of the class.
     *
     * @return name
     */
    protected String getClassDisplayName()
    {
        return getClass().getName();
    }

    /**
     * Called to build data about the object.
     *
     * @param builder - builder to append data to
     */
    protected void toStringData(StringBuilder builder)
    {
        builder.append("host = ");
        //only do host name@hash to prevent inf loop
        //      Host has 'this' in its toString()
        builder.append(host.getClass().getName());
        builder.append("@");
        builder.append(host.hashCode());
    }

    //=============================================
    //========== Position data ====================
    //=============================================

    protected final Pos toPos()
    {
        return new Pos(x(), y(), z());
    }

    protected final Location toLocation()
    {
        return new Location(world(), x(), y(), z());
    }

    //=============================================
    //========== Save Code     ====================
    //=============================================

    @Override
    public void load(NBTTagCompound nbt)
    {

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        return nbt;
    }
}
