//
//  Input.scala
//  soda
//
//  Created by d-exclaimation on 8:06 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import sangria.ast.{InputObjectTypeDefinition, InputValueDefinition}

object Input {
  def apply(obj: InputObjectTypeDefinition): String = {
    val objName = if (obj.name.contains("Input")) obj.name else s"${obj.name}Input"

    val inputFields = obj
      .fields
      .map(inputField)
      .map(s => s"  $s")
      .mkString("\n")

    val inputDef =
      s"""case class $objName (
         |$inputFields
         |)
         |""".stripMargin

    val sodaFields = obj
      .fields
      .map(inputProp)
      .mkString("\n")

    val sodaDef =
      s"""object $objName extends SodaInputType[$objName](\"$objName\") {
         |  def definition: Def = { t =>
         |$sodaFields
         |  }
         |}
         |""".stripMargin
    s"package schema\n\nimport io.github.dexclaimation.soda.schema._\nimport sangria.schema._\n\n$inputDef\n\n$sodaDef"
  }

  private def inputField(f: InputValueDefinition): String = {
    val typeDef = ScalaGql.gqlToScalaType(f.valueType)
    s"  ${f.name}: $typeDef"
  }

  private def inputProp(f: InputValueDefinition): String = {
    val typeDef = SodaGql.gqlToSangriaType(f.valueType)
    s"""    t.prop("${f.name}", $typeDef)"""
  }
}
