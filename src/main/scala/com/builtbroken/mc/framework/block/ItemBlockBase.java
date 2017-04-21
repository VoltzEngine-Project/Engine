package com.builtbroken.mc.framework.block;

import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
import com.builtbroken.mc.prefab.items.ItemBlockAbstract;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraftforge.client.IItemRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2017.
 */
public class ItemBlockBase extends ItemBlockAbstract implements IJsonRenderStateProvider
{
    public ItemBlockBase(Block block)
    {
        super(block);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getRenderContentID(IItemRenderer.ItemRenderType renderType, Object objectBeingRendered)
    {
        return getRenderContentID(0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public List<String> getRenderContentIDs()
    {
        List<String> list = new ArrayList();
        list.add(getRenderContentID(0));
        return list;
    }

    public String getRenderContentID(int meta)
    {
        return ((BlockBase)field_150939_a).getContentID(meta);
    }
}
