package com.builtbroken.mc.core.content.fluids;

import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;

public enum EnumFluids
{
    HYDROGEN("hydrogen", true, 1, 50, EnumRarity.common, "H2"),
    OXYGEN("oxygen", true, 1, 100, EnumRarity.common, "O2"),
    AMMONIA("ammonia", true, 1, 100, EnumRarity.uncommon, "NH3"),
    NITRICACID("nitricAcid", false, 1512, 1500, EnumRarity.uncommon, "HNO3"),
    NITROGENDIOXIDE("nitrogenDioxide", true, 2620, 500, EnumRarity.common, "NO2"),
    URANYLNITRATE("uranylNitrate", false, 2810, 2000, EnumRarity.rare, "UO2(NO3)2"),
    AMMONIUMDIURANATE("amoniumDiuranate", false, 2810, 2000, EnumRarity.rare, "(NH4)2U2O7"),
    URANIUMDIOXIDE("uraniumDioxide", false, 10970, 4000, EnumRarity.rare, "UO2")

    ;

    private String name;
    private boolean gaseous;
    private int density;
    private int viscosity;
    private String forumla;

    private Fluid fluid;

    EnumFluids(String name, boolean gaseous, int density, int viscosity, EnumRarity rarity, String formula){
        this.name = name;
        this.gaseous = gaseous;
        this.density = density;
        this.viscosity = viscosity;
        this.forumla = formula;
        this.fluid = new Fluid(name).setGaseous(gaseous).setDensity(density).setViscosity(viscosity).setRarity(rarity);
    }

    EnumFluids(String name, boolean gaseous, int density, int viscosity, EnumRarity rarity){
        this(name, gaseous, density, viscosity, rarity, "");
    }
}
