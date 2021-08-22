//
//  SodaMutation.scala
//  graphql-soda
//
//  Created by d-exclaimation on 4:01 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import io.github.dexclaimation.graphqlSoda.utils.MutationField
import sangria.schema.Field

import scala.reflect.ClassTag

/**
 * Soda Mutation Extension
 *
 * @tparam Ctx Context Type.
 * @tparam Val Mutation Root Value.
 */
abstract class SodaMutation[Ctx, Val: ClassTag] {
  def definition: List[Field[Ctx, Val]]

  /**
   * MutationField derivation.
   */
  val t: MutationField[Ctx, Val] = MutationField(
    definition: _*
  )
}
