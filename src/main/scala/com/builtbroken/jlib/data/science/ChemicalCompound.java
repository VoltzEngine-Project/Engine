package com.builtbroken.jlib.data.science;

//TODO replace with localized data base, or data file to avoid loading up a ton of combinations when not needed
public enum ChemicalCompound
{
	/**
	 * http://encyclopedia.airliquide.com/encyclopedia.asp?GasID=8#GeneralData
	 */
	BUTANE("Butane", "C4H10", MatterPhase.GAS, 58.12f, 2.48f, new HeatingData(133f, 274f, 1379.23f, 6634.23f, 88f)),
	METHANE("Methane", "CH4", MatterPhase.GAS, 16.043f, 1.819f, new HeatingData(90.65f, 111.55f, 3656.67f, 31789.56f, 27f)),
	WATER("Water", "H20", MatterPhase.LIQUID, 18.01528f, 1000f, new HeatingData(274.15f, 373.13f, 18539.817f, 126004.1476f, 4.24f)),
	AIR("Air", "", MatterPhase.GAS, 29f, .125f, null);
    //http://science.jrank.org/pages/6523/Stone-Masonry-Chemical-composition.html
    //SILICON_DIOXIDE("Siliceous stone", "SiO2", MatterPhase.SOLID),
    //("Argillaceous stone", "AlO2", MatterPhase.SOLID);

	/**
	 * Formula
	 */
	public final String formula;
	/**
	 * IUPAC ID
	 */
	public final String compoundName;
	/**
	 * g/mol
	 */
	public final float molarMass;
	/**
	 * g/cm^3
	 */
	public final float density;

	public final MatterPhase defaultPhase;

	public final HeatingData heatingData;

	ChemicalCompound(String name, String formula, MatterPhase phase, float molarMass, float density, HeatingData data)
	{
		this.compoundName = name;
		this.formula = formula;
		this.molarMass = molarMass;
		this.density = density;
		this.defaultPhase = phase;
		this.heatingData = data;
	}
}
