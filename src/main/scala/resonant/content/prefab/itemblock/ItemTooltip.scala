package resonant.content.prefab.itemblock

import net.minecraft.item.Item

/**
 * extend this instead of Item, gives the basis to Special Tooltip behaviour
 *
 * @author tgame14
 * @since 06/04/14
 */
abstract class ItemTooltip extends Item with TItemToolTip
{
}