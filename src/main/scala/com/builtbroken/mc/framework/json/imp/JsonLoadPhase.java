package com.builtbroken.mc.framework.json.imp;

/**
 * Enum of different phases JsonContent load will run through during mod loading
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2017.
 */
public enum JsonLoadPhase
{
    //Pre-Init in 1.7.10, after Pre-Init in 1.12
    /** Called after all handlers are registered */
    HANDLERS,
    /** Called after blocks are done registering */
    BLOCKS,
    /** Called after items are done registering */
    ITEMS,
    /** Called after all content is done registering */
    CONTENT,
    /** Called after entities are done registering */
    ENTITIES,
    /** Called after all client side assets are done registering */
    ASSETS,

    //Pre-Init
    /** Called at the end of pre-init phase */
    LOAD_PHASE_ONE,

    //Init
    /** Called after all recipes are loaded */
    RECIPES,
    /** Called at the end of init phase */
    LOAD_PHASE_TWO,

    //Post init
    /** Called at the end of post phase */
    LOAD_PHASE_THREE,

    //Completed
    /** Called at the end of load complete phase but before data cleanup */
    COMPLETED
}
