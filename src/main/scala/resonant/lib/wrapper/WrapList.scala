package resonant.lib.wrapper

import java.util

/**
 * @author Calclavia
 */
object WrapList
{

  implicit class ListWithGenericAdd[T](list: util.List[T])
  {
    def add(value: Any) = list.add(value.asInstanceOf[T])
  }

}
