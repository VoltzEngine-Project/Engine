package net.minecraft.src.universalelectricity.components;

import net.minecraft.src.Item;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.ITextureProvider;

public class UCItem extends Item implements ITextureProvider
{
	public static String textureFile = UniversalComponents.filePath+"items.png";
	
	public UCItem(String name, int id, int texture)
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
