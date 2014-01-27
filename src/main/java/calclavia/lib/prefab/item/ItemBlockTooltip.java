package calclavia.lib.prefab.item;

import java.util.List;

import calclavia.lib.utility.LanguageUtility;
import net.minecraft.client.resources.Language;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTooltip extends ItemBlock
{
	public ItemBlockTooltip(int id)
	{
		super(id);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List info, boolean par4)
	{
		String tooltip = LanguageUtility.getLocal(this.getUnlocalizedName() + ".tooltip");

		if (tooltip != null && tooltip.length() > 0)
		{
			info.addAll(LanguageUtility.splitStringPerWord(tooltip, 5));
		}
	}

}
