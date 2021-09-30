//
//  Enum.scala
//  soda
//
//  Created by d-exclaimation on 3:04 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import io.github.dexclaimation.soda.codegen.generate.Common.PACKAGE_INIT
import sangria.ast.{EnumTypeDefinition, EnumValueDefinition}

object Enum {
  def apply(e: EnumTypeDefinition): String = {
    val objects = e
      .values
      .map(enumValue)
      .map(name => s"  case object $name extends ${e.name}\n")
      .mkString

    val members = e
      .values
      .map(e => (e.name, enumValue(e)))
      .map { case (name, obj) => s"""enum("$name", value = $obj)""" }
      .map(_ + " :: ")
      .mkString
    s"""
       |${PACKAGE_INIT}sealed trait ${e.name}
       |
       |object ${e.name} extends SodaEnumType[${e.name}]("${e.name}") {
       |$objects
       |  def members: Def =
       |    ${members}Nil
       |}
       |""".stripMargin
  }

  def enumValue(v: EnumValueDefinition): String = {
    val curr = v.name.toLowerCase
    if (curr.isBlank)
      curr
    else
      curr.indices.map(i => if (i == 0) curr(i).toUpper else curr(i)).mkString("")
  }
}

