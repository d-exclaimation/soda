//
//  SodaSubscription.scala
//  graphql-soda
//
//  Created by d-exclaimation on 4:02 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import io.github.dexclaimation.graphqlSoda.utils.SubscriptionField
import sangria.schema.Field

import scala.reflect.ClassTag

/**
 * Soda Subscription Extension
 *
 * @tparam Ctx Context Type.
 * @tparam Val Subscription Root Value.
 */
abstract class SodaSubscription[Ctx, Val: ClassTag] {
  def definition: List[Field[Ctx, Val]]

  /**
   * SubscriptionField derivation.
   */
  val t: SubscriptionField[Ctx, Val] = SubscriptionField(
    definition: _*
  )
}
