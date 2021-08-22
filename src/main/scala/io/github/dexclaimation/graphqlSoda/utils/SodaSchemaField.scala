//
//  RootSchemaField.scala
//  graphql-soda
//
//  Created by d-exclaimation on 3:35 PM.
//


package io.github.dexclaimation.graphqlSoda.utils

/**
 * Soda Root Schema Field
 *
 * @tparam C Context type.
 * @tparam T Value type.
 */
trait SodaSchemaField[C, T] {
  /** Name of the field to extend to */
  def extendTo: String
}
