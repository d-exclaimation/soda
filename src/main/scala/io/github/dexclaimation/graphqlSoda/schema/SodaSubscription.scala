//
//  SodaSubscription.scala
//  graphql-soda
//
//  Created by d-exclaimation on 4:02 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import io.github.dexclaimation.graphqlSoda.utils.SubscriptionField

import scala.reflect.ClassTag

/**
 * Soda Subscription Extension
 *
 * @tparam Ctx Context Type.
 * @tparam Val Subscription Root Value.
 */
abstract class SodaSubscription[Ctx, Val: ClassTag] {
  type Def = SodaDefinitionBlock[Ctx, Val] => Unit

  private val __block = new SodaDefinitionBlock[Ctx, Val]

  def definition: Def

  /**
   * SubscriptionField derivation.
   */
  val t: SubscriptionField[Ctx, Val] = {
    definition(__block)
    val fields = __block.typedefs.toList
    SubscriptionField(fields: _*)
  }
}
