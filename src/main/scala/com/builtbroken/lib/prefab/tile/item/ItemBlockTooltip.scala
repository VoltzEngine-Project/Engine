package com.builtbroken.lib.prefab.tile.item

import net.minecraft.block.Block
import net.minecraft.item.ItemBlock
import com.builtbroken.lib.prefab.item.TItemToolTip

class ItemBlockTooltip(block: Block) extends ItemBlock(block) with TItemToolTip
{
}