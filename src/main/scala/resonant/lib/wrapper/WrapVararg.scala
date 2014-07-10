package resonant.lib.wrapper

/**
 * Used to wrap sequences of generic "Any" into "AnyRef" for VarArg usages.
 * @author Calclavia
 */
object WrapVararg
{

  implicit class WrapVarArgAsAnyRef[+A](iterable: Seq[A])
  {
    def toAnyRef: Seq[AnyRef] = iterable map (_.asInstanceOf[AnyRef])
  }

}
