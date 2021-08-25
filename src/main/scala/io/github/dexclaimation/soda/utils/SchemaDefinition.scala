//
//  SchemaDefinition.scala
//  soda
//
//  Created by d-exclaimation on 3:35 PM.
//

package io.github.dexclaimation.soda.utils

import io.github.dexclaimation.soda.utils.MutationField.makeMutation
import io.github.dexclaimation.soda.utils.QueryField.makeQuery
import io.github.dexclaimation.soda.utils.SubscriptionField.makeSubscription
import sangria.schema.Schema

import scala.reflect.ClassTag

object SchemaDefinition {
  /**
   * Compose all Root schema extensions to create a Sangria Schema.
   *
   * @param fields Any Schema extensions (Query, Mutations, Subscriptions)
   * @tparam C Context type.
   * @tparam T Value type.
   * @return A full Sangria Schema with the right values.
   */
  def makeSchema[C, T: ClassTag](fields: SodaSchemaField[C, T]*): Schema[C, T] = {
    val QueryType = makeQuery(
      fields.flatMap {
        case QueryField(fields@_*) => Some(QueryField(fields: _*))
        case _ => None
      }: _*
    )

    val MutationType = makeMutation(
      fields.flatMap {
        case MutationField(fields@_*) => Some(MutationField(fields: _*))
        case _ => None
      }: _*
    )

    val SubscriptionType = makeSubscription(
      fields.flatMap {
        case SubscriptionField(fields@_*) => Some(SubscriptionField(fields: _*))
        case _ => None
      }: _*
    )

    Schema[C, T](
      query = QueryType,
      mutation = if (MutationType.fields.isEmpty) None else Some(MutationType),
      subscription = if (SubscriptionType.fields.isEmpty) None else Some(SubscriptionType)
    )
  }
}
