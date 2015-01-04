package com.builtbroken.lib.wrapper

import java.util.Random

/**
 * An extension method for Java's random
 * @author Calclavia
 */
object RandomWrapper
{

  implicit class RichRandom(val underlying: Random)
  {
    /**
     * Generates a random value between -amount to +amount
     * @param amount - Amount of deviation
     * @return
     */
    def deviate(amount: Double = 1) = (underlying.nextDouble() - 0.5) * 2 * amount

    def range(from: Double, to: Double) = underlying.nextDouble() * (to - from) + from
  }

}
