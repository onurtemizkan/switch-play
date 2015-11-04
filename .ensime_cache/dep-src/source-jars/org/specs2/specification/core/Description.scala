package org.specs2
package specification
package core

import org.specs2.main.Arguments

import scalaz.Show
import org.specs2.data.{NamedTag, Tag}
import text.Regexes._

/**
 * Description of a Fragment
 */
trait Description {
  def show: String
  def matches(s: String) = false
  def stripMargin(margin: Char): Description = this
  def stripMargin: Description = stripMargin('|')
}

/**
 * Text description
 */
case class Text(text: String) extends Description {
  def show: String = text
  override def matches(s: String) = text matchesSafely s
  override def stripMargin(margin: Char) = copy(text.stripMargin(margin))
}

case class Code(text: String) extends Description {
  def show: String =
    if (text.contains("\n")) "```\n"+text+"\n```"
    else                     s"`$text`"

  override def matches(s: String) = text matches s
  override def stripMargin(margin: Char) = copy(text.stripMargin(margin))
}

/**
 * NoText description, used when creating steps and actions which are not described
 */
case object NoText extends Description {
  def show: String = ""
}

/**
 * Reference to another specification
 */
case class SpecificationRef(header: SpecHeader, arguments: Arguments, alias: String = "", tooltip: String = "", hidden: Boolean = false, muted: Boolean = false) extends Description {
  def specClassName = header.className

  def url = specClassName+".html"

  def linkText =
    if (alias.nonEmpty) alias else header.showWords

  def show = header.show

  def hide: SpecificationRef =
    copy(hidden = true)

  def mute: SpecificationRef =
    copy(muted = true)
}

object SpecificationRef {
  def create(specificationStructure: =>SpecificationStructure): SpecificationRef =
    create(specificationStructure.is)

  def create(specStructure: SpecStructure): SpecificationRef =
    SpecificationRef(specStructure.header, arguments = specStructure.arguments, alias = specStructure.header.showWords)
}

/**
 * Break (== new line)
 */
case object Br extends Description {
  def show = "\n"
}

/**
 * Start of a block. This is used to delimit the blocks in mutable specifications and
 * know exactly how to create levels when transforming a specification to a tree of examples (for JUnit for example)
 */
case object Start extends Description {
  def show = ""
}

/**
 * End of a block
 */
case object End extends Description {
  def show = ""
}

/**
 * The next fragment must be indented
 */
case class Tab(n: Int = 1) extends Description {
  def show = ""
}

/**
 * The next fragment must be un-indented
 */
case class Backtab(n: Int = 1) extends Description {
  def show = ""
}

/**
 * Description of a Tag fragment
 */
case class Marker(tag: NamedTag, isSection: Boolean = false, appliesToNext: Boolean = true) extends Description {
  def show = ""

  override def toString =
    s"Marker($tag, isSection = $isSection, appliesToNext = $appliesToNext)"

}

/**
 * Creation methods for Descriptions
 */
object Description {

  def text(text: String) = Text(text)
  def code(text: String) = Code(text)

  def tag(ts: String*)       = mark(Tag(ts:_*))
  def taggedAs(ts: String*)  = markAs(Tag(ts:_*))
  def section(ts: String*)   = markSection(Tag(ts:_*))
  def asSection(ts: String*) = markSectionAs(Tag(ts:_*))

  def mark(tag: NamedTag)          = Marker(tag, isSection = false)
  def markAs(tag: NamedTag)        = Marker(tag, isSection = false, appliesToNext = false)
  def markSection(tag: NamedTag)   = Marker(tag, isSection = true)
  def markSectionAs(tag: NamedTag) = Marker(tag, isSection = true, appliesToNext = false)

  implicit def showInstance: Show[Description] = new Show[Description] {
    override def shows(d: Description): String =
      d match {
        case Text(t) => t
        case _ => d.toString
      }
  }
}

