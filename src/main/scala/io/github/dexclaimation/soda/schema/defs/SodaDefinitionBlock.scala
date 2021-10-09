//
//  SodaDefinitionBlock.scala
//  soda
//
//  Created by d-exclaimation on 1:27 PM.
//


package io.github.dexclaimation.soda.schema.defs

import io.github.dexclaimation.soda.schema.defs.traits.SodaBlock
import sangria.schema.PossibleInterface

import scala.collection.mutable
import scala.reflect.ClassTag

class SodaDefinitionBlock[Ctx, Val: ClassTag] extends SodaBlock[Ctx, Val] {
  private[schema] val interfaces: mutable.ArrayBuffer[PossibleInterface[Ctx, Val]] = mutable.ArrayBuffer.empty

  /**
   * Implement an interface
   *
   * @param i Possible Interfaces for this Object.
   */
  def implements(i: PossibleInterface[Ctx, Val]*): Unit = interfaces.addAll(i)
}