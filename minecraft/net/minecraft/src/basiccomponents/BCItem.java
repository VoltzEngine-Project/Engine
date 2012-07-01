package net.minecraft.src.basiccomponents;

import net.minecraft.src.Item;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.ITextureProvider;

public class BCItem extends Item implements ITextureProvider
{
	public static String textureFile = BasicComponents.filePath+"items.png";
	
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
