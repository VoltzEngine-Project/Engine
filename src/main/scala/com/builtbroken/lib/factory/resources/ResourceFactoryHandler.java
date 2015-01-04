package com.builtbroken.lib.factory.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import com.builtbroken.lib.factory.FactoryHandler;
import com.builtbroken.mod.References;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Factory handler for all ore based item generation for Resonant Engine.
 * Doesn't automaticly generate all of its items without being triggered by a sub mod.
 * Based on Calclavia's ResourceGenerator from Resonant Induction in MC 1.6.4 which it replaces
 *
 * @author Darkguardsman
 */
public class ResourceFactoryHandler extends FactoryHandler
{

	public ResourceFactoryHandler()
	{
		super(References.ID);
	}

    @Deprecated
	public void generateAll()
	{

	}

    public Block getMixture(String name)
    {
        return null;
    }

    public String getName(ItemStack item)
    {
        return "";
    }
	/**
	 * Gets the average color of this item.
	 *
	 * @param itemStack
	 * @return The RGB hexadecimal color code.
	 */
	@SideOnly(Side.CLIENT)
	public int getAverageColor(ItemStack itemStack)
	{
		int totalR = 0;
		int totalG = 0;
		int totalB = 0;
		int colorCount = 0;
		Item item = itemStack.getItem();
		try
		{
			IIcon icon = item.getIconIndex(itemStack);

			//if (iconColorCache.containsKey(icon))
			//{
			//	return iconColorCache.get(icon);
			//}

			String iconString = icon.getIconName();
			if (iconString != null && !iconString.contains("MISSING_ICON_ITEM"))
			{
				iconString = iconString.contains(":") ? iconString.replace(":", ":" + References.ITEM_TEXTURE_DIRECTORY) : References.ITEM_TEXTURE_DIRECTORY + iconString + ".png";
				ResourceLocation textureLocation = new ResourceLocation(iconString);
				InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(textureLocation).getInputStream();
				BufferedImage bufferedImage = ImageIO.read(inputStream);
				int width = bufferedImage.getWidth();
				int height = bufferedImage.getWidth();

				/**
				 * Read every single pixel of the texture.
				 */
				for (int x = 0; x < width; x++)
				{
					for (int y = 0; y < height; y++)
					{
						Color rgb = new Color(bufferedImage.getRGB(x, y));
						double luma = 0.2126 * rgb.getRed() + 0.7152 * rgb.getGreen() + 0.0722 * rgb.getBlue();
						if (luma > 40)
						{
							totalR += rgb.getRed();
							totalG += rgb.getGreen();
							totalB += rgb.getBlue();
							colorCount += 1;
						}
					}
				}
			}
			if (colorCount > 0)
			{
				totalR /= colorCount;
				totalG /= colorCount;
				totalB /= colorCount;
				//iconColorCache.put(icon, averageColor);
				return new Color(totalR, totalG, totalB).brighter().getRGB();
			}
		}
		catch (Exception e)
		{
			System.out.println(References.NAME + ": Failed to compute colors for: " + item);
			e.printStackTrace();
		}
		return 0xFFFFFF;
	}
}
