package com.builtbroken.mc.codegen.templates.item;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.items.tools.IWorldPosItem;
import com.builtbroken.mc.codegen.processor.ItemWrappedTemplate;
import com.builtbroken.mc.framework.item.ItemBase;
import com.builtbroken.mc.framework.item.logic.ItemNode;
import com.builtbroken.mc.imp.transform.vector.Location;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/9/2017.
 */
@ItemWrappedTemplate(annotationName = "IWorldPos")
public class ItemWorldPos extends ItemBase implements IWorldPosItem
{
    public ItemWorldPos(ItemNode node)
    {
        super(node);
    }

    //#StartMethods#
    @Override
    public Location getLocation(ItemStack stack)
    {
        if (stack.getTagCompound().hasKey("linkPos"))
        {
            return new Location(stack.getTagCompound().getCompoundTag("linkPos"));
        }
        return null;
    }

    @Override
    public void setLocation(ItemStack stack, IWorldPosition loc)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setTag("linkPos", loc.toLocation().toNBT());
    }

    @Override
    public boolean canAccessLocation(ItemStack stack, Object obj)
    {
        if (node instanceof IWorldPosItem)
        {
            return ((IWorldPosItem) node).canAccessLocation(stack, obj);
        }
        return false;
    }
    //#EndMethods#
}
