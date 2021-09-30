//
//  GraphQLObject.scala
//  soda
//
//  Created by d-exclaimation on 3:58 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import io.github.dexclaimation.soda.codegen.generate.Common.PACKAGE_INIT
import sangria.ast.{FieldDefinition, ObjectTypeDefinition}

import java.util.concurrent.atomic.AtomicInteger

object Object {
  def apply(obj: ObjectTypeDefinition): String = {
    val caseFields = obj
      .fields
      .map(field)
      .mkString(",\n")
    val caseClass = s"case class ${obj.name}(\n$caseFields\n) ${
      if (obj.interfaces.nonEmpty) s"extends ${obj.interfaces.map(_.name).mkString(" with ")}" else ""
    }"

    val atomic = new AtomicInteger()

    val interfaces = if (obj.interfaces.nonEmpty)
      s"    t.implements(${obj.interfaces.map(_.name + ".t").mkString(", ")})\n\n"
    else ""

    val sodaFields = obj
      .fields
      .map(sodaProp(atomic))
      .mkString("\n")

    s"""
       |$PACKAGE_INIT$caseClass
       |
       |
       |object ${obj.name} extends SodaObjectType[Unit, ${obj.name}](\"${obj.name}\") {
       |  def definition: Def = { t =>
       |$interfaces$sodaFields
       |  }
       |}
       |""".stripMargin
  }

  private def field(f: FieldDefinition): String = {
    val typeName = ScalaGql.fromGql(f.fieldType)
    s"  ${f.name}: $typeName"
  }


  def sodaProp(atomic: AtomicInteger): FieldDefinition => String = f => {
    val typeDef = SodaGql.fromGql(f.fieldType)
    if (f.arguments.isEmpty) {
      s"""    t.prop("${f.name}", $typeDef, of = _.${f.name})"""
    } else {
      val args = f.arguments
        .map(a => (a.name + s"${if (atomic.get() == 0) "" else atomic.get().toString}Arg", a))
      atomic.getAndIncrement()
      val argVars = args
        .map { case (name, a) => s"""val $name = ${"$"}("${a.name}", ${
          SodaGql.fromGql(a.valueType, isInput = true
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
