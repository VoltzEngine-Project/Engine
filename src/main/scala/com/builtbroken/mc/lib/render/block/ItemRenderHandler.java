package com.builtbroken.mc.lib.render.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import com.builtbroken.mc.api.items.ISimpleItemRenderer;

import java.util.HashMap;

/**
 * An easy way to globally register item renderers.
 *
 * @author Calclavia
 */
@SideOnly(Side.CLIENT)
@Deprecated
public class ItemRenderHandler implements IItemRenderer
{
	public static final ItemRenderHandler instance = new ItemRenderHandler();
	private static final HashMap<Item, ISimpleItemRenderer> renderer = new HashMap();

	public static void register(Item item, ISimpleItemRenderer renderer)
	{
		MinecraftForgeClient.registerItemRenderer(item, ItemRenderHandler.instance);
		ItemRenderHandler.renderer.put(item, renderer);
	}

	@Override
	public boolean handleRenderType(ItemStack itemStack, ItemRenderType type)
	{
		return renderer.containsKey(itemStack.getItem());
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
		renderer.get(itemStack.getItem()).renderInventoryItem(type, itemStack, data);
		GL11.glPopMatrix();
	}

}
