package universalelectricity.basiccomponents;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemBCOre extends ItemBlock
{
    private String[] ores = {"Copper Ore", "Tin Ore"};

	public ItemBCOre(int id)
    {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
    	return (new StringBuilder())
                .append(super.getItemName())
                .append(".")
                .append(this.ores [itemstack.getItemDamage()])
                .toString();
    }
}
