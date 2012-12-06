package basiccomponents.item;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import railcraft.common.api.core.items.ICrowbar;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.implement.IToolConfigurator;
import basiccomponents.BasicComponents;
import buildcraft.api.tools.IToolWrench;

public class ItemWrench extends Item implements ICrowbar, IToolConfigurator, IToolWrench
{
	public ItemWrench(int id, int texture)
	{
		super(id);
		this.setMaxStackSize(1);
		this.setMaxDamage(3000);
		this.setCreativeTab(UETab.INSTANCE);
		this.setItemName("wrench");
		this.setIconIndex(texture);
		this.setTextureFile(BasicComponents.ITEM_TEXTURE_FILE);
	}

	@Override
	public boolean canWrench(EntityPlayer entityPlayer, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer entityPlayer, int x, int y, int z)
	{
		if (entityPlayer.getCurrentEquippedItem() != null)
		{
			if (entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemWrench)
			{
				entityPlayer.getCurrentEquippedItem().damageItem(1, entityPlayer);
			}
		}
	}
}
