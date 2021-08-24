package io.github.dexclaimation.graphqlSoda

import sangria.marshalling.{FromInput, ToInput}
import sangria.schema.{Argument, ArgumentType, InputType}

package object schema {
  /** Argument type, following GraphQL Variable convention ($ prefix) */
  def $[T, Default](name: String, argType: InputType[T], desc: String, default: Default)(
    implicit toInput: ToInput[Default, _], fromInput: FromInput[T], res: ArgumentType[T]
  ): Argument[res.Res] =
    Argument(name, argType, Some(desc), Some(default -> toInput), fromInput, Vector.empty, Vector.empty)
}
