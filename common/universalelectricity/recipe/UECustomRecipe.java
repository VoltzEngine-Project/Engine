package universalelectricity.recipe;

public class UECustomRecipe
{
	public String name;
    public Object[] output;
    public Object[] input;

    public UECustomRecipe(String name, Object[] output, Object[] input)
    {
    	this.name = name;
        this.output = output;
        this.input = input;
    }
}
