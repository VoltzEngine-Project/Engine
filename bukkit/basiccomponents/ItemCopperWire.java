package basiccomponents;

import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;


/**
 * The Class ItemCopperWire.
 */
public class ItemCopperWire extends BCItem
{
    
    /** The spawn id. */
    private int spawnID;

    /**
     * Instantiates a new item copper wire.
     *
     * @param i the i
     * @param j the j
     */
    public ItemCopperWire(int i, int j)
    {
        super("Copper Wire", i, j);
        spawnID = BasicComponents.BlockCopperWire.id;
        setMaxDurability(0);
        a(true);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     *
     * @param itemstack the itemstack
     * @param entityhuman the entityhuman
     * @param world the world
     * @param i the i
     * @param j the j
     * @param k the k
     * @param l the l
     * @return true, if successful
     */
    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l)
    {
        int i1 = world.getTypeId(i, j, k);

        if (i1 == Block.SNOW.id)
        {
            l = 1;
        }
        else if (i1 != Block.VINE.id && i1 != Block.LONG_GRASS.id && i1 != Block.DEAD_BUSH.id)
        {
            if (l == 0)
            {
                j--;
            }

            if (l == 1)
            {
                j++;
            }

            if (l == 2)
            {
                k--;
            }

            if (l == 3)
            {
                k++;
            }

            if (l == 4)
            {
                i--;
            }

            if (l == 5)
            {
                i++;
            }
        }

        if (world.mayPlace(spawnID, i, j, k, false, l))
        {
            if (world.setTypeId(i, j, k, spawnID))
            {
                if (world.getTypeId(i, j, k) == spawnID)
                {
                    Block.byId[spawnID].postPlace(world, i, j, k, l);
                    Block.byId[spawnID].postPlace(world, i, j, k, entityhuman);
                }

                itemstack.count--;
                return true;
            }
        }

        return false;
    }
}
