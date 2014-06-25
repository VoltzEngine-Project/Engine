/**
 *
 */
package resonant.lib.prefab.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.core.transform.vector.VectorWorld;

import java.util.List;

/**
 * @author Calclavia
 */
public abstract class ItemCoordLink extends ItemTooltip
{
	public ItemCoordLink(int id)
	{
		super(id);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
	{
		super.addInformation(itemstack, entityplayer, list, flag);

		if (hasLink(itemstack))
		{
			VectorWorld vec = getLink(itemstack);
			Block block = vec.getBlock(entityplayer.worldObj);

			if (block != null)
			{
				list.add("Linked with: " + block.getLocalizedName());
			}

			list.add(vec.xi() + ", " + vec.yi() + ", " + vec.zi());
			list.add("Dimension: '" + vec.world().provider.getDimensionName() + "'");
		}
		else
		{
			list.add("Not linked.");
		}
	}

	public boolean hasLink(ItemStack itemStack)
	{
		return getLink(itemStack) != null;
	}

	public VectorWorld getLink(ItemStack itemStack)
	{
		if (itemStack.stackTagCompound == null || !itemStack.getTagCompound().hasKey("link"))
		{
			return null;
		}

		return new VectorWorld(itemStack.getTagCompound().getCompoundTag("link"));
	}

	public void setLink(ItemStack itemStack, VectorWorld vec)
	{
		if (itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		itemStack.getTagCompound().setTag("link", vec.toNBT());
	}

	public void clearLink(ItemStack itemStack)
	{
		itemStack.getTagCompound().removeTag("link");
	}
}
