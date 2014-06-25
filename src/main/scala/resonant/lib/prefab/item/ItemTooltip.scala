package resonant.lib.prefab.item

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import org.lwjgl.input.Keyboard
import resonant.content.prefab.itemblock.TItemToolTip
import resonant.lib.render.EnumColor
import resonant.lib.utility.LanguageUtility
import resonant.lib.utility.TooltipUtility
import java.util.List

/**
 * extend this instead of Item, gives the basis to Special Tooltip behaviour
 *
 * @author tgame14
 * @since 06/04/14
 */
abstract class ItemTooltip extends Item with TItemToolTip
{
}