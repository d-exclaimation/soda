//
//  SodaGql.scala
//  soda
//
//  Created by d-exclaimation on 4:48 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import sangria.ast.{ListType, NamedType, NotNullType, Type}

object SodaGql {

  private val convert = {
    val types = Vector("String", "Int", "Boolean", "Float", "ID")
      .map(t => t -> s"${t}Type")
    Map(types: _*)
  }

  def certainType(t: NamedType): String = nameConvention(t.name)

  def fromGql(t: Type, isInput: Boolean = false): String = {
    val postFix = if (isInput) "Input" else ""
    t match {
      case NamedType(name, _) => s"Option${postFix}Type(${nameConvention(name, isInput)})"
      case NotNullType(ofType, _) => normal(ofType, isInput)
      case ListType(ofType, _) => s"Option${postFix}Type(${list(ofType, isInput)})"
    }
  }

  private def normal(t: Type, isInput: Boolean = false): String = {
    t match {
      case NamedType(name, _) => nameConvention(name, isInput)
      case NotNullType(ofType, _) => fromGql(ofType, isInput)
      case ListType(ofType, _) => list(ofType, isInput)
    }
  }

  private def list(t: Type, isInput: Boolean = false): String = {
    val postFix = if (isInput) "Input" else ""
    t match {
      case NamedType(name, _) => s"List${postFix}Type(Option${postFix}Type(${nameConvention(name, isInput)}))"
      case NotNullType(ofType, _) => s"List${postFix}Type(${normal(ofType, isInput)})"
      case ListType(ofType, _) => s"Option${postFix}Type(${list(ofType, isInput)})"
    }
  }

  private def nameConvention(name: String, isInput: Boolean = false): String = {
    val objName = if (isInput && !name.contains("Input")) s"${name}Input.t" else s"$name.t"
    convert.getOrElse(name, objName)
  }
}
