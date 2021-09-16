//
//  SodaQuery.scala
//  soda
//
//  Created by d-exclaimation on 3:52 PM.
//

package io.github.dexclaimation.soda.schema

import io.github.dexclaimation.soda.utils.QueryField

import scala.reflect.ClassTag

/**
 * Soda Query Extension
 *
 * @tparam Ctx Context Type.
 * @tparam Val Query Root Value.
 */
abstract class SodaQuery[Ctx, Val: ClassTag] {
  private val __block = new SodaRootBlock[Ctx, Val]()

  /** Definition Block */
  type Def = SodaRootBlock[Ctx, Val] => Unit

  def definition: Def

  /**
   * QueryField derivation.
   */
  lazy final val t: QueryField[Ctx, Val] = {
    definition(__block)
    val fields = __block.fields.toList
    QueryField(fields: _*)
  }
}
