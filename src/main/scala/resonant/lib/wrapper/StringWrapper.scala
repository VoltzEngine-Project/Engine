package resonant.lib.wrapper

import java.util.{List => JList}

import com.google.common.base.CaseFormat
import net.minecraft.client.resources.I18n
import org.apache.commons.lang3.text.WordUtils
import scala.collection.convert.wrapAll._
/**
 * @author Calclavia
 */
object StringWrapper
{

  implicit class WrappedString(str: String)
  {
    def getLocal: String = I18n.format(str)

    def listWrap(characters: Int): JList[String] = WordUtils.wrap(str, characters).split("\\n").toList

    def wrap(characters: Int): Array[String] = WordUtils.wrap(str, characters).split("\\n")

    def capitalizeFirst: String = str.substring(0, 1).toUpperCase + str.substring(1)

    def decapitalizeFirst: String = str.substring(0, 1).toLowerCase + str.substring(1)

    def toCamelCase: String = str.toPascalCase.decapitalizeFirst

    def toPascalCase: String =
    {
      val parts: Array[String] = str.split("_")
      var camelCaseString: String = ""
      for (part <- parts)
      {
        camelCaseString = camelCaseString + (part.toProperCase)
      }
      return camelCaseString
    }

    def camelToLowerUnderscore: String = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str)

    def camelToReadable: String = str.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ").capitalizeFirst

    def underscoreToCamel: String = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str)

    def toProperCase: String = str.substring(0, 1).toUpperCase + str.substring(1).toLowerCase
  }

}
