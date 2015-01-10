package com.builtbroken.jlib.math.dice;

import java.util.ArrayList;
import java.util.Random;

/**
 * Rolls a series of dice to get a result of true or false
 * <p/>
 * Created on 10/10/2014.
 *
 * @author Darkguardsman
 */
public class DiceRoller extends ArrayList<Dice>
{
	public static DiceRoller COIL_FLIP_TAILS = new DiceRoller(1, 2, 1, 1, 1);
	public static DiceRoller COIL_FLIP_HEADS = new DiceRoller(1, 2, 2, 2, 1);

	protected static Random rand = new Random();

	int minValueForTrue = 0;
	int maxValueForTrue = 0;
	int numDiceToMeetValue = 0;

	public DiceRoller(int valueToHit)
	{
		this(valueToHit, -1);
	}

	public DiceRoller(int valueToHit, int numDiceToMeetValue)
	{
		this(valueToHit, valueToHit, numDiceToMeetValue);
	}

	public DiceRoller(int minValueForTrue, int maxValueForTrue, int numDiceToMeetValue)
	{
		this.minValueForTrue = minValueForTrue;
		this.maxValueForTrue = maxValueForTrue;
		this.numDiceToMeetValue = numDiceToMeetValue;
	}

	public DiceRoller(int minValueForTrue, int maxValueForTrue, int numDiceToMeetValue, Dice... dice)
	{
		this(minValueForTrue, maxValueForTrue, numDiceToMeetValue);
		for (Dice di : dice)
		{
			add(di);
		}
	}

	public DiceRoller(int numOfDice, int sidesPerDice, int minValueForTrue, int maxValueForTrue, int numDiceToMeetValue)
	{
		this(minValueForTrue, maxValueForTrue, numDiceToMeetValue);

		if (sidesPerDice <= 0)
		{
			throw new IllegalArgumentException("DiceRoller: Dice must have at least one side");
		}
		if (numOfDice <= 0)
		{
			throw new IllegalArgumentException("DiceRoller: Must have at least one dice to roll");
		}
		for (int i = 0; i < numOfDice; i++)
		{
			this.add(new Dice(sidesPerDice));
		}
	}

	/**
	 * Rolls the dice returning true if enough dice match the requirements
	 */
	public boolean roll()
	{
		int trueDice = 0;
		if (size() > 0)
		{
			for (Dice dice : this)
			{
				int roll = dice.roll();
				if (roll >= minValueForTrue && roll <= maxValueForTrue)
				{
					trueDice++;
					if (trueDice >= numDiceToMeetValue)
					{
						return true;
					}
				}
			}
		}
		return trueDice >= numDiceToMeetValue || numDiceToMeetValue == -1 && trueDice == this.size();
	}
}
