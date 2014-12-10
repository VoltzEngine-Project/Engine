/**
 *
 */
package resonant.lib.prefab.item

import java.util

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import resonant.lib.prefab.item.ItemTooltip
import resonant.lib.wrapper.WrapList._
import resonant.lib.transform.vector.VectorWorld

/**
 * @author Calclavia
 */
abstract class ItemCoordLink extends ItemTooltip
{
  @SideOnly(Side.CLIENT) override def addInformation(itemstack: ItemStack, entityplayer: EntityPlayer, list: util.List[_], flag: Boolean)
  {
    super.addInformation(itemstack, entityplayer, list, flag)

    if (hasLink(itemstack))
    {
      val vec: VectorWorld = getLink(itemstack)
      val block: Block = vec.getBlock(entityplayer.worldObj)
      if (block != null)
      {
        list.add("Linked with: " + block.getLocalizedName)
      }
      list.add(vec.xi + ", " + vec.yi + ", " + vec.zi)
      list.add("Dimension: '" + vec.world.provider.getDimensionName + "'")
    }
    else
    {
      list.add("Not linked.")
    }
  }

  def hasLink(itemStack: ItemStack): Boolean = getLink(itemStack) != null

  def getLink(itemStack: ItemStack): VectorWorld =
  {
    if (itemStack.stackTagCompound == null || !itemStack.getTagCompound.hasKey("link"))
    {
      return null
    }
    return new VectorWorld(itemStack.getTagCompound.getCompoundTag("link"))
  }

  def setLink(itemStack: ItemStack, vec: VectorWorld)
  {
    if (itemStack.getTagCompound == null)
    {
      itemStack.setTagCompound(new NBTTagCompound)
    }
    itemStack.getTagCompound.setTag("link", vec.toNBT)
  }

  def clearLink(itemStack: ItemStack)
  {
    itemStack.getTagCompound.removeTag("link")
  }
}