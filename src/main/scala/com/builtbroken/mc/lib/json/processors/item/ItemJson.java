package com.builtbroken.mc.lib.json.processors.item;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.RenderState;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.lib.json.IJsonGenMod;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.item.processor.JsonItemProcessor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Container/Wrapper for data representing an item
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public class ItemJson extends Item implements IJsonGenObject, IJsonRenderStateProvider
{
    /** ID used to register this item to the game */
    public final String ID;
    /** Mod that owns the content, domain ID */
    public final String owner;
    /** Was the content registered */
    protected boolean registered = false;

    private boolean initTexture = false;

    /**
     * @param id   - unique id for the item to be registered
     * @param name - used to localize the item
     */
    public ItemJson(String id, String owner, String name)
    {
        this.ID = id;
        this.owner = owner;
        setUnlocalizedName(owner + ":" + name);
        setTextureName(owner + ":" + name);
    }

    @Override
    public void register(IJsonGenMod mod, ModManager manager)
    {
        if (!registered)
        {
            registered = true;
            manager.newItem(ID, this);
            Engine.logger().info(this + " has been claimed by " + mod);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        super.registerIcons(reg);
        initTexture = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta)
    {
        //Get texture from loader if has render data
        if (initTexture)
        {
            initTexture = true;
            RenderData data = ClientDataHandler.INSTANCE.getRenderData(getRenderContentID());
            if (data != null)
            {
                RenderState state = data.getState(RenderData.INVENTORY_RENDER_ID);
                if (!state.isModelRenderer())
                {
                    IIcon icon = state.getIcon();
                    if (icon != null)
                    {
                        itemIcon = icon;
                    }
                }
            }
        }
        return itemIcon != null ? itemIcon : Items.stick.getIconFromDamage(0);
    }

    @Override
    public String getLoader()
    {
        return JsonItemProcessor.KEY;
    }

    @Override
    public String getMod()
    {
        return owner;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getRenderContentID(IItemRenderer.ItemRenderType renderType, Object objectBeingRendered)
    {
        return getRenderContentID();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public List<String> getRenderContentIDs()
    {
        List<String> list = new ArrayList();
        list.add(getRenderContentID());
        return list;
    }

    public String getRenderContentID()
    {
        return getUnlocalizedName();
    }
}
