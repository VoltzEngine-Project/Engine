/**
 * 
 */
package resonant.lib.prefab.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.api.vector.VectorWorld;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** @author Calclavia */
public abstract class ItemCoordLink extends ItemTooltip
{
    public ItemCoordLink(int id)
    {
        super(id);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
    {
        super.addInformation(itemstack, entityplayer, list, flag);

        if (hasLink(itemstack))
        {
            VectorWorld vec = getLink(itemstack);
            int blockId = vec.getBlockID(entityplayer.worldObj);

            if (Block.blocksList[blockId] != null)
            {
                list.add("Linked with: " + Block.blocksList[blockId].getLocalizedName());
            }

            list.add(vec.intX() + ", " + vec.intY() + ", " + vec.intZ());
            list.add("Dimension: '" + vec.world.provider.getDimensionName() + "'");
        }
        else
        {
            list.add("Not linked.");
        }
    }

    public boolean hasLink(ItemStack itemStack)
    {
        return getLink(itemStack) != null;
    }

    public VectorWorld getLink(ItemStack itemStack)
    {
        if (itemStack.stackTagCompound == null || !itemStack.getTagCompound().hasKey("link"))
        {
            return null;
        }

        return new VectorWorld(itemStack.getTagCompound().getCompoundTag("link"));
    }

    public void setLink(ItemStack itemStack, VectorWorld vec)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        itemStack.getTagCompound().setCompoundTag("link", vec.writeToNBT(new NBTTagCompound()));
    }

    public void clearLink(ItemStack itemStack)
    {
        itemStack.getTagCompound().removeTag("link");
    }
}
