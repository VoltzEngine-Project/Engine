package com.builtbroken.mc.debug.gui.panels.recipes;

import com.builtbroken.mc.debug.data.IJsonDebugData;
import com.builtbroken.mc.debug.gui.panels.imp.PanelDataList;
import com.builtbroken.mc.lib.data.StringComparator;
import com.builtbroken.mc.lib.helper.ReflectionUtility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2017.
 */
public class TabPanelShapedRecipes extends PanelDataList<TabPanelShapedRecipes.ShapedRecipeData>
{
    @Override
    protected IJsonDebugData getDataEntryFor(ShapedRecipeData object)
    {
        return object;
    }

    @Override
    protected void buildData()
    {
        try
        {
            data.clear();
            for (Object r : CraftingManager.getInstance().getRecipeList())
            {
                if (r instanceof ShapedRecipes)
                {
                    ShapedRecipeData recipeData = new ShapedRecipeData();
                    recipeData.recipeWidth = ((ShapedRecipes) r).recipeWidth;
                    recipeData.recipeHeight = ((ShapedRecipes) r).recipeHeight;
                    recipeData.recipeOutput = ((ShapedRecipes) r).getRecipeOutput();
                    recipeData.recipeItems = ((ShapedRecipes) r).recipeItems;
                    data.add(recipeData);
                }
                else if (r instanceof ShapedOreRecipe)
                {
                    ShapedRecipeData shapedRecipe = new ShapedRecipeData();
                    shapedRecipe.recipeWidth = ReflectionUtility.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe) r, "width");
                    shapedRecipe.recipeHeight = ReflectionUtility.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe) r, "height");
                    shapedRecipe.recipeOutput = ((ShapedOreRecipe) r).getRecipeOutput();
                    shapedRecipe.recipeItems = ((ShapedOreRecipe) r).getInput();
                    data.add(shapedRecipe);
                }
            }
            Collections.sort(data, new FurnaceDataSorter());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static class ShapedRecipeData implements IJsonDebugData
    {
        /** How many horizontal slots this recipe is wide. */
        public int recipeWidth;
        /** How many vertical slots this recipe uses. */
        public int recipeHeight;
        /** Is a array of ItemStack that composes the recipe. */
        public Object[] recipeItems;
        /** Is the ItemStack that you get when craft the recipe. */
        private ItemStack recipeOutput;

        @Override
        public String buildDebugLineDisplay()
        {
            String text = "<html>";
            text += "<h3>Output: " + asString(recipeOutput) + "</h3>";
            text += "<p>Size: " + recipeWidth + "x" + recipeHeight + "</p>";

            text += "<table>";
            for (int w = 0; w < recipeWidth; w++)
            {
                text += "<tr>";
                for (int h = 0; h < recipeHeight; h++)
                {
                    int index = h + w * recipeWidth;
                    if (index < recipeItems.length)
                    {
                        text += "<td>" + asString(recipeItems[index]) + "</td>";
                    }
                }
                text += "</tr>";
            }
            text += "</table>";
            text += "</html>";
            return text;
        }

        protected String asString(Object object)
        {
            if (object instanceof ItemStack)
            {
                ItemStack stack = (ItemStack) object;
                if (stack.getItem() != null)
                {
                    final String name = Item.itemRegistry.getNameForObject(stack.getItem()).split(":")[0];
                    return "[" + name + " : " + getName(stack) + "]";
                }
                return "null item";
            }
            else if (object instanceof String)
            {
                return (String) object;
            }
            else if (object instanceof List)
            {
                if (((List) object).size() > 0)
                {
                    if (((List) object).size() > 1)
                    {
                        String t = "[";
                        for (Object o : (List) object)
                        {
                            t += asString(o) + (o != ((List) object).get(((List) object).size() - 1) ? ", " : "");
                        }
                        return t + "]";
                    }
                    return asString(((List) object).get(0));
                }
                return "_";
            }
            return object != null ? object.toString() : "";
        }

        public String getMod()
        {
            return getMod(recipeOutput);
        }

        public String getMod(ItemStack stack)
        {
            if (stack != null && stack.getItem() != null)
            {
                String regName = Item.itemRegistry.getNameForObject(stack.getItem());
                if (regName != null)
                {
                    return regName.split(":")[0];
                }
            }
            return "null";
        }

        public String getName()
        {
            return getName(recipeOutput);
        }

        public String getName(ItemStack stack)
        {
            final String regName = Item.itemRegistry.getNameForObject(stack.getItem());
            final String name = regName.split(":")[1];

            for (String value : new String[]{stack.getDisplayName(), stack.getUnlocalizedName(), name, StatCollector.translateToLocal(stack.getItem().getUnlocalizedName()) + " " + stack.getItemDamage()})
            {
                if (value != null)
                {
                    value = value.trim();
                    if (!value.isEmpty() && !value.toLowerCase().startsWith("null"))
                    {
                        return value;
                    }
                }
            }
            return stack.getItem().getUnlocalizedName() + "@" + stack.getItemDamage();
        }
    }

    public static class FurnaceDataSorter implements Comparator<ShapedRecipeData>
    {
        protected final StringComparator stringComparator;

        public FurnaceDataSorter()
        {
            stringComparator = new StringComparator();
        }

        @Override
        public int compare(ShapedRecipeData o1, ShapedRecipeData o2)
        {
            String regName1 = Item.itemRegistry.getNameForObject(o1.recipeOutput.getItem());
            String regName2 = Item.itemRegistry.getNameForObject(o2.recipeOutput.getItem());

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
