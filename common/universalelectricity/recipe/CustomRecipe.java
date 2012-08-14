package universalelectricity.recipe;

public class CustomRecipe
{
	public String name;
    public Object[] output;
    public Object[] input;

    public CustomRecipe(String name, Object[] output, Object[] input)
    {
    	this.name = name;
        this.output = output;
        this.input = input;
    }
}
