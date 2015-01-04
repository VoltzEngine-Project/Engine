package com.builtbroken.lib.mod.content

import net.minecraft.block.Block
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.oredict.{OreDictionary, ShapedOreRecipe, ShapelessOreRecipe}
import com.builtbroken.lib.utility.recipe.Recipes

/**
 * Recipe management syntax enhancements.
 *
 * @author anti344, Calclavia
 */
trait RecipeHolder
{
  val recipes = Recipes

  protected def shaped(output: Item, input: Any*): IRecipe =
    new ShapedOreRecipe(output, convertToMinecraft(input): _*)

  protected def shaped(output: Block, input: Any*): IRecipe =
    new ShapedOreRecipe(output, convertToMinecraft(input): _*)

  private def convertToMinecraft(params: Seq[Any]): Seq[AnyRef] =
    params.flatMap
    {
      case ch: Char =>
        Seq(Char.box(ch))
      case (ch: Char, obj: AnyRef) =>
        Seq(Char.box(ch), obj)
      case (obj: AnyRef, size: Int) =>
        Seq.fill(size)(obj)
      case obj: AnyRef =>
        Seq(obj)
      case _ =>
        Seq()
    }

  protected def shaped(output: ItemStack, input: Any*): IRecipe =
    new ShapedOreRecipe(output, convertToMinecraft(input): _*)

  protected def shapeless(output: ItemStack, input: Any*): IRecipe =
    new ShapelessOreRecipe(output, convertToMinecraft(input): _*)

  protected def shapeless(output: Item, input: Any*): IRecipe =
    new ShapelessOreRecipe(output, convertToMinecraft(input): _*)

  protected def shapeless(output: Block, input: Any*): IRecipe =
    new ShapelessOreRecipe(output, convertToMinecraft(input): _*)

  protected def smelting(input: ItemStack, output: ItemStack, xp: Double = 0.0): (ItemStack, ItemStack, Float) =
    (input, output, xp.toFloat)

  implicit protected def blockToStack(block: Block): ItemStack =
    new ItemStack(block)

  implicit protected def itemToStack(item: Item): ItemStack =
    new ItemStack(item)

  implicit protected class OredictOrWrapper(str: String)
  {
    def or(stack: ItemStack): AnyRef =
      if (OreDictionary.getOres(str).isEmpty)
        stack
      else
        str
  }

  /**
   * Configuration options
   */
  implicit protected class ConfigWrapper(recipe: IRecipe)
  {
    final val configCategory = "crafting"

    def config(config: Configuration, allow: Boolean = true): IRecipe =
    {
      val name = recipe.getRecipeOutput.getDisplayName

      if (config.get(configCategory, "Allow " + name, allow).getBoolean(allow))
        return recipe

      return null
    }
  }

}
