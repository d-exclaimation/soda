//
//  SodaEnumType.scala
//  graphql-soda
//
//  Created by d-exclaimation on 3:38 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

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
  def description: String = ""

  def members: List[EnumValue[T]]

  /**
   * Sangria EnumType derivation.
   */
  lazy val t: EnumType[T] = EnumType(
    name = name,
    description = if (description == "") None else Some(description),
    values = members
  )
}
