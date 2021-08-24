//
//  SodaQuery.scala
//  graphql-soda
//
//  Created by d-exclaimation on 3:52 PM.
//

package io.github.dexclaimation.graphqlSoda.schema

import io.github.dexclaimation.graphqlSoda.utils.QueryField

import scala.reflect.ClassTag

/**
 * Soda Query Extension
 *
 * @tparam Ctx Context Type.
 * @tparam Val Query Root Value.
 */
abstract class SodaQuery[Ctx, Val: ClassTag] {
  private val __block = new SodaRootBlock[Ctx, Val]
  type Def = __block.type => Unit

  def definition: Def

  /**
   * QueryField derivation.
   */
  lazy val t: QueryField[Ctx, Val] = {
    definition(__block)
    val fields = __block.fields.toList
    QueryField(fields: _*)
  }
}
