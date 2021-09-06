//
//  SodaEnumType.scala
//  soda
//
//  Created by d-exclaimation on 3:38 PM.
//

package io.github.dexclaimation.soda.schema

import sangria.ast
import sangria.schema.{EnumType, EnumValue}

/**
 * Soda Implementable Enum Type definition.
 *
 * Help to avoid nesting, avoid prefixing with `Types`
 *
 * Accessing the EnumType from `t: EnumType` properties
 *
 * @param name Name of the Enum.
 * @tparam T Value paired for this Enum (*best to implement this on a case class's companion object)
 */
abstract class SodaEnumType[T](name: String) {
  /** Definition with list of Enums */
  type Def = List[EnumValue[T]]

  def desc: String = ""

  /** Enum Value wrapper */
  final def enum(
    name: String,
    description: Option[String] = None,
    value: T,
    deprecationReason: Option[String] = None,
    astDirectives: Vector[ast.Directive] = Vector.empty,
    astNodes: Vector[ast.AstNode] = Vector.empty
  ): EnumValue[T] = EnumValue(name, description, value, deprecationReason, astDirectives, astNodes)

  def members: Def

  /**
   * Sangria EnumType derivation.
   */
  lazy final val t: EnumType[T] = EnumType(
    name = name,
    description = if (desc.isEmpty) None else Some(desc),
    values = members
  )
}
