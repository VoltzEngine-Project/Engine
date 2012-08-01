package net.minecraft.src.basiccomponents;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.universalelectricity.extend.IWrenchable;

public class ItemWrench extends BCItem
{
    public ItemWrench(int par1, int par2)
    {
        super("Wrench", par1, par2);
        this.setIconIndex(par2);
        this.setMaxStackSize(1);
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int side)
    {
    	if(Block.blocksList[par3World.getBlockId(x, y, z)] instanceof IWrenchable)
    	{
    		((IWrenchable)Block.blocksList[par3World.getBlockId(x, y, z)]).onUseWrench(par3World, x, y, z, par2EntityPlayer);
    		return true;
    	}
    	
        return false;
    }
}
