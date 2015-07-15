package com.builtbroken.mc.prefab.tile.multiblock;

import net.minecraft.block.ITileEntityProvider;

/** Enum of different mutliblock tiles that can be used to restrict connections
 * Created by Dark on 7/4/2015.
 */
public enum EnumMultiblock
{
    /* Basic */
    /* 0 */TILE,
    /* 1 */TANK,
    /* 2 */INVENTORY,
    /* 3 */ENERGY,
    /* 4 */MECH,
    /* Combinations */
    /* 5 */TANK_INV,
    /* 6 */TANK_ENERGY,
    /* 7 */INV_ENERGY,
    /* 8 */MECH_INV,
    /* 9 */MECH_ENERGY,
    /* 10 */MECH_TANK,
    /* 11 */TANK_INV_ENERGY,;

    private ITileEntityProvider creator;
}
