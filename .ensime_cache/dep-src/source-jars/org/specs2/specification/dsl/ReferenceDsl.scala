package org.specs2
package specification
package dsl

import core._
import create.FragmentsFactory

/**
 * Dsl for creating specification references
 */
trait ReferenceDsl extends ReferenceCreation {

  implicit class linkFragment(alias: String) {
    def ~(s: SpecStructure): Fragment =
      fragmentFactory.link(SpecificationRef(s.header, s.arguments, alias = alias))

    def ~(s: SpecStructure, tooltip: String): Fragment =
      fragmentFactory.link(SpecificationRef(s.header, s.arguments, alias = alias, tooltip = tooltip))

    def ~(s: => SpecificationStructure): Fragment = {
      lazy val spec = s.is
      fragmentFactory.link(SpecificationRef(spec.header, spec.arguments, alias = alias))
    }

    def ~(s: => SpecificationStructure, tooltip: String): Fragment =  {
      lazy val spec = s.is
      fragmentFactory.link(SpecificationRef(spec.header, spec.arguments, alias = alias, tooltip = tooltip))
    }
  }

  implicit class seeFragment(alias: String) {
    def ~/(s: SpecStructure): Fragment =
      fragmentFactory.see(SpecificationRef(s.header, s.arguments, alias = alias))

    def ~/(s: SpecStructure, tooltip: String): Fragment =
      fragmentFactory.see(SpecificationRef(s.header, s.arguments, alias = alias, tooltip = tooltip))

    def ~/(s: => SpecificationStructure): Fragment = {
      lazy val spec = s.is
      fragmentFactory.see(SpecificationRef(spec.header, spec.arguments, alias = alias))
    }

    def ~/(s: => SpecificationStructure, tooltip: String): Fragment =  {
      lazy val spec = s.is
      fragmentFactory.see(SpecificationRef(spec.header, spec.arguments, alias = alias, tooltip = tooltip))
    }
  }
}

/**
 * Create references without any implicits
 */
trait ReferenceCreation extends FragmentsFactory {
  def link(s: SpecStructure): Fragment            = fragmentFactory.link(SpecificationRef.create(s))
  def link(s: =>SpecificationStructure): Fragment = fragmentFactory.link(SpecificationRef.create(s.is))

  def see(s: SpecStructure): Fragment            = fragmentFactory.see(SpecificationRef.create(s))
  def see(s: =>SpecificationStructure): Fragment = fragmentFactory.see(SpecificationRef.create(s))
}

