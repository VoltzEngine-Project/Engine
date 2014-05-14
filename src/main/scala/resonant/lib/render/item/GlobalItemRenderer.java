package resonant.lib.render.item;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import resonant.api.items.ISimpleItemRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** @author Calclavia */
@SideOnly(Side.CLIENT)
public class GlobalItemRenderer implements IItemRenderer
{
    public static final GlobalItemRenderer INSTANCE = new GlobalItemRenderer();
    private static final HashMap<Integer, ISimpleItemRenderer> RENDERER = new HashMap<Integer, ISimpleItemRenderer>();

    public static void register(int id, ISimpleItemRenderer renderer)
    {
        MinecraftForgeClient.registerItemRenderer(id, GlobalItemRenderer.INSTANCE);
        RENDERER.put(id, renderer);
    }

    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType type)
    {
        return RENDERER.containsKey(itemStack.getItem().itemID);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data)
    {
        GL11.glPushMatrix();

        RENDERER.get(itemStack.getItem().itemID).renderInventoryItem(itemStack);

        GL11.glPopMatrix();
    }

}
