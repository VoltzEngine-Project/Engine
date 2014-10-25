package resonant.content.factory.resources;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.oredict.OreDictionary;
import resonant.content.factory.Factory;
import resonant.content.factory.FactoryHandler;
import resonant.content.factory.resources.block.FactoryFluidMixture;
import resonant.content.factory.resources.block.FactoryFluidMolten;
import resonant.content.factory.resources.item.FactoryDust;
import resonant.content.factory.resources.item.FactoryRubble;
import resonant.engine.References;
import resonant.lib.utility.LanguageUtility;
import resonant.lib.utility.nbt.IVirtualObject;
import resonant.lib.utility.nbt.NBTUtility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Factory handler for all ore based item generation for Resonant Engine.
 * Doesn't automaticly generate all of its items without being triggered by a sub mod.
 * Based on Calclavia's ResourceGenerator from Resonant Induction in MC 1.6.4 which it replaces
 *
 * @author Darkguardsman
 */
public class ResourceFactoryHandler extends FactoryHandler implements IVirtualObject
{
	public final Set<String> materials;
	public final HashMap<String, Integer> materialColorCache;
	public final HashMap<IIcon, Integer> iconColorCache;
	public final BiMap<Integer, String> materialIds;
	public FactoryDust dustFactory;
	public FactoryRubble rubbleFactory;
	public FactoryFluidMixture mixtureFactory;
	public FactoryFluidMolten moltenFactory;
	private boolean hasGenerated = false;
	private boolean hasLoadedIDs = false;
	private int lastSetMaterialID = 0;

	public ResourceFactoryHandler()
	{
		super(References.ID);
		materials = new HashSet<String>();
		materialColorCache = new HashMap();
		iconColorCache = new HashMap();
		materialIds = HashBiMap.create();

		dustFactory = new FactoryDust(this, References.ID, References.PREFIX);
		factories.put("dust", dustFactory);

		rubbleFactory = new FactoryRubble(this, References.ID, References.PREFIX);
		factories.put("rubble", rubbleFactory);

		moltenFactory = new FactoryFluidMolten(this, References.ID, References.PREFIX);
		factories.put("moltenMetal", moltenFactory);

		mixtureFactory = new FactoryFluidMixture(this, References.ID, References.PREFIX);
		factories.put("mixtureMetal", mixtureFactory);
	}

	/**
	 * Only call server side to load the NBT file for this handler
	 */
	protected void loadIdData()
	{

	}

