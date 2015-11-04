package org.specs2
package text

import collection._
import Seqx._

import scala.collection.{IndexedSeqOptimized, immutable}

/**
 * This trait represents the difference between 2 "contents"
 */
trait ContentDifference {
  type Difference

  /** @return true if there is no difference in 2 contents */
  def isEmpty: Boolean

  /** @return what is missing in content1 from content2 and what is missing in content2 from content1 */
  def show: (Seq[Difference], Seq[Difference])
}

/**
 * This class shows the differences between 2 sequences of lines.
 *
 * - if partial is true we only expect some of the lines of lines2 to be in lines1
 * - if unordered is true we only expect the lines of lines2 to appear in any order in lines1
 */
case class LinesContentDifference(
  lines1: Seq[String], lines2: Seq[String],
  partial: Boolean = false,
  unordered: Boolean = false,
  reportMisplaced: Boolean = false) extends ContentDifference {

  type Difference = DifferentLine
  private type Diffs = (Seq[DifferentLine], Seq[DifferentLine])

  def isEmpty = {
    val (diffs1, diffs2) = show
    diffs1.isEmpty && diffs2.isEmpty
  }

  def all = !partial
  def ordered = !unordered

  lazy val show: Diffs = {
    if      (all     && ordered)   showNotEqual
    else if (all     && unordered) showNotOrdered
    else if (partial && ordered)   showNotIncluded
    else                           showNotContained
  }

  // all && ordered
  private lazy val showNotEqual: Diffs = {
    val lines2Indexed = lines2.to[IndexedSeq]
    val diffs1 = lines1.zipWithIndex.flatMap { case (l1, index1) =>
      lines2Indexed.drop(index1).headOption match {
        case None     => Some(MissingLine(l1, index1+1))
        case Some(l2) =>
          if (l1 == l2)                 None
          else if (lines2.contains(l1)) Some(MisplacedLine(l1, index1+1))
          else                          Some(MissingLine(l1, index1+1))
      }
    }

    (diffs1, missingInOther(lines2, lines1))
  }

  private def missingInOther(ls1: Seq[String], ls2: Seq[String]) =
    (ls1 filterNot ls2.contains).map(l1 => MissingLine(l1, ls1.indexOf(l1)+1))

  // all && unordered
  private lazy val showNotOrdered: Diffs = {
    val compareLine = (p: (String, Int), o: String) => p._1 == o
    (lines1.zipWithIndex.delta(lines2, compareLine).map { case (l, i) => NotFoundLine(l, i+1) },
     lines2.zipWithIndex.delta(lines1, compareLine).map { case (l, i) => NotFoundLine(l, i+1) })
  }

  // partial && ordered
  private lazy val showNotIncluded: Diffs =
    LinesContentDifference(lines1 filter lines2.contains, lines2, partial = false, unordered, reportMisplaced).show

  // partial && unordered
  private lazy val showNotContained: Diffs  =
    LinesContentDifference(lines1 filter lines2.contains, lines2, partial = false, unordered, reportMisplaced).show

}

/**
 * case classes for the representation of lines which are different: not found, missing, misplaced
 */
sealed trait DifferentLine
case class NotFoundLine(line: String, lineNumber: Int) extends DifferentLine {
  override def toString = lineNumber+". "+line
}
case class MissingLine(line: String, lineNumber: Int) extends DifferentLine {
  override def toString = "MISSING:   "+lineNumber+". "+line
}
case class MisplacedLine(line: String, lineNumber: Int) extends DifferentLine {
  override def toString = "MISPLACED: "+lineNumber+". "+line
}

/**
 * A trait to filter results of a difference check
 */
trait DifferenceFilter extends Function1[(Seq[_], Seq[_]), (Seq[_], Seq[_])]

/**
 * This trait provides some syntactic sugar to create a DifferenceFilter to take only the first n differences:
 *
 *  10.differences == FirstNDifferencesFilter(10)
 */
trait DifferenceFilters {
  implicit def toDifferenceFilter(n: Int): FirstNDifferencesFilter = FirstNDifferencesFilter(n)
  case class FirstNDifferencesFilter(n: Int) {
    def difference = FirstDifferences(n: Int)
    def differences = FirstDifferences(n: Int)
  }
}

/**
 * mix-in this trait to remove the implicit provided by the DifferenceFilters trait
 */
trait NoDifferenceFilters extends DifferenceFilters {
  override def toDifferenceFilter(n: Int): FirstNDifferencesFilter = super.toDifferenceFilter(n)
}

/**
 * return all the differences
 */
object AllDifferences extends SomeDifferences((s: Seq[_]) => s)

/**
 * return the first n differences
 */
case class FirstDifferences(n: Int) extends SomeDifferences((s: Seq[_]) => s.take(n))

/**
 * return some of the differences, filtered with a function
 */
class SomeDifferences(f: Seq[_] => Seq[_]) extends DifferenceFilter {
  def apply(diffs: (Seq[_], Seq[_])) = (f(diffs._1), f(diffs._2))
}
