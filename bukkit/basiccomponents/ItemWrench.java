package basiccomponents;


/**
 * The Class ItemWrench.
 */
public class ItemWrench extends BCItem
{
    
    /**
     * Instantiates a new item wrench.
     *
     * @param i the i
     * @param j the j
     */
    public ItemWrench(int i, int j)
    {
        super("Wrench", i, j);
        textureId = j;
    }
}
