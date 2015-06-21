package com.builtbroken.jlib.data.science;

/**
 * List of element from the periodic table of elements for any kind of use. This list is not
 * complete for all parts but each element should have a listed names, symbol, and atomic mass.
 * Atomic number should be the placement # in the list. Var ZERO should not be used as its designed
 * to offset the placement of all elements by one. As well is returned instead of null.
 *
 * @author Robert Seifert
 * @Source http://www.periodictable.com/Properties/A/SpecificHeat.an.html
 * @source http://www.chemicalelements.com/
 * @source http://www.lenntech.com/periodic/periodic-chart.htm
 */
public enum ChemElement
{
	/**
	 * Placeholder so that hydrogen starts as number one
	 */
	ZERO("ZERO", "ZERO", 0, 0, null, null),
	Hydrogen("Hydrogen", "H", 1.00794f, 0.08988f, ElementProperty.nonmetal, MatterPhase.GAS, new HeatingData(14.01f, 20.28f, 0.558f, 0.558f, 14300f)),
	Helium("Helium", "He", 4.002602f, 0.1785f, ElementProperty.inertGas, MatterPhase.GAS, new HeatingData(0, 4.22f, 0.02f, 0.083f, 5193.1f)),
	Lithium("Lithium", "Li", 6.941f, 0.53f, ElementProperty.inertGas, MatterPhase.GAS, new HeatingData(543.69f, 1615f, 3f, 147f, 3570f)),
	Beryllium("Beryllium", "Be", 9.012182f, 1.8477f, ElementProperty.inertGas, MatterPhase.GAS, new HeatingData(1560f, 2743f, 7.95f, 297f, 1820f)),
	Boron("Boron", "B", 10.811f, 2.46f, ElementProperty.inertGas, MatterPhase.GAS, new HeatingData(2348f, 4273f, 50f, 507f, 1030f)),
	Carbon("Carbon", "C", 12.0107f, 2.26f, ElementProperty.inertGas, MatterPhase.GAS, new HeatingData(3823f, 4300f, 105f, 715f, 710f)),
	Nitrogen("Nitrogen", "N", 14.0067f, 1.251f, ElementProperty.inertGas, MatterPhase.GAS, new HeatingData(63.05f, 77.36f, 0.36f, 2.79f, 1040)),
	Oxygen("Oxygen", "O", 15.9994f, 1.429f, ElementProperty.inertGas, MatterPhase.GAS, new HeatingData(54.8f, 90.2f, 0.222f, 3.41f, 919f)),
	Fluorine("Fluorine", "F", 18.9994f, 1.696f, ElementProperty.inertGas, MatterPhase.GAS, new HeatingData(53.5f, 85.03f, 0.26f, 3.27f, 824f)),
	Neon("Neon", "Ne", 20.1797f, 0.9f, ElementProperty.inertGas, MatterPhase.GAS, new HeatingData(24.56f, 27.07f, 0.34f, 1.75f, 1030f)),
	Sodium("Sodium", "Na", 22.98976928f, 0.968f, ElementProperty.alkaliMetal, MatterPhase.SOLID, new HeatingData(370.87f, 1156f, 2.6f, 97.7f, 1230f)),

	Magnesium("Magnesium", "Mg", 24.305f, 1.738f, ElementProperty.alkalineEarthMetal, MatterPhase.SOLID),
	aluminium("aluminium", "Al", 26.9815386f, 2.7f, ElementProperty.otherMetal, MatterPhase.SOLID),
	Silicon("Silicon", "Si", 28.0855f, 2.33f, ElementProperty.otherMetal, MatterPhase.SOLID),

	Phosphorus("Phosphorus", "P", 30.973762f, 1.823f, ElementProperty.nonmetal, MatterPhase.SOLID),
	Sulphur("Sulphur", "S", 32.065f, 1.96f, ElementProperty.nonmetal, MatterPhase.SOLID),
	Chlorine("Chlorine", "Cl", 35.453f, 3.214f, ElementProperty.halogen, MatterPhase.GAS),

