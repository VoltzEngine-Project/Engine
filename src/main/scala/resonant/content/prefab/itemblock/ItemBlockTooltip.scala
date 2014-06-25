package resonant.content.prefab.itemblock

import net.minecraft.block.Block
import net.minecraft.item.ItemBlock

class ItemBlockTooltip(block: Block) extends ItemBlock(block) with TItemToolTip
{
}