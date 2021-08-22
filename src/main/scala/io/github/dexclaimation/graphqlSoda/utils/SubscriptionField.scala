//
//  SubscriptionField.scala
//  graphql-soda
//
//  Created by d-exclaimation on 5:57 PM.
//


package io.github.dexclaimation.graphqlSoda.utils

import sangria.schema.{Field, ObjectType}

import scala.reflect.ClassTag

/**
 * Extension of Subscriptions Field
 *
 * @param fields GraphQL Field into the query.
 * @tparam C Context type.
 * @tparam T Type value.
 */
case class SubscriptionField[C, T: ClassTag](fields: Field[C, T]*) extends SodaSchemaField[C, T] {
  /** Fields conversion */
  def fieldList: List[Field[C, T]] = fields.toList

  override def extendTo: String = "Subscription"
}

object SubscriptionField {
  /**
   * Compose a Subscription Object Type.
   *
   * @param subscriptionField Extension of Query Fields.
   * @tparam C Context type.
   * @tparam T Value type.
   * @return An ObjectType representing the Root Subscription
   */
  def makeSubscription[C, T: ClassTag](subscriptionField: SubscriptionField[C, T]*): ObjectType[C, T] =
    ObjectType(
      name = "Subscription",
      fields =
        subscriptionField.flatMap(_.fieldList).toList
    )
}
