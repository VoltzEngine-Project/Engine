package resonant.lib.prefab.tile.item

import net.minecraft.block.Block
import net.minecraft.item.ItemBlock
import resonant.lib.prefab.item.TItemToolTip

class ItemBlockTooltip(block: Block) extends ItemBlock(block) with TItemToolTip
{
}