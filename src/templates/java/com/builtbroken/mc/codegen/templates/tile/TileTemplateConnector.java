package com.builtbroken.mc.codegen.templates.tile;

import com.builtbroken.mc.api.tile.ConnectionType;
import com.builtbroken.mc.api.tile.ITileConnection;
import com.builtbroken.mc.api.tile.node.ITileNode;
import com.builtbroken.mc.framework.logic.wrapper.TileEntityWrapper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/17/2017.
 */
@TileWrappedTemplate(annotationName = "ConnectorWrapped")
public class TileTemplateConnector extends TileEntityWrapper implements ITileConnection
{
    /**
     * @param controller - tile, passed in by child class wrapper
     */
    public TileTemplateConnector(ITileNode controller)
    {
        super(controller);
    }

    //#StartMethods#
    @Override
    public boolean canConnect(TileEntity connection, ConnectionType type, ForgeDirection from)
    {
        if (getTileNode() instanceof ITileConnection)
        {
            return ((ITileConnection) getTileNode()).canConnect(connection, type, from);
        }
        return false;
    }

    @Override
    public boolean hasConnection(ConnectionType type, ForgeDirection side)
    {
        if (getTileNode() instanceof ITileConnection)
        {
            return ((ITileConnection) getTileNode()).hasConnection(type, side);
        }
        return false;
    }
    //#EndMethods#
}
