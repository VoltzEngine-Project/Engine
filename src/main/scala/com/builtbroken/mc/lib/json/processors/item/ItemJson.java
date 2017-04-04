package com.builtbroken.mc.lib.json.processors.item;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.lib.json.IJsonGenMod;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.item.processor.JsonItemProcessor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        return getIconFromState(ClientDataHandler.INSTANCE.getRenderData(getRenderContentID()), meta, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true; //Fix for getting ItemStack
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderPasses(int metadata)
    {
        RenderData data = ClientDataHandler.INSTANCE.getRenderData(getRenderContentID());
        if (data != null)
        {
            return data.itemRenderLayers;
        }
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int meta, int pass)
    {
        return getIconFromState(ClientDataHandler.INSTANCE.getRenderData(getRenderContentID()), meta, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        String gunState = getRenderKey(stack, player, useRemaining);
        if (gunState != null)
        {
            RenderData data = ClientDataHandler.INSTANCE.getRenderData(getRenderContentID());
            if (data != null)
            {
                IRenderState state = data.getState(RenderData.INVENTORY_RENDER_KEY + "." + gunState);
                if (data != null)
                {
                    IIcon icon = state.getIcon(renderPass);
                    if (icon != null)
                    {
                        return icon;
                    }
                }
            }
        }
        return getIconFromDamageForRenderPass(stack.getItemDamage(), renderPass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass)
    {
        String gunState = getRenderKey(stack);
        if (gunState != null)
        {
            RenderData data = ClientDataHandler.INSTANCE.getRenderData(getRenderContentID());
            if (data != null)
            {
                IRenderState state = data.getState(RenderData.INVENTORY_RENDER_KEY + "." + gunState);
                if (data != null)
                {
                    IIcon icon = state.getIcon(pass);
                    if (icon != null)
                    {
                        return icon;
                    }
                }
            }
        }
        return getIconFromDamageForRenderPass(stack.getItemDamage(), pass);
    }

    /**
     * Called to ge the render key for the stack
     * <p>
     * Keep in mind key is prefixed by render type
     * <p>
     * Ex: item.inventory.key
     * item.entity.key
     *
     * @param stack - stack being rendered
     * @return key for the render ID
     */
    public String getRenderKey(ItemStack stack)
    {
        return null;
    }

    /**
     * Called to ge the render key for the stack
     * <p>
     * Keep in mind key is prefixed by render type
     * <p>
     * Ex: item.inventory.key
     * item.entity.key
     *
     * @param stack        - stack being rendered
     * @param entity       - entity holding the item
     * @param useRemaining - how many uses are left
     * @return key for the render ID
     */
    public String getRenderKey(ItemStack stack, Entity entity, int useRemaining)
    {
        return null;
    }

    /**
     * Called to get the icon from the state
     *
     * @param data - render data
     * @param meta - metadata or damage of the item
     * @param pass - render pass, 0 by default
     * @return icon, can not be null or will crash
     */
    protected IIcon getIconFromState(RenderData data, int meta, int pass)
    {
        if (data != null)
        {
            IRenderState state = data.getState(RenderData.INVENTORY_RENDER_KEY + "." + meta);
            if (state != null)
            {
                state = data.getState(RenderData.INVENTORY_RENDER_KEY);
                if (state != null)
                {
                    IIcon icon = state.getIcon(pass);
                    if (icon != null)
                    {
                        return icon;
                    }
                }
            }
            if (!initTexture)
            {
                initTexture = true;
                if (data != null)
                {
                    state = data.getState(RenderData.INVENTORY_RENDER_KEY);
                    if (state != null)
                    {
                        IIcon icon = state.getIcon(0);
                        if (icon != null)
                        {
                            itemIcon = icon;
                        }
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
        return "item." + owner + ":" + ID;
    }
}
