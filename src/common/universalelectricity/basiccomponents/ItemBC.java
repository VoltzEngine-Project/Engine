package universalelectricity.basiccomponents;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemBC extends Item
{
    public ItemBC(String name, int id, int texture, CreativeTabs creativeTab)
    {
        super(id);
        this.iconIndex = texture;
        this.setItemName(name);
        this.setCreativeTab(creativeTab);
        LanguageRegistry.addName(this, name);
    }
    
    public ItemBC(String name, int id, int texture)
    {
    	this(name, id, texture, CreativeTabs.tabMaterials);
    }

    @Override
    public String getTextureFile()
    {
        return BasicComponents.ITEM_TEXTURE_FILE;
    }
}