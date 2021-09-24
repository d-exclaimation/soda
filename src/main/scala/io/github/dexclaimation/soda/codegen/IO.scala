//
//  IO.scala
//  soda
//
//  Created by d-exclaimation on 3:49 PM.
//

package io.github.dexclaimation.soda.codegen

import scala.util.{Success, Try}

object IO {
  def read(path: String): String = ""

  def write(path: String, content: String): Try[Unit] = Success(())
}
