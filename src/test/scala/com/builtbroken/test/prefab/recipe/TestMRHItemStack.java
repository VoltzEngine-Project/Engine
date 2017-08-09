package com.builtbroken.test.prefab.recipe;

import com.builtbroken.mc.api.recipe.IMachineRecipe;
import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.api.recipe.RecipeRegisterResult;
import com.builtbroken.mc.framework.recipe.item.MRHandlerItemStack;
import com.builtbroken.mc.framework.recipe.item.MRItemStack;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.junit.runner.RunWith;

import java.util.Collection;

/**
 * Created by robert on 1/10/2015.
 */
@RunWith(VoltzTestRunner.class)
public class TestMRHItemStack extends AbstractTest
{
    MRHandlerItemStack handler;

    @Override
    public void setUpForEntireClass()
    {
        super.setUpForEntireClass();
        this.handler = new MRHandlerItemStack(MachineRecipeType.ITEM_CRUSHER.INTERNAL_NAME);

    }

    public void testAddRecipe()
    {
        RecipeRegisterResult result = handler.registerRecipe(new MRItemStack(MachineRecipeType.ITEM_CRUSHER.INTERNAL_NAME, Blocks.stone).addInputOption(Blocks.stone));
        assertEquals("Failed to register recipe", RecipeRegisterResult.REGISTERED, result);
    }

    public void testGetRecipe()
    {
        RecipeRegisterResult result = handler.registerRecipe(new MRItemStack(MachineRecipeType.ITEM_CRUSHER.INTERNAL_NAME, Blocks.bedrock).addInputOption(Blocks.bedrock));
        assertEquals("Failed to register recipe", RecipeRegisterResult.REGISTERED, result);

        ItemStack out = handler.getRecipe(new Object[]{new ItemStack(Blocks.bedrock)}, 0, 0);
        if(out == null)
        {
            Collection<IMachineRecipe> recipes = handler.getRecipes(new Object[]{new ItemStack(Blocks.bedrock)});
            if(recipes == null || recipes.size() == 0)
            {
                recipes = handler.getRecipes();
                if(recipes == null || recipes.size() == 0)
                {
                    fail("No recipes listed for handler");
                }
                System.out.println("Recipes Contain: " + handler.recipes.containsKey(handler.getKeyFor(new Object[]{new ItemStack(Blocks.bedrock)})));
                for(IMachineRecipe recipe : recipes)
                {
                    System.out.println("Machine Recipe: " + recipe + ", Should handler: " + recipe.shouldHandleRecipe(new Object[]{new ItemStack(Blocks.bedrock)}));
                }
                fail("Not recipes listed for input");
            }
            for(IMachineRecipe recipe : recipes)
            {
                System.out.println("Machine Recipe: " + recipe + ", Should handler: " + recipe.shouldHandleRecipe(new Object[]{new ItemStack(Blocks.bedrock)}));
            }
            fail("Output == null");
        }
        assertTrue("getRecipe should have returned bedrock", out.isItemEqual(new ItemStack(Blocks.bedrock)));
    }
}
