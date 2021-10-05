//
//  Artifacts.scala
//  soda
//
//  Created by d-exclaimation on 11:26 PM.
//

package io.github.dexclaimation.soda.common

import sangria.renderer.SchemaRenderer
import sangria.schema.Schema

import java.io.File
import scala.reflect.ClassTag

object Artifacts {
  /**
   * Artifact compiler
   *
   * @param schema The Sangria schema to be compiled into.
   * @param path   The resulting path.
   */
  def compile[Ctx, Val: ClassTag](
    schema: Schema[Ctx, Val],
    path: String = "./src/main/resources/artifacts/schema.graphql"
  ): Unit = {
    val compiled = SchemaRenderer.renderSchema(schema)
    val target = new File(path)
    FS.makeIfNotExist(target)
    FS.compileFile(target, compiled)
  }

}
