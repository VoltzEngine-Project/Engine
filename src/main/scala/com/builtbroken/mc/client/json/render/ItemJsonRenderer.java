package com.builtbroken.mc.client.json.render;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
import com.builtbroken.mc.lib.render.RenderUtility;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/19/2016.
 */
public class ItemJsonRenderer implements IItemRenderer
{
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        if (item.getItem() instanceof IJsonRenderStateProvider)
        {
            RenderData data = ClientDataHandler.INSTANCE.getRenderData(((IJsonRenderStateProvider) item.getItem()).getRenderContentID(type, item));
            if (data != null)
            {
                return data.shouldRenderType(type);
            }
            return type != ItemRenderType.INVENTORY;
        }
        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        //TODO see if we can return true as we really don't need to check this twice
        if (item.getItem() instanceof IJsonRenderStateProvider)
        {
            RenderData data = ClientDataHandler.INSTANCE.getRenderData(((IJsonRenderStateProvider) item.getItem()).getRenderContentID(type, item));
            if (data != null)
            {
                return data.shouldRenderType(type);
            }
            return type != ItemRenderType.INVENTORY;
        }
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... dataArray)
    {
        if (item.getItem() instanceof IJsonRenderStateProvider)
        {
            RenderData data = ClientDataHandler.INSTANCE.getRenderData(((IJsonRenderStateProvider) item.getItem()).getRenderContentID(type, item));
            GL11.glPushMatrix();
            switch (type)
            {
                case ENTITY:
                    GL11.glTranslatef(0, 0.3f, 0);
                    break;
                case EQUIPPED:
                    GL11.glRotatef(-75, 1, 0, 0);
                    GL11.glRotatef(30, 0, 0, 1);
                    GL11.glRotatef(20, 0, 1, 0);
                    GL11.glTranslatef(0.2f, -0.5f, -0f);
                    GL11.glScalef(2, 2, 2);
                    break;
                case EQUIPPED_FIRST_PERSON:
                    GL11.glTranslatef(-0.4f, 1.3f, 1f);
                    GL11.glRotatef(-30, 0, 1, 0);
                    GL11.glRotatef(13, 1, 0, 0);
                    GL11.glScaled(1.8f, 1.8f, 1.8f);
                    break;
            }
            if (data == null || !data.render(type))
            {
                //Backup renderer
                RenderUtility.renderCube(0, 0, 0, 1, 1, 1, Blocks.sponge);
            }
            GL11.glPopMatrix();
        }
    }
}
