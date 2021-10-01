//
//  GraphQLInterface.scala
//  soda
//
//  Created by d-exclaimation on 5:30 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import io.github.dexclaimation.soda.codegen.generate.Common.{indent, pkgInit}
import io.github.dexclaimation.soda.codegen.generate.Object.sodaProp
import sangria.ast.{FieldDefinition, InterfaceTypeDefinition}

import java.util.concurrent.atomic.AtomicInteger

object Interface {
  /** Make the compilation for the InterfaceType */
  def apply(obj: InterfaceTypeDefinition, pkg: String): String = {
    val traitFields = obj
      .fields
      .map(field)
      .map(s => indent()(s))
      .mkString("\n")

    val traitDef =
      s"""trait ${obj.name} {
         |$traitFields
         |}
         |""".stripMargin

    val atomic = new AtomicInteger()

    val sodaFields = obj
      .fields
      .map(sodaProp(atomic))
      .mkString("\n")

    s"""
       |${pkgInit(pkg)}$traitDef
       |
       |object ${obj.name} extends SodaInterfaceType[Unit, ${obj.name}](\"${obj.name}\") {
       |  def definition: Def = { t =>
       |$sodaFields
       |  }
       |}
       |""".stripMargin
  }

  private def field(f: FieldDefinition): String = {
    val typeDef = ScalaGql.fromGql(f.fieldType)
    s"def ${f.name}: $typeDef"
  }
}
