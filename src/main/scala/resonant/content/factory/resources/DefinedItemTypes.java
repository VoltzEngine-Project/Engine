package resonant.content.factory.resources;

import scala.tools.nsc.doc.model.Def;

/**
 * Created by robert on 11/23/2014.
 */
public enum DefinedItemTypes
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

    private DefinedItemTypes(boolean gen)
    {
        this.shouldGenerate = gen;
    }
}
