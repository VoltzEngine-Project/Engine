package com.builtbroken.lib.wrapper

/**
 * @author Calclavia
 */
object ObjectOrWrapper
{

  implicit protected class ObjectOrWrapper(obj: AnyRef)
  {
    def or(alternative: AnyRef): AnyRef =
      if (obj == null)
        alternative
      else
        obj
  }

}
