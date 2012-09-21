package universalelectricity.prefab;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;
import universalelectricity.implement.IDisableable;

/**
 * An easier way to implement the methods from IElectricityDisableable with default values set.
 * @author Calclavia
 */
public abstract class TileEntityDisableable extends TileEntity implements IDisableable
{
    protected int disabledTicks = 0;

    @Override
    public void updateEntity()
    {
    	 if(this.disabledTicks > 0)
         {
             this.disabledTicks --;
             this.whileDisable();
             return;
         }
    }
    
    /**
     * Called every tick while this tile entity is disabled.
     */
    protected void whileDisable()
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
