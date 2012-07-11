package universalelectricity.extend;

import net.minecraft.server.TileEntity;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.electricity.IElectricUnit;


/**
 * The Class TileEntityConductor.
 */
public abstract class TileEntityConductor extends TileEntity
{
    
    /** The connection id. */
    public int connectionID;
    
    /** The connected blocks. */
    public TileEntity connectedBlocks[] =
    {
        null, null, null, null, null, null
    };

    /**
     * Instantiates a new tile entity conductor.
     */
    public TileEntityConductor()
    {
        connectionID = 0;
        reset();
    }

    /**
     * Update connection.
     *
     * @param tileentity the tileentity
     * @param byte0 the byte0
     */
    public void updateConnection(TileEntity tileentity, byte byte0)
    {
        if ((tileentity instanceof TileEntityConductor) || (tileentity instanceof IElectricUnit))
        {
            connectedBlocks[byte0] = tileentity;

            if (tileentity instanceof TileEntityConductor)
            {
                ElectricityManager.mergeConnection(connectionID, ((TileEntityConductor)tileentity).connectionID);
            }
        }
        else
        {
            if (connectedBlocks[byte0] != null && (connectedBlocks[byte0] instanceof TileEntityConductor))
            {
                ElectricityManager.splitConnection(this, (TileEntityConductor)connectedBlocks[byte0]);
            }

            connectedBlocks[byte0] = null;
        }
    }

    /**
     * Update connection without split.
     *
     * @param tileentity the tileentity
     * @param byte0 the byte0
     */
    public void updateConnectionWithoutSplit(TileEntity tileentity, byte byte0)
    {
        if ((tileentity instanceof TileEntityConductor) || (tileentity instanceof IElectricUnit))
        {
            connectedBlocks[byte0] = tileentity;

            if (tileentity instanceof TileEntityConductor)
            {
                ElectricityManager.mergeConnection(connectionID, ((TileEntityConductor)tileentity).connectionID);
            }
        }
        else
        {
            connectedBlocks[byte0] = null;
        }
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.TileEntity#canUpdate()
     */
    public boolean canUpdate()
    {
        refreshConnectedBlocks();
        return false;
    }

    /**
     * Reset.
     */
    public void reset()
    {
        connectionID = 0;
        ElectricityManager.registerConductor(this);
    }

    /**
     * Refresh connected blocks.
     */
    public void refreshConnectedBlocks()
    {
        if (world != null)
        {
            BlockConductor.updateConductorTileEntity(world, x, y, z);
        }
    }

    /**
     * Gets the resistance.
     *
     * @return the resistance
     */
    public abstract double getResistance();
}
