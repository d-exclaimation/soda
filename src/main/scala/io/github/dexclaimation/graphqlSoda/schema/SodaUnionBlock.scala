//
//  SodaUnionBlock.scala
//  graphql-soda
//
//  Created by d-exclaimation on 3:33 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import sangria.schema.ObjectType

import scala.collection.mutable

class SodaUnionBlock[Ctx] {
  private[schema] val typedefs: mutable.ArrayBuffer[ObjectType[Ctx, _]] = mutable.ArrayBuffer.empty

  /**
   * Added possible members of the union.
   *
   * @param objectType All Union possibilities.
   */
  def members(objectType: ObjectType[Ctx, _]*): Unit =
    typedefs.appendAll(objectType)
}
