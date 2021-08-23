//
//  SodaMutation.scala
//  graphql-soda
//
//  Created by d-exclaimation on 4:01 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import io.github.dexclaimation.graphqlSoda.utils.MutationField

import scala.reflect.ClassTag

/**
 * Soda Mutation Extension
 *
 * @tparam Ctx Context Type.
 * @tparam Val Mutation Root Value.
 */
abstract class SodaMutation[Ctx, Val: ClassTag] {
  type Def = SodaDefinitionBlock[Ctx, Val] => Unit

  private val __block = new SodaDefinitionBlock[Ctx, Val]

  def definition: Def

  /**
   * MutationField derivation.
   */
  val t: MutationField[Ctx, Val] = {
    definition(__block)
    val fields = __block.typedefs.toList
    MutationField(fields: _*)
  }
}
