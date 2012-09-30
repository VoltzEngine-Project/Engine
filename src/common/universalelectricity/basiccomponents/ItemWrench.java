package universalelectricity.basiccomponents;

import ic2.api.IWrenchable;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import railcraft.common.api.core.items.ICrowbar;
import buildcraft.api.tools.IToolWrench;

public class ItemWrench extends ItemBasic implements ICrowbar, IToolWrench
{
    public ItemWrench(int par1, int par2)
    {
        super("Universal Wrench", par1, par2);
        this.setIconIndex(par2);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) 
    {
    	TileEntity tileEntity = world.getBlockTileEntity(x,  y, z);
    	
    	if(tileEntity != null)
    	{
	    	if(tileEntity instanceof IWrenchable)
	    	{
	    		IWrenchable wrenchableTile = (IWrenchable)tileEntity;
	
	    		if(entityPlayer.isSneaking())
	    	    {
	    			side = ForgeDirection.getOrientation(side).getOpposite().ordinal();
	    	    }
	    		
	    		if(wrenchableTile.wrenchCanSetFacing(entityPlayer, side)) 
	    		{
	    	        wrenchableTile.setFacing((short)side);	
	    	        return true;
	    		}
	    		
	    		if(wrenchableTile.wrenchCanRemove(entityPlayer)) 
	    		{
	    			Block block = Block.blocksList[world.getBlockId(x, y, z)];
	    			
	    			if(block != null)
	    			{
	    				block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
	    			}
	    		}
	    	}
    	}
    	
        return false;
    }

	@Override
	public boolean canWrench(EntityPlayer player, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer player, int x, int y, int z)
	{
		
	}
}
