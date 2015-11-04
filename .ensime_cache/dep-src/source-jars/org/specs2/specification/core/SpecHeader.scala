package org.specs2
package specification
package core

import text.CamelCase._
import reflect.ClassName

/**
 * Header of a Specification
 *
 * It can hold a user-defined title for better presentation
 */
case class SpecHeader(specClass: Class[_], title: Option[String] = None) {
  def show = title.getOrElse(simpleName)
  def showWords = title.getOrElse(wordsTitle)
  def className = ClassName.className(specClass)
  def simpleName = ClassName.simpleName(specClass)
  def wordsTitle = simpleName.camelCaseToWordsCapitalized
}

object SpecHeader {
  def create(specClass: Class[_]): SpecHeader =
    SpecHeader(specClass, title = None)
}