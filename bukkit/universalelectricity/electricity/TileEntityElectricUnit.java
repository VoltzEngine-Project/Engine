package universalelectricity.electricity;

import net.minecraft.server.Block;
import net.minecraft.server.TileEntity;


/**
 * The Class TileEntityElectricUnit.
 */
public abstract class TileEntityElectricUnit extends TileEntity implements IElectricUnit
{
    
    /** The disabled ticks. */
    protected int disabledTicks;

    /**
     * Instantiates a new tile entity electric unit.
     */
    public TileEntityElectricUnit()
    {
        disabledTicks = 0;
    }

    /* (non-Javadoc)
     * @see universalelectricity.extend.IDisableable#onDisable(int)
     */
    public void onDisable(int i)
    {
        disabledTicks = i;
    }

    /* (non-Javadoc)
     * @see universalelectricity.extend.IDisableable#isDisabled()
     */
    public boolean isDisabled()
    {
        return disabledTicks > 0;
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.IElectricUnit#onUpdate(float, float, byte)
     */
    public void onUpdate(float f, float f1, byte byte0)
    {
        if (disabledTicks > 0)
        {
            disabledTicks -= getTickInterval();
            return;
        }
        else
        {
            return;
        }
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.IElectricUnit#canConnect(byte)
     */
    public boolean canConnect(byte byte0)
    {
        return canReceiveFromSide(byte0);
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.IElectricUnit#getVoltage()
     */
    public float getVoltage()
    {
        return 120F;
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.IElectricUnit#getTickInterval()
     */
    public int getTickInterval()
    {
        return 1;
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.TileEntity#canUpdate()
     */
    public boolean canUpdate()
    {
        return false;
    }

    /**
     * Gets the block type.
     *
     * @return the block type
     */
    public Block getBlockType()
    {
        if (q == null)
        {
            q = Block.byId[world.getTypeId(x, y, z)];
        }

        return q;
    }

    /**
     * Returns block data at the location of this entity (client-only).
     *
     * @return the int
     */
    public int k()
    {
        if (p == -1)
        {
            p = world.getData(x, y, z);
        }

        return p;
    }
}
