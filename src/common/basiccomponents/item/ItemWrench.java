package basiccomponents.item;

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
    public ItemWrench(int id, int texture)
    {
        super("Universal Wrench", id, texture);
        this.setMaxStackSize(1);
        this.setMaxDamage(2000);
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
	    	        this.wrenchUsed(entityPlayer, x, y, z);
	    	        return true;
	    		}
	    		
	    		if(wrenchableTile.wrenchCanRemove(entityPlayer)) 
	    		{
	    			Block block = Block.blocksList[world.getBlockId(x, y, z)];
	    			
	    			if(block != null)
	    			{
	    				block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
	    			}
	    			
	    			this.wrenchUsed(entityPlayer, x, y, z);
	    			return true;
	    		}
	    	}
    	}
    	
        return false;
    }

	@Override
	public boolean canWrench(EntityPlayer entityPlayer, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer entityPlayer, int x, int y, int z)
	{
		if(entityPlayer.getCurrentEquippedItem() != null)
		{
			if(entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemWrench)
			{
				entityPlayer.getCurrentEquippedItem().damageItem(1, entityPlayer);
			}
		}
	}
}
