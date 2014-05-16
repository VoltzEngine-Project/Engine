package resonant.core.content;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import resonant.core.content.tool.ToolMode;
import resonant.lib.utility.LanguageUtility;
import resonant.lib.utility.nbt.NBTUtility;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.Optional;

public class ItemScrewdriver extends ItemBase implements IToolWrench
{
    public ItemScrewdriver(int id)
    {
        super("screwdriver", id);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean canWrench(EntityPlayer entityPlayer, int x, int y, int z)
    {
        return true;
    }

    @Override
    public void wrenchUsed(EntityPlayer entityPlayer, int x, int y, int z)
    {

    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(LanguageUtility.getLocal("toolmode.mode") + ": " + LanguageUtility.getLocal(ToolMode.REGISTRY.get(getMode(itemStack)).getName()));
        par3List.addAll(LanguageUtility.splitStringPerWord(LanguageUtility.getLocal("item.resonant:screwdriver.tooltip"), 4));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        // TODO: Fix this.
        if (player.isSneaking())
        {
            setMode(itemStack, (getMode(itemStack) + 1) % ToolMode.REGISTRY.size());

            if (!world.isRemote)
                player.addChatMessage(LanguageUtility.getLocal("item.resonant:screwdriver.toolmode.set") + LanguageUtility.getLocal(ToolMode.REGISTRY.get(getMode(itemStack)).getName()));
            return itemStack;
        }

        return ToolMode.REGISTRY.get(getMode(itemStack)).onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return ToolMode.REGISTRY.get(getMode(stack)).onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return ToolMode.REGISTRY.get(getMode(stack)).onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z)
    {
        return true;
    }

    public int getMode(ItemStack itemStack)
    {
        return NBTUtility.getNBTTagCompound(itemStack).getInteger("mode");
    }

    public void setMode(ItemStack itemStack, int mode)
    {
        NBTUtility.getNBTTagCompound(itemStack).setInteger("mode", mode);
    }

}
