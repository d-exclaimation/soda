//
//  FS.scala
//  soda
//
//  Created by d-exclaimation on 11:26 PM.
//

package io.github.dexclaimation.soda.common

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object FS {

  /**
   * Read file and return it's content.
   *
   * @param path String path for the file.
   */
  def read(path: String): String = try {
    val schemaPath = Paths.get(path)
    Files.readString(schemaPath)
  } catch {
    case NonFatal(_) => ""
  }

  /**
   * Write to file with given content, will make the file if any.
   *
   * @param path    String path for the file.
   * @param content String representation for the content.
   */
  def write(path: String, content: String): Try[String] = try {
    val target = new File(path)
    makeIfNotExist(target)
    compileFile(target, content)
    Success(path)
  } catch {
    case NonFatal(e) => Failure(e)
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
