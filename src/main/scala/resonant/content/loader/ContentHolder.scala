package resonant.content.loader

import resonant.lib.modproxy.ILoadable

/**
 * Extend this trait, and load all content of the mod in the body of the trait. Registration and recipes will be automatically handled.
 *
 * In the mod class, the content holder must be applied to the mod proxy.
 *
 * @author Calclavia
 */
trait ContentHolder extends ContentLoader with RecipeHolder with ILoadable
{

}
