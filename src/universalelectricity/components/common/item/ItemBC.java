package universalelectricity.components.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import universalelectricity.components.common.BasicComponents;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * An Base Item Class for Basic Components. Do not use this! Make your own!
 * 
 * @author Calclavia
 * 
 */
public class ItemBC extends Item
{
	protected final Icon[] icons = new Icon[256];

	public ItemBC(String name, int id)
	{
		super(id);
		this.setUnlocalizedName(name);
		this.setCreativeTab(BasicComponents.TAB);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		this.getSubItems(this.itemID, this.getCreativeTab(), list);

		if (list.size() < this.icons.length)
		{
			for (ItemStack itemStack : list)
			{
				this.icons[list.indexOf(itemStack)] = iconRegister.registerIcon(this.getUnlocalizedName(itemStack).replace("item.", BasicComponents.TEXTURE_NAME_PREFIX));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIconFromDamage(int damage)
	{
		if (this.icons.length > damage && !this.isDamageable())
		{
			return this.icons[damage];
		}

		return icons[0];
	}
}
