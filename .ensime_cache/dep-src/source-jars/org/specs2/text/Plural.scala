package org.specs2
package text


/**
 * This trait provides functions to transform nouns to their plural form
 */
private[specs2] 
trait Plural {
  
  /** @return a Noun object which can be pluralized */
  implicit class Noun(s: String) {
    def plural(vs: Iterable[Any]): String = s.plural(vs.size)
    def plural(v: Int): String     = if (v > 1) s+"s" else s
    def plural(v: Long): String    = if (v > 1) s+"s" else s

    def bePlural(v: Int)  = s.plural(v) + " " + beVerbPlural(v)
    def bePlural(v: Long) = s.plural(v) + " " + beVerbPlural(v)
  }

  def beVerbPlural(v: Int)  = if (v > 1) "are" else "is"
  def beVerbPlural(v: Long) = if (v > 1) "are" else "is"

  /** @return a Quantity which can be applied to a string to pluralize it */
  implicit class Quantity(i: Int) {
    /** @return a pluralized string describing this quantity */
    def qty(s: String) = i.toString + " " + s.plural(i)
    /** @return a pluralized string describing this quantity with the be verb */
    def beQty(s: String) = i.toString + " " + s.bePlural(i)
    /** @return a Option with a pluralized string describing this quantity if it is greater than 0 */
    def optQty(s: String): Option[String] = if (i > 0) Some(qty(s)) else None
    /** @return a pluralized string describing this quantity if it is greater than 0 or an empty string */
    def strictlyPositiveOrEmpty(s: String): String = if (i > 0) qty(s) else ""
    /**
     * @return a Option with a non-pluralized string describing this quantity if it is
     * greater than 0
     */
    def optInvariantQty(s: String): Option[String] = if (i > 0) Some(i.toString+" "+s) else None
  }

  /** @return an Ordinal which can have a rank in a sequence */
  implicit class Ordinal(i: Int) {
    /**
     * @return the proper postfix for an ordinal number
     */
    def th = i.toString +
      (if      (i == 1) "st"
       else if (i == 2) "nd"
       else if (i == 3) "rd"
       else             "th")
  }
}

private[specs2] 
object Plural extends Plural
