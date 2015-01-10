package com.builtbroken.jlib.math.dice;

/**
 * Simple was to encapsulate the dice roll action
 * Created on 10/10/2014.
 *
 * @author Darkguardsman
 */
public class Dice
{
	private int sides = 0;

	public Dice(int sides)
	{
		this.sides = sides;
	}

	/**
	 * Rolls the side and @Returns the value
	 */
	public int roll()
	{
		return 1 + DiceRoller.rand.nextInt(sides - 1);
	}

	/**
	 * Rolls the dice then checks if it matches @param value and @return true if it matches
	 */
	public boolean rollMatch(int value)
	{
		return roll() == value;
	}

	/**
	 * Rolls the dice then checks if its between two values.
	 * If low and high point are switch this can be used to exclude values between
	 *
	 * @param low  - lowest point of the two
	 * @param high - highest point of the two
	 * @return true if its bettwen the two values
	 */
	public boolean rollBetweenInclude(int low, int high)
	{
		return rollBetweenInclude(low, high, false);
	}

	/**
	 * Rolls the dice then checks if its between two values.
	 * If low and high point are switch this can be used to exclude values between
	 *
	 * @param low       - lowest point of the two
	 * @param high      - highest point of the two
	 * @param exclusive - true will not use the end points
	 * @return true if its bettwen the two values
	 */
	public boolean rollBetweenInclude(int low, int high, boolean exclusive)
	{
		int roll = roll();

		if (exclusive)
		{
			return roll > low && roll < high;
		}
		else
		{
			return roll >= low && roll <= high;
		}
	}

	/**
	 * Num of sides of the dice, doesn't assume continues numbers
	 */
	public int getSides()
	{
		return sides;
	}

	public Dice setSide(int sides)
	{
		this.sides = sides;
		return this;
	}
}
