package com.builtbroken.mc.core.content.tool;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.prefab.recipe.item.RecipeShapedOre;
import com.builtbroken.mc.prefab.recipe.item.RecipeTool;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Very basic hand tools used in complex crafting recipes
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/29/2015.
 */
public class ItemSimpleCraftingTool extends ItemAbstractCraftingTool implements IPostInit
{
    public static int MAX_FILE_DAMAGE = 400;
    public static int MAX_HAMMER_DAMAGE = 200;
    public static int MAX_CHISEL_DAMAGE = 200;
    public static int MAX_DRILL_DAMAGE = 300;
    public static int MAX_CUTTER_DAMAGE = 300;

    @SideOnly(Side.CLIENT)
    IIcon hammer;

    @SideOnly(Side.CLIENT)
    IIcon chisel;

    @SideOnly(Side.CLIENT)
    IIcon drill;

    @SideOnly(Side.CLIENT)
    IIcon file;

    @SideOnly(Side.CLIENT)
    IIcon cutter;


    public ItemSimpleCraftingTool()
    {
        super("SimpleTools");
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setUnlocalizedName(References.PREFIX + "simpleTools");
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public void onPostInit()
    {
        if (OreDictionary.getOreID("flint") == -1)
        {
            OreDictionary.registerOre("flint", Items.flint);
        }
        if (OreDictionary.getOreID("string") == -1)
        {
            OreDictionary.registerOre("string", Items.string);
        }
        //Hammer recipe
        GameRegistry.addRecipe(new RecipeShapedOre(getHammer(), "S", "t", 'S', "stone", 't', "stickWood"));
        //Chisel recipe
        GameRegistry.addRecipe(new RecipeShapedOre(getChisel(), "t", "S", 'S', "stone", 't', "stickWood"));

        //File recipe
        GameRegistry.addRecipe(new RecipeTool(getFile(), "th", "Sc", 'S', "stone", 't', "stickWood", 'c', getChisel(), 'h', getHammer()));
        GameRegistry.addRecipe(new RecipeTool(getFile(), "tc", "Sh", 'S', "stone", 't', "stickWood", 'c', getChisel(), 'h', getHammer()));

        if (Engine.itemSheetMetalTools != null)
        {
            GameRegistry.addRecipe(new RecipeTool(getFile(), "th", "Sc", 'S', "stone", 't', "stickWood", 'c', getChisel(), 'h', Engine.itemSheetMetalTools.getHammer()));
            GameRegistry.addRecipe(new RecipeTool(getFile(), "tc", "Sh", 'S', "stone", 't', "stickWood", 'c', getChisel(), 'h', Engine.itemSheetMetalTools.getHammer()));
        }

        //Drill recipe
        GameRegistry.addRecipe(new RecipeShapedOre(getDrill(), "tGs", "Gtt", "sF ", 's', "string", 'G', "gearWood", 'F', "flint", 't', "stickWood"));

        //Cutter recipe
        GameRegistry.addRecipe(new RecipeTool(getCutters(), "tft", "dsh", "g g", 's', "ironScrew", 'g', "ironRod", 't', "ingotIron", 'd', getDrill(), 'h', getHammer(), 'f', getFile()));

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        list.add(getHammer());
        list.add(getChisel());
        list.add(getDrill());
        list.add(getFile());
        list.add(getCutters());
    }

    public static ItemStack getTool(String type)
    {
        ItemStack stack = new ItemStack(Engine.itemSimpleCraftingTools);
        Engine.itemSimpleCraftingTools.setToolType(stack, type);
        return stack;
    }

    public static ItemStack getHammer()
    {
        return getTool("hammer");
    }

    public static ItemStack getChisel()
    {
        return getTool("chisel");
    }

    public static ItemStack getDrill()
    {
        return getTool("drill");
    }

    public static ItemStack getCutters()
    {
        return getTool("cutter");
    }

    public static ItemStack getFile()
    {
        return getTool("file");
    }

    @Override
    public int getMaxDamage(ItemStack stack)
    {
        String type = getToolType(stack);
        if ("hammer".equals(type))
        {
            return MAX_HAMMER_DAMAGE;
        }
        else if ("file".equals(type))
        {
            return MAX_FILE_DAMAGE;
        }
        else if ("drill".equals(type))
        {
            return MAX_DRILL_DAMAGE;
        }
        else if ("chisel".equals(type))
        {
            return MAX_CHISEL_DAMAGE;
        }
        else if ("cutter".equals(type))
        {
            return MAX_CUTTER_DAMAGE;
        }
        return 100;
    }

    @Override
    public String getToolCategory(ItemStack stack)
    {
        if (getToolType(stack).equalsIgnoreCase("cutter"))
        {
            return "iron";
        }
        return "stone";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.hammer = reg.registerIcon(References.PREFIX + "stoneHammer");
        this.chisel = reg.registerIcon(References.PREFIX + "stoneChisel");
        this.drill = reg.registerIcon(References.PREFIX + "stoneDrill");
        this.file = reg.registerIcon(References.PREFIX + "stoneFile");
        this.cutter = reg.registerIcon(References.PREFIX + "ironCutters");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return 1;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        String type = getToolType(stack);
        if (getToolCategory(stack).equalsIgnoreCase("stone"))
        {
            if (type != null && !type.isEmpty())
            {
                switch (type)
                {
                    case "chisel":
                        return chisel;
                    case "hammer":
                        return hammer;
                    case "drill":
                        return drill;
                    case "file":
                        return file;
                }
            }
        }
        else if (getToolCategory(stack).equalsIgnoreCase("iron"))
        {
            if (type != null && !type.isEmpty())
            {
                switch (type)
                {
                    case "cutter":
                        return cutter;
                }
            }
        }
        return Items.stone_hoe.getIcon(stack, pass);
    }
}
