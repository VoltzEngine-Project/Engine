package calclavia.lib.prefab.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import calclavia.lib.utility.LanguageUtility;

public class ItemBlockTooltip extends ItemBlock
{
	public ItemBlockTooltip(int id)
	{
		super(id);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List info, boolean par4)
	{
		String tooltip = LanguageUtility.getLocal(getUnlocalizedName() + ".tooltip");

		if (tooltip != null && tooltip.length() > 0)
		{
			info.addAll(LanguageUtility.splitStringPerWord(tooltip, 5));
		}
	}

}
