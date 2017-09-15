package com.builtbroken.mc.debug.gui.panels.recipes;

import com.builtbroken.mc.debug.data.IJsonDebugData;
import com.builtbroken.mc.debug.gui.panels.imp.PanelDataList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2017.
 */
public class TabPanelFurnaceRecipes extends PanelDataList<TabPanelFurnaceRecipes.FurnaceData>
{
    @Override
    protected IJsonDebugData getDataEntryFor(FurnaceData object)
    {
        return object;
    }

    @Override
    protected void buildData()
    {
        data.clear();
        Map<ItemStack, ItemStack> map = FurnaceRecipes.smelting().getSmeltingList();
        for (Map.Entry<ItemStack, ItemStack> entry : map.entrySet())
        {
            data.add(new FurnaceData(entry.getKey(), entry.getValue()));
        }
    }

    public static class FurnaceData implements IJsonDebugData
    {
        ItemStack input;
        ItemStack output;
        float xp;

        public FurnaceData(ItemStack in, ItemStack out)
        {
            this.input = in;
            this.output = out;
        }

        @Override
        public String buildDebugLineDisplay()
        {
            return asString(input) + "  ->>  " + asString(output); //TODO add way to highlight red if bad values exist
        }

        protected String asString(ItemStack stack)
        {
            if (stack != null)
            {
                if (stack.getItem() != null)
                {
                    String value = stack.getDisplayName();
                    if (value != null)
                    {
                        value = value.trim();
                        if (!value.isEmpty() && !value.equalsIgnoreCase("null"))
                        {
                            return value;
                        }
                    }
                    //Fix for IC2 unlocalized being null for item mug
                    if (value != null)
                    {
                        value = value.trim();
                        if (!value.isEmpty() && !value.equalsIgnoreCase("null"))
                        {
                            return value;
                        }
                    }
                    return stack.toString();
                }
                return "null item";
            }
            return "null stack";
        }
    }
}
