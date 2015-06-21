package com.builtbroken.mc.core.content.resources;

/** List of known items to generate per material
 * Created by robert on 11/23/2014.
 */
public enum DefinedGenItems
{
    DUST(true),
    RUBBLE(true),
    INGOT(true),
    PLATE(true),
    ROD(true),
    GEAR(true),
    AX_HEAD(false),
    SHOVEL_HEAD(false),
    SWORD_BLADE(false),
    PICK_HEAD(false),
    HOE_HEAD(false);

    public boolean shouldGenerate;
    public boolean requested = false;

    DefinedGenItems(boolean gen)
    {
        this.shouldGenerate = gen;
    }
}
