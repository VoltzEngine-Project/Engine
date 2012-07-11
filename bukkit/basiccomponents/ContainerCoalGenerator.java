package basiccomponents;

import net.minecraft.server.Container;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Slot;


/**
 * The Class ContainerCoalGenerator.
 */
public class ContainerCoalGenerator extends Container
{
    
    /** The tile entity. */
    private TileEntityCoalGenerator tileEntity;
    
    /** The player. */
    private EntityHuman player;

    /**
     * Instantiates a new container coal generator.
     *
     * @param playerinventory the playerinventory
     * @param tileentitycoalgenerator the tileentitycoalgenerator
     */
    public ContainerCoalGenerator(PlayerInventory playerinventory, TileEntityCoalGenerator tileentitycoalgenerator)
    {    	
    	player = playerinventory.player;
        tileEntity = tileentitycoalgenerator;
        a(new Slot(tileentitycoalgenerator, 0, 33, 34));

        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                a(new Slot(playerinventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            a(new Slot(playerinventory, j, 8 + j * 18, 142));
        }
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.Container#b(net.minecraft.server.EntityHuman)
     */
    public boolean b(EntityHuman entityhuman)
    {
        return tileEntity.a(entityhuman);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     *
     * @param i the i
     * @return the item stack
     */
    public ItemStack a(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)e.get(i);

        if (slot != null && slot.c())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.cloneItemStack();

            if (i != 0)
            {
                if (itemstack1.id == Item.COAL.id)
                {
                    if (!a(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (i >= 30 && i < 37 && !a(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!a(itemstack1, 3, 37, false))
            {
                return null;
            }

            if (itemstack1.count == 0)
            {
                slot.set((ItemStack)null);
            }
            else
            {
                slot.d();
            }

            if (itemstack1.count == itemstack.count)
            {
                return null;
            }

            slot.c(itemstack1);
        }

        return itemstack;
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.server.Container#getPlayer()
     */
    public EntityHuman getPlayer() {
    	return player;
    }
     
    /* (non-Javadoc)
     * @see net.minecraft.server.Container#getInventory()
     */
    public IInventory getInventory() {
    	return tileEntity;
    }
}
