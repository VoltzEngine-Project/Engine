package resonant.lib.prefab.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;

public class ItemBase extends ItemTooltip
{
    public ItemBase(int id, String name, Configuration config, String prefix, CreativeTabs tab)
    {
        super(config.getItem(name, id).getInt());
        this.setUnlocalizedName(prefix + name);
        this.setCreativeTab(tab);
        this.setTextureName(prefix + name);
    }
}
