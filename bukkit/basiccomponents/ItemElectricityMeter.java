package basiccomponents;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import forge.ITextureProvider;


/**
 * The Class ItemElectricityMeter.
 */
public class ItemElectricityMeter extends Item implements ITextureProvider
{
    
    /**
     * Instantiates a new item electricity meter.
     *
     * @param i the i
     * @param j the j
     */
    public ItemElectricityMeter(int i, int j)
    {
        super(i);
        textureId = j;
        a("Electricity Meter");
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
        if (!world.isStatic)
        {
            net.minecraft.server.TileEntity tileentity = world.getTileEntity(i, j, k);

            if (tileentity == null);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.Item#getTextureFile()
     */
    public String getTextureFile()
    {
        return BCItem.textureFile;
    }
}