	public void generateAll()
	{
		if (!hasGenerated)
		{
			for (String material : materials)
			{
				//Give it a new id if it doesn't have one
				if (!materialIds.values().contains(material))
				{
					while (lastSetMaterialID >= 5000)
					{
						if (!materialIds.containsKey(lastSetMaterialID))
						{
							materialIds.put(lastSetMaterialID, material);
							break;
						}
						lastSetMaterialID++;
					}
				}
				for (Factory factory : this.factories.values())
				{
					if (factory instanceof FactoryResource)
					{
						factory.generate(material);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void oreRegisterEvent(OreDictionary.OreRegisterEvent evt)
	{
		if (evt.Name.startsWith("ingot"))
		{
			String oreDictName = evt.Name.replace("ingot", "");
			String materialName = oreDictName.substring(0, 1).toLowerCase() + oreDictName.substring(1);

			if (!materials.contains(materialName))
			{
				References.CONFIGURATION.load();
				boolean allowMaterial = References.CONFIGURATION.get("Resource-Generator", "Enable " + oreDictName, true).getBoolean(true);
				References.CONFIGURATION.save();

				if (!allowMaterial)
				{
					return;
				}

				materials.add(materialName);
			}
		}
	}

	public String moltenToMaterial(String fluidName)
	{
		return fluidNameToMaterial(fluidName, "molten");
	}

	public String materialNameToMolten(String fluidName)
	{
		return materialNameToFluid(fluidName, "molten");
	}

	public String mixtureToMaterial(String fluidName)
	{
		return fluidNameToMaterial(fluidName, "mixture");
	}

	public String materialNameToMixture(String fluidName)
	{
		return materialNameToFluid(fluidName, "mixture");
	}

	public String fluidNameToMaterial(String fluidName, String type)
	{
		return LanguageUtility.decapitalizeFirst(LanguageUtility.underscoreToCamel(fluidName).replace(type, ""));
	}

	public String materialNameToFluid(String materialName, String type)
	{
		return type + "_" + LanguageUtility.camelToLowerUnderscore(materialName);
	}

	public BlockFluidFinite getMixture(String name)
	{
		return (BlockFluidFinite) Block.blockRegistry.getObject("mixture" + name);
	}

	public BlockFluidFinite getMolten(String name)
	{
		return (BlockFluidFinite) Block.blockRegistry.getObject("molten" + LanguageUtility.capitalizeFirst(name));
	}

	/**
	 * Gets the ItemStack of the ore dust with this material name.
	 */
	public ItemStack getDust(String name)
	{
		return getDust(name, 1);
	}

	/**
	 * Gets the ItemStack of the ore dust with this material name.
	 */
	public ItemStack getDust(String name, int quantity)
	{
		return new ItemStack(dustFactory.dirtDust, quantity);
	}

	/**
	 * Gets the ItemStack of the refined ore dust with this material name.
	 */
	public ItemStack getRefinedDust(String name, int quantity)
	{
		return new ItemStack(dustFactory.dust, quantity);
	}

	/**
	 * Gets the material of this ItemStack
	 */
	public String getMaterial(ItemStack stack)
	{
		return NBTUtility.getNBTTagCompound(stack).getString("material");
	}

	public ItemStack setMaterial(ItemStack stack, String material)
	{
		NBTUtility.getNBTTagCompound(stack).setString("material", material);
		return stack;
	}

	public String getName(ItemStack itemStack)
	{
		return LanguageUtility.decapitalizeFirst(OreDictionary.getOreName(OreDictionary.getOreID(itemStack)).replace("dirtyDust", "").replace("dust", "").replace("ore", "").replace("ingot", ""));
	}

	public int getColor(String name)
	{
		if (name != null && materialColorCache.containsKey(name))
		{
			return materialColorCache.get(name);
		}
		return 0xFFFFFF;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void reloadTextures(TextureStitchEvent.Post e)
	{
		computeColors();
	}

	@SideOnly(Side.CLIENT)
	public void computeColors()
	{
		for (String material : materials)
		{
			int totalR = 0;
			int totalG = 0;
			int totalB = 0;
			int colorCount = 0;
			for (ItemStack ingotStack : OreDictionary.getOres("ingot" + LanguageUtility.capitalizeFirst(material)))
			{
				Item theIngot = ingotStack.getItem();
				int color = getAverageColor(ingotStack);
				materialColorCache.put(material, color);
			}
			if (!materialColorCache.containsKey(material))
			{
				materialColorCache.put(material, 0xFFFFFF);
			}
		}
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

			if (iconColorCache.containsKey(icon))
			{
				return iconColorCache.get(icon);
			}

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
				int averageColor = new Color(totalR, totalG, totalB).brighter().getRGB();
				iconColorCache.put(icon, averageColor);
				return averageColor;
			}
		}
		catch (Exception e)
		{
			System.out.println(References.NAME + ": Failed to compute colors for: " + item);
			e.printStackTrace();
		}
		return 0xFFFFFF;
	}

	@Override
	public File getSaveFile()
	{
		return new File(NBTUtility.getSaveDirectory(), "ResourceFactory");
	}

	@Override
	public void setSaveFile(File file)
	{

	}

	@Override
	public void save(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for (Map.Entry<Integer, String> entry : materialIds.entrySet())
		{

		}
	}

	@Override
	public void load(NBTTagCompound nbt)
	{

	}
}
