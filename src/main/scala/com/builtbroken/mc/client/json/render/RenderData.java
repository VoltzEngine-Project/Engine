package com.builtbroken.mc.client.json.render;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
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
    public static final int DEFAULT_RENDER = -1;
    public static final int INVENTORY_RENDER_ID = 0;
    public static final int EQUIPPED_RENDER_ID = 1;
    public static final int FIRST_PERSON_RENDER_ID = 2;
    public static final int ENTITY_RENDER_ID = 3;

    /** ID to reference the content that this render data belongs to */
    public final String contentID;
    /** Type of render, defaults to VE render code */
    public final String renderType;

    /** States for the renderer to handle */
    public HashMap<Integer, RenderState> renderStates = new HashMap();

    /** Map for quickly looking up name of the state with it's render ID */
    private HashMap<String, RenderState> renderStatesByName = new HashMap();

    public RenderData(IJsonProcessor processor, String contentID, String type)
    {
        super(processor);
        this.contentID = contentID;
        this.renderType = type;
    }

    /**
     * Called to render a state raw with
     * no modifying data
     *
     * @param state
     * @return true if something was renderer
     */
    public boolean render(int state)
    {
        return canRenderState(state) && getState(state).render();
    }

    public RenderState getState(int state)
    {
        return renderStates.get(state);
    }

    public RenderState getState(String state)
    {
        return renderStatesByName.get(state);
    }

    public boolean hasState(int state)
    {
        return renderStates.get(state) != null;
    }

    public boolean hasState(String state)
    {
        return renderStatesByName.get(state) != null;
    }

    public boolean canRenderState(int state)
    {
        return hasState(state) && getState(state).isModelRenderer();
    }

    public boolean canRenderState(String state)
    {
        return hasState(state) && getState(state).isModelRenderer();
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
        return canRenderState(state) && getState(state).render();
    }

    @Override
    public void register()
    {
        ClientDataHandler.INSTANCE.addRenderData(contentID, this);
    }

    /**
     * Called to render the object in its state
     *
     * @param type - type of render being applied
     * @param item - object being rendered
     * @return
     */
    public boolean render(IItemRenderer.ItemRenderType type, Object item)
    {
        return render(getRenderIDForState(type, item));
    }

    /**
     * Gets the render ID for the state of the object and renderer
     *
     * @param type - type of render being applied by Item rendering
     * @param item - object being rendered (normally an entity, tile, or item)
     * @return ID to use
     */
    public int getRenderIDForState(IItemRenderer.ItemRenderType type, Object item)
    {
        int renderID = DEFAULT_RENDER;
        if (item instanceof IJsonRenderStateProvider)
        {
            renderID = ((IJsonRenderStateProvider) item).getRenderStateID(type, item);
        }
        else if (item instanceof ItemStack && ((ItemStack) item).getItem() instanceof IJsonRenderStateProvider)
        {
            renderID = ((IJsonRenderStateProvider) ((ItemStack) item).getItem()).getRenderStateID(type, item);
        }

        //Use defaults if -1
        if (renderID == DEFAULT_RENDER)
        {
            switch (type)
            {
                case ENTITY:
                    return ENTITY_RENDER_ID;
                case INVENTORY:
                    return INVENTORY_RENDER_ID;
                case EQUIPPED:
                    return EQUIPPED_RENDER_ID;
                case EQUIPPED_FIRST_PERSON:
                    return FIRST_PERSON_RENDER_ID;
            }
        }
        return renderID;
    }

    public boolean shouldRenderType(IItemRenderer.ItemRenderType type, Object item)
    {
        return canRenderState(getRenderIDForState(type, item));
    }

    public void add(int id, RenderState state)
    {
        renderStates.put(id, state);
    }

    public void add(String name, RenderState state)
    {
        renderStatesByName.put(name, state);
    }
}
