package net.minecraft.src.basiccomponents;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.ITextureProvider;

public class ItemElectricityMeter extends Item implements ITextureProvider
{
    public ItemElectricityMeter(int par1, int par2)
    {
        super(par1);
        this.iconIndex = par2;
        this.setItemName("Electricity Meter");
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7)
    {
    	if(!par3World.isRemote)
        {
	    	//Check to make sure the meter has electricity.
	    	TileEntity tileEntity = par3World.getBlockTileEntity(par4, par5, par6);
	        if(tileEntity != null)
	        {/*
	        	if(tileEntity instanceof TileEntityConductor)
	        	{
	        		//par2EntityPlayer.addChatMessage("Electricity Flow: "+UniversalElectricity.getWattDisplay( ((TileEntityConductor)tileEntity).getStoredElectricity())+", "+UniversalElectricity.getAmpDisplay(UniversalElectricity.getAmps( ((TileEntityConductor)tileEntity).getStoredElectricity(), ((TileEntityConductor)tileEntity).getVolts())) +", "+UniversalElectricity.getVoltDisplay(((TileEntityConductor)tileEntity).getVolts()));
	        		return true;
	        	}
	        	else if(tileEntity instanceof IElectricityConsumer)
	        	{
	        		par2EntityPlayer.addChatMessage("Electricity: "+UniversalElectricity.getWattDisplay(((IElectricityConsumer)tileEntity).getStoredElectricity())+"/"+UniversalElectricity.getWattDisplay(((IElectricityConsumer)tileEntity).getElectricityCapacity()));
	        		return true;
	        	}*/
	        }
        }
        return false;
    }
    
    @Override
	public String getTextureFile()
    {
    	return BCItem.textureFile;
    }
}
