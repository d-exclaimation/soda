//
//  Dfe.scala
//  soda
//
//  Created by d-exclaimation on 4:11 PM.
//

package io.github.dexclaimation.soda.schema

import sangria.schema.{Args, Context}

import scala.reflect.ClassTag

/**
 * Data fetching environment (sangria.schema.Context)
 */
object Dfe {
  /** Data fetching environment (sangria.schema.Context) */
  def unapply[Ctx, Val: ClassTag](c: Context[Ctx, Val]): Option[(Val, Args, Ctx)] =
    Some((c.value, c.args, c.ctx))
}
