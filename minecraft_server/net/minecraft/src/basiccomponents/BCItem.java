package net.minecraft.src.basiccomponents;

import net.minecraft.src.Item;
import net.minecraft.src.ModLoader;

public class BCItem extends Item
{
    public static String textureFile = BasicComponents.FILE_PATH + "items.png";

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
        return textureFile;
    }
}
