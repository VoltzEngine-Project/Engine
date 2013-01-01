package basiccomponents.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import railcraft.common.api.core.items.ICrowbar;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.implement.ISneakUseWrench;
import universalelectricity.prefab.implement.IToolConfigurator;
import basiccomponents.common.BasicComponents;

public class ItemWrench extends Item implements ICrowbar, IToolConfigurator
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
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (player.isSneaking())
		{
			int blockID = world.getBlockId(x, y, z);

			if (blockID > 0 && blockID < Block.blocksList.length)
			{
				Block block = Block.blocksList[blockID];

				if (block != null)
				{
					if (block instanceof ISneakUseWrench)
					{
						((ISneakUseWrench) block).onSneakUseWrench(world, x, y, z, player, side, hitX, hitY, hitZ);
						return true;
					}
				}
			}
		}

		return false;
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
