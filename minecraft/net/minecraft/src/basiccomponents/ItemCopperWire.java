package net.minecraft.src.basiccomponents;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemCopperWire extends BCItem
{
    private int spawnID = BasicComponents.BlockCopperWire.blockID;

    public ItemCopperWire(int id, int texture)
    {
        super("Copper Wire", id, texture);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    @Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7)
    {
    	int blockID = par3World.getBlockId(par4, par5, par6);
    	
    	if (blockID == Block.snow.blockID)
        {
            par7 = 1;
        }
        else if (blockID != Block.vine.blockID && blockID != Block.tallGrass.blockID && blockID != Block.deadBush.blockID)
        {
            if (par7 == 0)
            {
                --par5;
            }

            if (par7 == 1)
            {
                ++par5;
            }

            if (par7 == 2)
            {
                --par6;
            }

            if (par7 == 3)
            {
                ++par6;
            }

            if (par7 == 4)
            {
                --par4;
            }

            if (par7 == 5)
            {
                ++par4;
            }
        }
    	
        if (par3World.canBlockBePlacedAt(this.spawnID, par4, par5, par6, false, par7))
        {
            Block var9 = Block.blocksList[this.spawnID];

            if (par3World.setBlockWithNotify(par4, par5, par6, this.spawnID))
            {
                if (par3World.getBlockId(par4, par5, par6) == this.spawnID)
                {
                    Block.blocksList[this.spawnID].onBlockPlaced(par3World, par4, par5, par6, par7);
                    Block.blocksList[this.spawnID].onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer);
                }

                --par1ItemStack.stackSize;
                
                return true;
            }
        }

        return false;
    }

}
