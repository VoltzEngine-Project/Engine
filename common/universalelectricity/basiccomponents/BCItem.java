package universalelectricity.basiccomponents;

import net.minecraft.src.Item;
import net.minecraft.src.ModLoader;

public class BCItem extends Item
{
    public BCItem(String name, int id, int texture)
    {
        super(id);
        this.iconIndex = texture;
        this.setItemName(name);
        ModLoader.addName(this, name);
    }

    @Override
    public String getTextureFile()
    {
        return BasicComponents.ITEM_TEXTURE_FILE;
    }
}