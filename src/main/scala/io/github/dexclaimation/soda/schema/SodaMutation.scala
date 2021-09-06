//
//  SodaMutation.scala
//  soda
//
//  Created by d-exclaimation on 4:01 PM.
//

package io.github.dexclaimation.soda.schema

import io.github.dexclaimation.soda.utils.MutationField

import scala.reflect.ClassTag

/**
 * Soda Mutation Extension
 *
 * @tparam Ctx Context Type.
 * @tparam Val Mutation Root Value.
 */
abstract class SodaMutation[Ctx, Val: ClassTag] {
  private val __block = new SodaRootBlock[Ctx, Val]

  /** Definition Block */
  type Def = SodaRootBlock[Ctx, Val] => Unit

  def definition: Def

  /**
   * MutationField derivation.
   */
  lazy final val t: MutationField[Ctx, Val] = {
    definition(__block)
    val fields = __block.fields.toList
    MutationField(fields: _*)
  }
}
