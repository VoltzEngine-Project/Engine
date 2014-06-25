package resonant.content.prefab.itemblock

import java.util

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import org.lwjgl.input.Keyboard
import resonant.lib.wrapper.WrapList
import WrapList._
import resonant.lib.render.EnumColor
import resonant.lib.utility.{LanguageUtility, TooltipUtility}

/**
 * @author Calclavia
 */
trait TItemToolTip extends Item
{
  override def addInformation(itemStack: ItemStack, par2EntityPlayer: EntityPlayer, list: util.List[_], par4: Boolean)
  {
    val tooltip: String = LanguageUtility.getLocal(getUnlocalizedName(itemStack) + ".tooltip")
    if (tooltip != null && tooltip.length > 0)
    {
      if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
      {
        list.add(LanguageUtility.getLocal("tooltip.noShift").replace("%0", EnumColor.AQUA.toString).replace("%1", EnumColor.GREY.toString))
      }
      else
      {
        list.addAll(LanguageUtility.splitStringPerWord(tooltip, 5))
      }
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_J))
    {
      TooltipUtility.addTooltip(itemStack, list)
    }
    else
    {
      list.add(LanguageUtility.getLocal("info.recipes.tooltip").replace("%0", EnumColor.AQUA.toString).replace("%1", EnumColor.GREY.toString))
    }
  }
}
