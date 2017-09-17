package com.builtbroken.mc.debug.gui.panels.recipes;

import com.builtbroken.mc.debug.data.IJsonDebugData;
import com.builtbroken.mc.debug.gui.panels.imp.PanelDataList;
import com.builtbroken.mc.lib.data.StringComparator;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.Collections;
import java.util.Comparator;
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
        Collections.sort(data, new FurnaceDataSorter());
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
            return asString(input) + "  >>>  " + asString(output); //TODO add way to highlight red if bad values exist
        }

        protected String asString(ItemStack stack)
        {
            if (stack != null)
            {
                if (stack.getItem() != null)
                {
                    final String name = Item.itemRegistry.getNameForObject(stack.getItem()).split(":")[0];
                    return "[" + name + " : " + InventoryUtility.getDisplayName(stack) + "]";
                }
                return "null item";
            }
            return "null stack";
        }

        public String getMod()
        {
            return InventoryUtility.getModID(input);
        }

        public String getName()
        {
            return InventoryUtility.getDisplayName(input);
        }


    }

    public static class FurnaceDataSorter implements Comparator<FurnaceData>
    {
        protected final StringComparator stringComparator;

        public FurnaceDataSorter()
        {
            stringComparator = new StringComparator();
        }

        @Override
        public int compare(FurnaceData o1, FurnaceData o2)
        {
            String regName1 = Item.itemRegistry.getNameForObject(o1.input.getItem());
            String regName2 = Item.itemRegistry.getNameForObject(o2.input.getItem());

            if (regName1 == null)
            {
                return -1;
            }
            else if (regName2 == null)
            {
                return 1;
            }

            int result = stringComparator.compare(o1.getMod(), o2.getMod());
            if (result == 0)
            {
                return stringComparator.compare(o1.getName(), o2.getName());
            }
            return result;
        }
    }
}
