package resonant.lib.prefab.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import resonant.lib.render.EnumColor;
import resonant.lib.utility.LanguageUtility;
import resonant.lib.utility.TooltipUtility;

public class ItemBlockTooltip extends ItemBlock
{
    public ItemBlockTooltip(int id)
    {
        super(id);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List list, boolean par4)
    {
        String tooltip = LanguageUtility.getLocal(getUnlocalizedName(itemStack) + ".tooltip");

        if (tooltip != null && tooltip.length() > 0)
        {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                list.add(LanguageUtility.getLocal("tooltip.noShift").replace("%0", EnumColor.AQUA.toString()).replace("%1", EnumColor.GREY.toString()));
            }
            else
            {
                list.addAll(LanguageUtility.splitStringPerWord(tooltip, 5));
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_J))
        {
            TooltipUtility.addTooltip(itemStack, list);
        }
        else
        {
            list.add(LanguageUtility.getLocal("info.recipes.tooltip").replace("%0", EnumColor.AQUA.toString()).replace("%1", EnumColor.GREY.toString()));
        }
    }

}
