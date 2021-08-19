//
//  StrawExtension.scala
//  graphql-straw
//
//  Created by d-exclaimation on 6:54 AM.
//

package com.dexclaimation.sangriaStraw.schema

import sangria.schema.{Field, Schema}
import com.dexclaimation.sangriaStraw.schema.MutationField.makeMutation
import com.dexclaimation.sangriaStraw.schema.QueryField.makeQuery
import com.dexclaimation.sangriaStraw.schema.SubscriptionField.makeSubscription

object StrawExtension {
  def extendQuery[C, T](fields: Field[C, T]*): QueryField[C, T] =
    QueryField(fields: _*)

  def extendMutation[C, T](fields: Field[C, T]*): MutationField[C, T] =
    MutationField(fields: _*)

  def extendSubscription[C, T](fields: Field[C, T]*): SubscriptionField[C, T] =
    SubscriptionField(fields: _*)

  def makeSchema[C, T](fields: RootSchemaField[C, T]*): Schema[C, T] = {
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

    Schema[C, T](QueryType, Some(MutationType), Some(SubscriptionType))
  }
}
