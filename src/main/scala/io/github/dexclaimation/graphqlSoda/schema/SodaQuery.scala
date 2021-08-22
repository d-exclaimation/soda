//
//  SodaQuery.scala
//  graphql-soda
//
//  Created by d-exclaimation on 3:52 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import io.github.dexclaimation.graphqlSoda.utils.QueryField
import sangria.schema.Field

import scala.reflect.ClassTag

/**
 * Soda Query Extension
 *
 * @tparam Ctx Context Type.
 * @tparam Val Query Root Value.
 */
abstract class SodaQuery[Ctx, Val: ClassTag] {
  def definition: List[Field[Ctx, Val]]

  /**
   * QueryField derivation.
   */
  val t: QueryField[Ctx, Val] = QueryField(
    definition: _*
  )
}
