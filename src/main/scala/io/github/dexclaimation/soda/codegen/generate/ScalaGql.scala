//
//  ScalaGql.scala
//  soda
//
//  Created by d-exclaimation on 4:13 PM.
//

package io.github.dexclaimation.soda.codegen.generate

import sangria.ast.{ListType, NamedType, NotNullType, Type}

object ScalaGql {

  private val convert = {
    val types = Vector("String", "Int", "Boolean")
      .map(t => t -> t) ++
      Vector("ID" -> "String", "Float" -> "Double")
    Map(types: _*)
  }

  def gqlToScalaType(t: Type): String = {
    t match {
      case NamedType(name, _) => s"Option[${convert.getOrElse(name, name)}]"
      case NotNullType(ofType, _) => normal(ofType)
      case ListType(ofType, _) => s"Option[${list(ofType)}]"
    }
  }

  private def normal(t: Type): String = {
    t match {
      case NamedType(name, _) => convert.getOrElse(name, name)
      case NotNullType(ofType, _) => gqlToScalaType(ofType)
      case ListType(ofType, _) => list(ofType)
    }
  }

  private def list(t: Type): String = {
    t match {
      case NamedType(name, _) => s"List[Option[${convert.getOrElse(name, name)}]]"
      case NotNullType(ofType, _) => s"List[${normal(ofType)}]"
      case ListType(ofType, _) => s"Option[${list(ofType)}]"
    }
  }

}
