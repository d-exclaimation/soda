//
//  SodaUnionType.scala
//  graphql-soda
//
//  Created by d-exclaimation on 6:36 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import sangria.schema.{ObjectType, UnionType}

/**
 * Soda Implementable Union Type definition.
 *
 * Help to avoid nesting, avoid prefixing with `Types`
 *
 * Accessing the UnionType from `t: UnionType` properties
 *
 * @param name Name of the Union.
 * @tparam Ctx Context value for all of the members.
 */
abstract class SodaUnionType[Ctx](name: String) {
  def description: String = ""

  def members: List[ObjectType[Ctx, _]]

  /**
   * Sangria UnionType derivation.
   */
  val t: UnionType[Ctx] = UnionType(
    name = name,
    description = if (description.isEmpty) None else Some(description),
    typesFn = () => members,
    astDirectives = Vector.empty,
    astNodes = Vector.empty
  )
}