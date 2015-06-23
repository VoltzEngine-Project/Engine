package com.builtbroken.jlib.type;

/**
 * Container for two objects that need to be linked to each other
 *
 * @author Robert Seifert
 */
public class Pair<L, R>
{
    private L left;
    private R right;

    public Pair(L left, R right)
    {
        this.left = left;
        this.right = right;
    }

    public L left()
    {
        return left;
    }

    public R right()
    {
        return right;
    }

    public void setLeft(L l)
    {
        left = l;
    }

    public void setRight(R r)
    {
        right = r;
    }

    @Override
    public int hashCode()
    {
        return (left == null || right == null) ? super.hashCode() : left.hashCode() ^ right.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof Pair ? this.left.equals(((Pair) o).left()) && this.right.equals(((Pair) o).right()) : false;
    }

    @Override
    public String toString()
    {
        return "Pair[l='" + left + "' r='" + right + "']";
    }
}