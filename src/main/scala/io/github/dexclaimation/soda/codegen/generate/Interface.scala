//
//  GraphQLInterface.scala
//  soda
//
//  Created by d-exclaimation on 5:30 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import io.github.dexclaimation.soda.codegen.generate.Object.sodaProp
import sangria.ast.{FieldDefinition, InterfaceTypeDefinition}

import java.util.concurrent.atomic.AtomicInteger

object Interface {
  def apply(obj: InterfaceTypeDefinition): String = {
    val traitFields = obj
      .fields
      .map(field)
      .map("  " + _)
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

    val sodaDef =
      s"""object ${obj.name} extends SodaInterfaceType[Unit, ${obj.name}](\"${obj.name}\") {
         |  def definition: Def = { t =>
         |$sodaFields
         |  }
         |}
         |""".stripMargin
    s"package schema\n\nimport io.github.dexclaimation.soda.schema._\nimport sangria.schema._\n\n$traitDef\n\n$sodaDef"
  }

  private def field(f: FieldDefinition): String = {
    val typeDef = ScalaGql.gqlToScalaType(f.fieldType)
    s"def ${f.name}: $typeDef"
  }
}
