//
//  GraphQLObject.scala
//  soda
//
//  Created by d-exclaimation on 3:58 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import sangria.ast.{FieldDefinition, ObjectTypeDefinition}

import java.util.concurrent.atomic.AtomicInteger

object Object {
  def apply(obj: ObjectTypeDefinition): String = {
    val caseFields = obj
      .fields
      .map(field)
      .mkString(",\n")
    val caseClass = s"case class ${obj.name}(\n$caseFields\n)"

    val atomic = new AtomicInteger()

    val interfaces = if (obj.interfaces.nonEmpty)
      s"    t.implements(${obj.interfaces.map(_.name + ".t").mkString(", ")})\n\n"
    else ""

    val sodaFields = obj
      .fields
      .map(sodaProp(atomic))
      .mkString("\n")

    val sodaDef =
      s"""object ${obj.name} extends SodaObjectType[Unit, ${obj.name}](\"${obj.name}\") {
         |  def definition: Def = { t =>
         |$interfaces$sodaFields
         |  }
         |}
         |""".stripMargin
    s"package schema\n\nimport io.github.dexclaimation.soda.schema._\nimport sangria.schema._\n\n$caseClass\n\n$sodaDef"
  }

  private def field(f: FieldDefinition): String = {
    val typeName = ScalaGql.gqlToScalaType(f.fieldType)
    s"  ${f.name}: $typeName"
  }


  def sodaProp(atomic: AtomicInteger): FieldDefinition => String = f => {
    val typeDef = SodaGql.gqlToSangriaType(f.fieldType)
    if (f.arguments.isEmpty) {
      s"""    t.prop("${f.name}", $typeDef, of = _.${f.name})"""
    } else {
      val args = f.arguments
        .map(a => (a.name + s"${if (atomic.get() == 0) "" else atomic.get().toString}Arg", a))
      atomic.getAndIncrement()
      val argVars = args
        .map { case (name, a) => s"""val $name = ${"$"}("${a.name}", ${
          SodaGql.gqlToSangriaType(a.valueType, isInput = true
          )
        })"""
        }
        .map("    " + _)
        .mkString("\n")
      val argsDeclare = s", args = ${args.map(_._1).mkString(" :: ")} :: Nil"
      s"""\n$argVars\n    t.field("${f.name}", $typeDef$argsDeclare) { c => c.value.${f.name} }"""
    }
  }
}
