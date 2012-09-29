package universalelectricity.prefab;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;

public abstract class AdvancedTile extends TileEntity
{
	protected boolean initiate = true;
	
	@Override
    public void updateEntity()
    {
    	if(this.initiate)
    	{
    		this.initiate();
    		this.initiate = false;
    	}
    }
	
	protected void initiate() { }
	
	@Override
	public void invalidate()
    {
		this.initiate = false;
		super.invalidate();
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
