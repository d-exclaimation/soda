//
//  SodaSubscription.scala
//  soda
//
//  Created by d-exclaimation on 4:02 PM.
//

package io.github.dexclaimation.soda.schema

import io.github.dexclaimation.soda.schema.defs.SodaRootBlock
import io.github.dexclaimation.soda.core.SubscriptionField

import scala.reflect.ClassTag

/**
 * Soda Subscription Extension
 *
 * @tparam Ctx Context Type.
 * @tparam Val Subscription Root Value.
 */
abstract class SodaSubscription[Ctx, Val: ClassTag] {
  private val __block = new SodaRootBlock[Ctx, Val](useStreaming = true)

  /** Definition Block */
  type Def = SodaRootBlock[Ctx, Val] => Unit

  def definition: Def

  /**
   * SubscriptionField derivation.
   */
  lazy final val t: SubscriptionField[Ctx, Val] = {
    definition(__block)
    val fields = __block.typedefs.map(_.apply()).toList
    SubscriptionField(fields: _*)
  }
}
