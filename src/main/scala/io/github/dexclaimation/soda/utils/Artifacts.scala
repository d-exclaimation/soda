//
//  Artifacts.scala
//  soda
//
//  Created by d-exclaimation on 12:36 AM.
//

package io.github.dexclaimation.soda.utils

import sangria.renderer.SchemaRenderer
import sangria.schema.Schema

import java.io.{File, PrintWriter}
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
    makeIfNotExist(target)
    compileFile(target, compiled)
  }

  /**
   * Make the file if it doesn't exist yet
   *
   * @param file The file to be written.
   */
  def makeIfNotExist(file: File): Unit = {
    if (!file.exists()) {
      file.getParentFile.mkdirs()
      file.createNewFile()
    }
  }

  /**
   * Compile the file or overwrite the new file
   *
   * @param file        the target File.
   * @param compilation The compilation result.
   */
  def compileFile(file: File, compilation: String): Unit = {
    val writer = new PrintWriter(file)
    writer.write(compilation)
    writer.close()
  }
}
