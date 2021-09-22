//
//  SodaRootBlock.scala
//  soda
//
//  Created by d-exclaimation on 10:22 PM.
//

package io.github.dexclaimation.soda.schema

import io.github.dexclaimation.soda.schema.traits.SodaBlock
import sangria.execution.FieldTag
import sangria.schema.{Action, Argument, Context, Field, OutputType}
import sangria.streaming.SubscriptionStreamLike

import scala.reflect.ClassTag

class SodaRootBlock[Ctx, Val: ClassTag](useStreaming: Boolean = false) extends SodaBlock[Ctx, Val] {

  /**
   * Added a field either that uses Sangria's streaming API
   *
   * ''Only for Subscriptions, will be ignored for other fields''
   *
   * @param name      Name of the property field.
   * @param fieldType The GraphQL Type of that field.
   * @param desc      Additional descriptions.
   * @param args      Required arguments from the field.
   * @param resolve   Compute or resolve the stream-like field
   */
  def stream[Out, Res, Stream](
    name: String,
    fieldType: OutputType[Out],
    desc: String = "",
    args: List[Argument[_]] = Nil,
    deprecated: Option[String] = None,
    tags: List[FieldTag] = Nil,
  )(resolve: Context[Ctx, Val] => Stream)
    (implicit s: SubscriptionStreamLike[Stream, Action, Ctx, Res, Out]): Unit = {
    if (useStreaming) {
      val streamingField = Field.subs(name, fieldType, if (desc.isEmpty) None else Some(desc), args,
        deprecationReason = deprecated,
        tags = tags,
        resolve = resolve
      )
      typedefs.addOne(streamingField)
    }
  }
}