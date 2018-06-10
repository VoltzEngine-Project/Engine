package com.builtbroken.mc.core.content.tool;

import buildcraft.api.tools.IToolWrench;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolMode;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.framework.recipe.item.RecipeTool;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.helper.recipe.OreNames;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.List;

@Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraft")
public class ItemScrewdriver extends ItemToolModeColor implements IToolWrench, IPostInit
{
    @SideOnly(Side.CLIENT)
    private HashMap<ToolMode, IIcon> coloredTexture;
    @SideOnly(Side.CLIENT)
    private HashMap<ToolMode, IIcon> texture;

    public ItemScrewdriver()
    {
        this.setUnlocalizedName(References.PREFIX + "screwdriver");
        this.setTextureName(References.PREFIX + "wrench/wrench");
        this.setNoRepair();
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean canWrench(EntityPlayer entityPlayer, int x, int y, int z)
    {
        return isWrench(entityPlayer.getHeldItem(), entityPlayer, x, y, z);
    }

    @Override
    public void wrenchUsed(EntityPlayer entityPlayer, int x, int y, int z)
    {

    }

    public boolean isWrench(ItemStack stack, EntityPlayer entityPlayer, int x, int y, int z)
    {
        return getToolMode(stack) == ToolMode.MODE_GENERAL;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean par4)
    {
        lines.add(LanguageUtility.getLocal(getToolMode(stack).getInfoName()));
        lines.add(LanguageUtility.getLocal(getUnlocalizedName() + ".color." + getColor(stack).name().toLowerCase() + ".info"));
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
            lines.add(LanguageUtility.getLocal(getUnlocalizedName() + ".info"));
            lines.add(LanguageUtility.getLocal(getUnlocalizedName() + ".ctrl.info"));
            lines.add(LanguageUtility.getLocal(getUnlocalizedName() + ".wheel.info"));
        }
        else
        {
            lines.add(LanguageUtility.getLocal(getUnlocalizedName() + ".more.info"));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return getToolMode(stack).getItemUnlocalizedName(getColor(stack));
    }

    public ToolMode getToolMode(ItemStack stack)
    {
        return ToolMode.getMode(getMode(stack));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        return getToolMode(stack).onItemRightClick(stack, world, player, getColor(stack));
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return getToolMode(stack).onItemUseFirst(stack, player, getColor(stack), world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return getToolMode(stack).onItemUse(stack, player, getColor(stack), world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }

    @Override
    public void onPostInit()
    {
        if (Engine.itemSimpleCraftingTools != null)
        {
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemWrench), "RFH", "DRW", "SWI", 'F', ItemSimpleCraftingTool.getFile(), 'H', ItemSimpleCraftingTool.getHammer(), 'D', ItemSimpleCraftingTool.getDrill(), 'R', OreNames.ROD_STEEL, 'I', OreNames.INGOT_STEEL, 'W', OreNames.WOOD, 'S', OreNames.SCREW_STEEL));
            if (Engine.itemSheetMetalTools != null)
            {
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemWrench), "RFH", "DRW", "SWI", 'F', ItemSimpleCraftingTool.getFile(), 'H', ItemSheetMetalTools.getHammer(), 'D', ItemSimpleCraftingTool.getDrill(), 'R', OreNames.ROD_STEEL, 'I', OreNames.INGOT_STEEL, 'W', OreNames.WOOD, 'S', OreNames.SCREW_STEEL));
            }
        }
    }

    @Override
    public int getModeCount(ItemStack stack)
    {
        return ToolMode.getModeCount();
    }


    //===============================================
    //=======Render Stuff
    //===============================================

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        super.registerIcons(reg);
        coloredTexture = new HashMap();
        texture = new HashMap();
        for (ToolMode mode : ToolMode.getModes())
        {
            if (mode.getTexture(getIconString()) != null)
            {
                texture.put(mode, reg.registerIcon(mode.getTexture(getIconString())));
            }
            if (mode.getColorizedTexture(getIconString()) != null)
            {
                coloredTexture.put(mode, reg.registerIcon(mode.getColorizedTexture(getIconString())));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass)
    {
        ToolMode mode = getToolMode(stack);
        if (pass == 0)
        {
            IIcon icon = texture.get(mode);
            if (icon != null)
            {
               return icon;
            }
            return super.getIcon(stack, pass);
        }

        IIcon icon = coloredTexture.get(mode);
        if (icon != null)
        {
            return icon;
        }
        return super.getIcon(stack, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderPasses(int metadata)
    {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        if (pass == 0 || !getToolMode(stack).canBeColored())
        {
            return super.getColorFromItemStack(stack, pass);
        }
        return getColor(stack).getColorInt();
    }
}