	Argon("Argon", "Ar", 39.948f, 1.784f, ElementProperty.inertGas, MatterPhase.GAS),
	Potassium("Potassium", "K", 39.0983f, 0.856f, ElementProperty.alkaliMetal, MatterPhase.SOLID),
	Calcium("Calcium", "Ca", 40.078f, 1.55f, ElementProperty.alkalineEarthMetal, MatterPhase.SOLID),
	Scandium("Scandium", "Sc", 44.955912f, 2.985f, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Titanium("Titanium", "Ti", 47.867f, 4.507f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Vanadium("Vanadium", "V", 50.9415f, 6.11f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Chromium("Chromium", "Cr", 51.9961f, 7.14f, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Manganese("Manganese", "Mn", 54.938045f, 7.47f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Iron("Iron", "Fe", 55.845f, 7.874f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Cobalt("Cobalt", "Co", 58.933195f, 8.9f, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Nickel("Nickel", "Ni", 58.6934f, 8.908f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Copper("Copper", "Cu", 63.546f, 8.92f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Zinc("Zinc", "Zn", 65.38f, 7.14f, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Gallium("Gallium", "Ga", 69.723f, 5.904f, ElementProperty.otherMetal, MatterPhase.SOLID),
	Germanium("Germanium", "Ge", 72.64f, 5.323f, ElementProperty.semimetallic, MatterPhase.SOLID),
	Arsenic("Arsenic", "As", 74.9216f, 5.727f, ElementProperty.semimetallic, MatterPhase.SOLID),

	Selenium("Selenium", "Se", 78.96f, 4.819f, ElementProperty.nonmetal, MatterPhase.SOLID),
	Bromine("Bromine", "Br", 79.904f, 3.12f, ElementProperty.halogen, MatterPhase.LIQUID),
	Krypton("Krypton", "Kr", 83.798f, 3.75f, ElementProperty.inertGas, MatterPhase.GAS),

	Rubidium("Rubidium", "Rb", 85.4678f, 1.532f, ElementProperty.alkaliMetal, MatterPhase.SOLID),
	Strontium("Strontium", "Sr", 87.62f, 2.63f, ElementProperty.alkalineEarthMetal, MatterPhase.SOLID),
	Yttrium("Yttrium", "Y", 88.90585f, 4.472f, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Zirkonium("Zirkonium", "Zr", 91.224f, 6.511f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Niobium("Niobium", "Nb", 92.90638f, 8.57f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Molybdaenum("Molybdaenum", "Mo", 95.96f, 10.28f, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Technetium("Technetium", "Tc", 98f, 11.5f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Ruthenium("Ruthenium", "Ru", 101.07f, 12.37f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Rhodium("Rhodium", "Rh", 102.9055f, 12.45f, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Palladium("Palladium", "Pd", 106.42f, 12.023f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Silver("Silver", "Ag", 107.8682f, 10.49f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Cadmium("Cadmium", "Cd", 112.411f, 8.65f, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Indium("Indium", "In", 114.818f, 7.31f, ElementProperty.otherMetal, MatterPhase.SOLID),
	Tin("Tin", "Sn", 118.71f, 7.31f, ElementProperty.otherMetal, MatterPhase.SOLID),
	Antimony("Antimony", "Sb", 121.76f, 6.697f, ElementProperty.semimetallic, MatterPhase.SOLID),

	Tellurium("Tellurium", "Te", 127.6f, 6.24f, ElementProperty.semimetallic, MatterPhase.SOLID),
	Iodine("Iodine", "I", 126.90447f, 4.94f, ElementProperty.halogen, MatterPhase.SOLID),
	Xenon("Xenon", "Xe", 131.293f, 5.9f, ElementProperty.inertGas, MatterPhase.GAS),

	Cesium("Cesium", "Cs", 132.9054519f, 1.879f, ElementProperty.alkaliMetal, MatterPhase.SOLID),
	Barium("Barium", "Ba", 137.327f, 3.51f, ElementProperty.alkalineEarthMetal, MatterPhase.SOLID),
	Lanthanum("Lanthanum", "La", 138.90547f, 6.146f, ElementProperty.lanthanide, MatterPhase.SOLID),

	Cerium("Cerium", "Ce", 140.116f, 6.689f, ElementProperty.lanthanide, MatterPhase.SOLID),
	Praseodymium("Praseodymium", "Pr", 140.90765f, 6.64f, ElementProperty.lanthanide, MatterPhase.SOLID),
	Neodymium("Neodymium", "Nd", 144.242f, 7.01f, ElementProperty.lanthanide, MatterPhase.SOLID),

	Promethium("Promethium", "Pm", 145f, 7.264f, ElementProperty.lanthanide, MatterPhase.SOLID),
	Samarium("Samarium", "Sm", 150.36f, 7.353f, ElementProperty.lanthanide, MatterPhase.SOLID),
	Europium("Europium", "Eu", 151.964f, 5.244f, ElementProperty.lanthanide, MatterPhase.SOLID),

	Gadolinium("Gadolinium", "Gd", 157.25f, 7.901f, ElementProperty.lanthanide, MatterPhase.SOLID),
	Terbium("Terbium", "Tb", 158.92535f, 8.219f, ElementProperty.lanthanide, MatterPhase.SOLID),
	Dysprosium("Dysprosium", "Dy", 162.5001f, 8.551f, ElementProperty.lanthanide, MatterPhase.SOLID),

	Holmium("Holmium", "Ho", 164.93032f, 8.795f, ElementProperty.lanthanide, MatterPhase.SOLID),
	Erbium("Erbium", "Er", 167.259f, 9.066f, ElementProperty.lanthanide, MatterPhase.SOLID),
	Thulium("Thulium", "Tm", 168.93421f, 9.321f, ElementProperty.lanthanide, MatterPhase.SOLID),

	Ytterbium("Ytterbium", "Yb", 173.054f, 6.57f, ElementProperty.lanthanide, MatterPhase.SOLID),
	Lutetium("Lutetium", "Lu", 174.9668f, 9.841f, ElementProperty.lanthanide, MatterPhase.SOLID),
	Hafnium("Hafnium", "Hf", 178.49f, 13.31f, ElementProperty.lanthanide, MatterPhase.SOLID),

	Tantalum("Tantalum", "Ta", 180.94788f, 16.65f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Tungsten("Tungsten", "W", 183.84f, 19.25f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Rhenium("Rhenium", "Re", 186.207f, 21.02f, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Osmium("Osmium", "Os", 190.23f, 22.59f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Iridium("Iridium", "Ir", 192.217f, 22.56f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Platinum("Platinum", "Pt", 192.084f, 21.09f, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Gold("Gold", "Au", 196.966569f, 19.3f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Hydrargyrum("Hydrargyrum", "Hg", 200.59f, 13.534f, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Thallium("Thallium", "Tl", 204.3833f, 11.85f, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Lead("Lead", "Pb", 207.2f, 11.34f, ElementProperty.otherMetal, MatterPhase.SOLID),
	Bismuth("Bismuth", "Bi", 208.980401f, 9.78f, ElementProperty.otherMetal, MatterPhase.SOLID),
	Polonium("Polonium", "Po", 210f, 9.196f, ElementProperty.semimetallic, MatterPhase.SOLID),

	Astatine("Astatine", "At", 210f, 0, ElementProperty.halogen, MatterPhase.SOLID),
	Radon("Radon", "Rn", 220f, 9.73f, ElementProperty.inertGas, MatterPhase.GAS),
	Francium("Francium", "Fr", 223f, 0f, ElementProperty.alkaliMetal, MatterPhase.SOLID),

	Radium("Radium", "Ra", 226f, 5f, ElementProperty.alkalineEarthMetal, MatterPhase.SOLID),
	Actinium("Actinium", "Ac", 227f, 10.07f, ElementProperty.actinide, MatterPhase.SOLID),
	Thorium("Thorium", "Th", 232.03806f, 11.724f, ElementProperty.actinide, MatterPhase.SOLID),

	Protactinium("Protactinium", "Pa", 231.03588f, 15.37f, ElementProperty.actinide, MatterPhase.SOLID),
	Uranium("Uranium", "U", 238.02891f, 19.05f, ElementProperty.actinide, MatterPhase.SOLID),
	Neptunium("Neptunium", "Np", 237f, 20.45f, ElementProperty.actinide, MatterPhase.SOLID),

	Plutonium("Plutonium", "Pu", 244f, 19.816f, ElementProperty.actinide, MatterPhase.SOLID),
	Americium("Americium", "Am", 243f, 13.67f, ElementProperty.actinide, MatterPhase.SOLID),
	Curium("Curium", "Cm", 247f, 3.51f, ElementProperty.actinide, MatterPhase.SOLID),

	Berkelium("Berkelium", "Bk", 247f, 14.78f, ElementProperty.actinide, MatterPhase.SOLID),
	Californium("Californium", "Cf", 251f, 15.1f, ElementProperty.actinide, MatterPhase.SOLID),
	Einsteinium("Einsteinium", "Es", 252f, 0, ElementProperty.actinide, MatterPhase.SOLID),

	Fermium("Fermium", "Fm", 257f, 0, ElementProperty.actinide, MatterPhase.SOLID),
	Mendelevium("Mendelevium", "Md", 0, 258f, ElementProperty.actinide, MatterPhase.SOLID),
	Nobelium("Nobelium", "No", 259f, 0, ElementProperty.actinide, MatterPhase.SOLID),

	Lawrencium("Lawrencium", "Lr", 262f, 0, ElementProperty.actinide, MatterPhase.SOLID),
	Rutherfordium("Rutherfordium", "Rf", 261f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Dubnium("Dubnium", "Db", 262f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Seaborgium("Seaborgium", "Sg", 266f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Bohrium("Bohrium", "Bh", 264f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Hassium("Hassium", "Hs", 277f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Meitnerium("Meitnerium", "Mt", 268f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Ununnilium("Ununnilium", "Ds", 271f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Unununium("Unununium", "Rg", 272f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Ununbium("Ununbium", "Uub", 285f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Ununtrium("Ununtrium", "Uut", 284f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Ununquadium("Ununquadium", "Uuq", 289f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),

	Ununpentium("Ununpentium", "Uup", 288f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID),
	Ununhexium("Ununhexium", "Uuh", 292f, 0, ElementProperty.transitionMetal, MatterPhase.SOLID);

	/**
	 * value units are (g/cm^3) aka grams per centimeter cubed
	 */
	public float density;
	/**
	 * value units are (amu) aka atomic mass unit
	 */
	public float atomicMass;

	public String elementName = "element";
	public String[] elementNames;
	public String elementSymbol = "element";

	public ElementProperty classification;

	public MatterPhase normalPhase;

	public HeatingData heatData;

	ChemElement(String[] name, String symbol, float atomicMass, float density, ElementProperty type, MatterPhase defaultPhase)
	{
		this(name[0], symbol, atomicMass, density, type, defaultPhase);
		this.elementNames = name;
	}

	ChemElement(String name, String symbol, float atomicMass, float density, ElementProperty type, MatterPhase defaultPhase)
	{
		this.elementName = name;
		this.elementSymbol = symbol;
		this.atomicMass = atomicMass;
		this.classification = type;
		this.normalPhase = defaultPhase;
		this.density = density;
	}

	ChemElement(String name, String symbol, float atomicMass, float density, ElementProperty type, MatterPhase defaultPhase, HeatingData heatData)
	{
		this(name, symbol, atomicMass, density, type, defaultPhase);
		this.heatData = heatData;

	}

}
