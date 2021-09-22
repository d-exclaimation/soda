package io.github.dexclaimation.soda

import sangria.macros.derive.{DeriveObjectSetting, DeriveObjectTypeMacro}
import sangria.schema.ObjectType

import scala.language.experimental.macros

package object derive {
  def obj[Ctx, Val](
    config: DeriveObjectSetting[Ctx, Val]*
  ): ObjectType[Ctx, Val] = macro DeriveObjectTypeMacro.deriveNormalObjectType[Ctx, Val]
}
