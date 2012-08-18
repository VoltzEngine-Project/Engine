package universalelectricity.basiccomponents;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemBC extends Item
{
    public ItemBC(String name, int id, int texture)
    {
        super(id);
        this.iconIndex = texture;
        this.setItemName(name);
        this.setTabToDisplayOn(CreativeTabs.tabRedstone);
        LanguageRegistry.addName(this, name);
    }

    @Override
    public String getTextureFile()
    {
        return BasicComponents.ITEM_TEXTURE_FILE;
    }
}