//
//  SubscriptionField.scala
//  hello-world
//
//  Created by d-exclaimation on 5:57 PM.
//


package com.dexclaimation.sangriaStraw.schema

import sangria.schema.{Field, ObjectType}

import scala.reflect.ClassTag

case class SubscriptionField[C, T: ClassTag](fields: Field[C, T]*) extends RootSchemaField[C, T] {
  def fieldList: List[Field[C, T]] = fields.toList

  override def extendTo: String = "Subscription"
}

object SubscriptionField {
  def makeSubscription[C, T: ClassTag](queryField: SubscriptionField[C, T]*): ObjectType[C, T] =
    ObjectType(
      name = "Subscription",
      fields =
        queryField.flatMap(_.fieldList).toList
    )
}
