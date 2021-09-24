//
//  IO.scala
//  soda
//
//  Created by d-exclaimation on 3:49 PM.
//

package io.github.dexclaimation.soda.codegen

import io.github.dexclaimation.soda.utils.Artifacts

import java.io.File
import java.nio.file.{Files, Paths}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object IO {

  def read(path: String): String = {
    try {
      val schemaPath = Paths.get(path)
      Files.readString(schemaPath)
    } catch {
      case NonFatal(_) => ""
    }
  }

  def write(path: String, content: String): Try[String] = {
    try {
      val target = new File(path)
      Artifacts.makeIfNotExist(target)
      Artifacts.compileFile(target, content)
      Success(path)
    } catch {
      case NonFatal(e) => Failure(e)
    }
  }

}
