package com.builtbroken.mc.client.json.render;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
import com.builtbroken.mc.client.json.imp.IModelState;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.lib.json.imp.IJsonProcessor;
import com.builtbroken.mc.lib.json.processors.JsonGenData;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/16/2016.
 */
public class RenderData extends JsonGenData
{
    public static final String INVENTORY_RENDER_KEY = "item.inventory";
    public static final String EQUIPPED_RENDER_KEY = "item.equipped";
    public static final String FIRST_PERSON_RENDER_KEY = "item.first";
    public static final String ENTITY_RENDER_KEY = "item.entity";

    /** ID to reference the content that this render data belongs to */
    public final String contentID;
    /** Type of render, defaults to VE render code */
    public final String renderType;

    public int itemRenderLayers = 1;

    /** Map for quickly looking up name of the state with it's render ID */
    public HashMap<String, IRenderState> renderStatesByName = new HashMap();

    public RenderData(IJsonProcessor processor, String contentID, String type)
    {
        super(processor);
        this.contentID = contentID;
        this.renderType = type;
    }

    public IRenderState getState(String state)
    {
        return renderStatesByName.get(state);
    }

    public boolean hasState(String state)
    {
        return renderStatesByName.get(state) != null;
    }

    public boolean canRenderState(String state)
    {
        return hasState(state) && getState(state) instanceof IModelState;
    }

    /**
     * Called to render a state raw with
     * no modifying data
     *
     * @param state - unique key name for the state
     * @return true if something was renderer
     */
    public boolean render(String state)
    {
        return canRenderState(state) && ((IModelState)getState(state)).render();
    }

    @Override
    public void register()
    {
        ClientDataHandler.INSTANCE.addRenderData(contentID, this);
    }

    @Override
    public String getContentID()
    {
        return contentID;
    }

    public boolean render(IItemRenderer.ItemRenderType type, String stateKey, Object item)
    {
        return render(getRenderKeyForState(type, stateKey, item));
    }

    public String getRenderKeyForState(IItemRenderer.ItemRenderType type, String stateKey, Object item)
    {
        String key = null;
        if (item instanceof IJsonRenderStateProvider)
        {
            key = ((IJsonRenderStateProvider) item).getRenderStateKey(type, stateKey, item);
        }
        else if (item instanceof ItemStack && ((ItemStack) item).getItem() instanceof IJsonRenderStateProvider)
        {
            key = ((IJsonRenderStateProvider) ((ItemStack) item).getItem()).getRenderStateKey(type, stateKey, item);
        }

        switch (type)
        {
            case ENTITY:
                key = ENTITY_RENDER_KEY + (stateKey != null ? "." + stateKey : "");
                break;
            case INVENTORY:
                key = INVENTORY_RENDER_KEY + (stateKey != null ? "." + stateKey : "");
                break;
            case EQUIPPED:
                key = EQUIPPED_RENDER_KEY + (stateKey != null ? "." + stateKey : "");
                break;
            case EQUIPPED_FIRST_PERSON:
                key = FIRST_PERSON_RENDER_KEY + (stateKey != null ? "." + stateKey : "");
                break;
        }
        return key;
    }

    public boolean shouldRenderType(IItemRenderer.ItemRenderType type, String key, Object item)
    {
        return canRenderState(getRenderKeyForState(type, key, item));
    }

    public void add(String name, IRenderState state)
    {
        renderStatesByName.put(name, state);
    }
}
