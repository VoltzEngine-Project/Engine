package basiccomponents;

import java.util.ArrayList;

import net.minecraft.server.ItemStack;


/**
 * The Class ItemCircuit.
 */
public class ItemCircuit extends BCItem
{
    
    /** The names. */
    private String names[] =
    {
        "Basic Circuit", "Adavanced Circuit", "Elite Circuit"
    };

    /**
     * Instantiates a new item circuit.
     *
     * @param i the i
     * @param j the j
     */
    public ItemCircuit(int i, int j)
    {
        super("Circuit", i, j);
        setMaxDurability(0);
        a(true);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place.
     *
     * @param i the i
     * @return the int
     */
    public int filterData(int i)
    {
        return i;
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.Item#a(net.minecraft.server.ItemStack)
     */
    public String a(ItemStack itemstack)
    {
        return names[itemstack.getData()];
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.Item#addCreativeItems(java.util.ArrayList)
     */
    public void addCreativeItems(ArrayList arraylist)
    {
        for (int i = 0; i < names.length; i++)
        {
            arraylist.add(new ItemStack(this, 1, i));
        }
    }
}
