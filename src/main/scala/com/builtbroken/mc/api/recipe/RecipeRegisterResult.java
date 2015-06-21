package com.builtbroken.mc.api.recipe;

/** Used to inform a dev with more details why the
 * recipe did not register correctly.
 * Created by robert on 1/9/2015.
 */
public enum RecipeRegisterResult
{
    /** Recipe is missing some data */
    INCOMPLETE,
    /** Recipe is not for the handler's machine type */
    INVALID_TYPE,
    /** Input items are not supported */
    INVALID_INPUT,
    /** Output items are not supported */
    INVALID_OUTPUT,
    /** Recipe matching the inputs already exists */
    ALREADY_EXISTS,
    /** Everything went correct, recipe is now registered */
    REGISTERED,
    /** Handler is null, only used for the MachineRecipeType enum */
    NO_HANDLER,
    /** Failed with an unknown error */
    FAILED
}
