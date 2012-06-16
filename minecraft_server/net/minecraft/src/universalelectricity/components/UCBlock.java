package net.minecraft.src.universalelectricity.components;

import java.util.ArrayList;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.ITextureProvider;

public class UCBlock extends Block implements ITextureProvider
{
	public static String textureFile = UniversalComponents.filePath+"blocks.png";
	
	public UCBlock(String name, int id, int textureIndex)
	{
		super(id, textureIndex, Material.rock);
		this.setBlockName(name);
		ModLoader.addName(this, name);
	}
	
	@Override
	public String getTextureFile()
	{
		return textureFile;
	}
	
	@Override
	public void addCreativeItems(ArrayList itemList)
    {
		itemList.add(new ItemStack(this, 1, 0));
    }
}
