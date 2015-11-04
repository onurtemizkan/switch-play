package org.specs2
package matcher

import text.Sentences._
import execute.{ResultExecution, AsResult}

trait ExpectationsDescription extends ExpectationsCreation {

  implicit def describeExpectation(description: String): ExpectationDescription = new ExpectationDescription(description)

  class ExpectationDescription(description: String) {
    def ==>[T : AsResult](result: =>T) = <==>(result)
    def <==>[T : AsResult](result: =>T) = checkResultFailure {
      val r = ResultExecution.execute(AsResult(result))
      r match {
        case i if i.isError || i.isFailure => i.mapMessage(m => negateSentence(description)+" because "+m)
        case other                         => other.mapMessage(m => description+" <=> "+m)
      }
    }
  }

  /** describe a value with the aka method */
  implicit def describe[T](t: => T): Descriptible[T] = new Descriptible(t)

  class Descriptible[T](value: => T) {
    /**
     * @return an expectable with its toString method as an alias description
     *         this is useful to preserve the original value when the matcher using
     *         it is adapting the value
     */
    def aka: Expectable[T] = aka(value.toString)

    /** @return an expectable with an alias description */
    def aka(alias: => String): Expectable[T] = createExpectable(value, alias)

    /** @return an expectable with an alias description, after the value string */
    def post(alias: => String): Expectable[T] = as((_: String) + " " + alias)

    /** @return an expectable with an alias description, after the value string */
    def as(alias: String => String): Expectable[T] = createExpectable(value, alias)

    /** @return an expectable with a function to show the element T */
    def showAs(implicit show: T => String): Expectable[T] = {
      lazy val v = value
      createExpectableWithShowAs(v, show(v))
    }
  }

}

object ExpectationsDescription extends ExpectationsDescription

trait NoExpectationsDescription extends ExpectationsDescription {
  override def describeExpectation(description: String): ExpectationDescription = super.describeExpectation(description)
}

