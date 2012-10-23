package basiccomponents.item;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import basiccomponents.BasicComponents;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemBasic extends Item
{
    public ItemBasic(String name, int id, int texture, CreativeTabs creativeTab)
    {
        super(id);
        this.setIconIndex(texture);
        this.setItemName(name);
        this.setCreativeTab(creativeTab);
        LanguageRegistry.addName(this, name);
    }
    
    public ItemBasic(String name, int id, int texture)
    {
    	this(name, id, texture, CreativeTabs.tabMaterials);
    }

    @Override
    public String getTextureFile()
    {
        return BasicComponents.ITEM_TEXTURE_FILE;
    }
}