package universalelectricity.electricity;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.implement.IElectricityReceiver;

/**
 * An easier way to implement the methods from IMachine with default values set.
 * @author Calclavia
 */
public abstract class TileEntityMachine extends TileEntity implements IElectricityReceiver
{
    protected int disabledTicks = 0;

    public TileEntityMachine()
    {
        ElectricityManager.instance.registerElectricUnit(this);
    }
    
    /**
     * Called every tick. Super this!
     */
    @Override
    public void onReceive(TileEntity entity, double ampere, double voltage, ForgeDirection side)
    {
        if (this.disabledTicks > 0)
        {
            this.disabledTicks --;
            this.whileDisable(ampere, voltage, side);
            return;
        }
    }
    
    /**
     * Called every tick while this tile entity is disabled.
     */
    protected void whileDisable(double ampere, double voltage, ForgeDirection side)
    {
		
	}

	@Override
    public void onPlayerLoggedIn(EntityPlayer player)
    {
    	
    }

    @Override
    public void onDisable(int duration)
    {
        this.disabledTicks = duration;
    }

    @Override
    public boolean isDisabled()
    {
        return this.disabledTicks > 0;
    }

    @Override
    public boolean canConnect(ForgeDirection side)
    {
        return this.canReceiveFromSide(side);
    }

    @Override
    public double getVoltage()
    {
        return 120;
    }

    /**
     * Determines if this TileEntity requires update calls.
     * As of 0.8.3, you should use tile entity ticks for your actions and use
     * onReceive for electricity reception ONLY to prevent concurrent modification
     * crashes.
     * @return True if you want updateEntity() to be called, false if not
     */
    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
    @Override
    public int getBlockMetadata()
    {
        if (this.blockMetadata == -1)
        {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        }

        return this.blockMetadata;
    }
    
    @Override
    public Block getBlockType()
    {
        if (this.blockType == null)
        {
            this.blockType = Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
        }

        return this.blockType;
    }
}
