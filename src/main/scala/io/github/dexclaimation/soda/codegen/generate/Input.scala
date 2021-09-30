//
//  Input.scala
//  soda
//
//  Created by d-exclaimation on 8:06 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import io.github.dexclaimation.soda.codegen.generate.Common.PACKAGE_INIT
import sangria.ast.{InputObjectTypeDefinition, InputValueDefinition}

object Input {
  def apply(obj: InputObjectTypeDefinition): String = {
    val objName = inputName(obj)

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

    s"""
       |$PACKAGE_INIT$inputDef
       |
       |
       |object $objName extends SodaInputType[$objName](\"$objName\") {
       |  def definition: Def = { t =>
       |$sodaFields
       |  }
       |}
       |""".stripMargin
  }

  private def inputField(f: InputValueDefinition): String = {
    val typeDef = ScalaGql.fromGql(f.valueType)
    s"  ${f.name}: $typeDef"
  }

  private def inputProp(f: InputValueDefinition): String = {
    val typeDef = SodaGql.fromGql(f.valueType)
    s"""    t.prop("${f.name}", $typeDef)"""
  }

  def inputName(obj: InputObjectTypeDefinition): String =
    if (obj.name.contains("Input")) obj.name else s"${obj.name}Input"
}
