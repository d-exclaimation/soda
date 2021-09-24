//
//  SodaGen.scala
//  soda
//
//  Created by d-exclaimation on 3:09 PM.
//

package io.github.dexclaimation.soda

import sangria.ast
import sangria.ast._
import sangria.parser.QueryParser

import java.nio.file.Paths
import scala.util.{Failure, Success}

package object codegen {
  def generateSchema(
    from: String,
    pkg: String = "schema",
    path: String = "./src/main/scala/"
  ): Unit = {
    val filepath = Paths.get(path, pkg).toAbsolutePath.toString
    val schema = IO.read(from)
    generateFrom(schema, filepath)
  }

  def generateFrom(
    doc: String,
    filepath: String
  ): Unit =
    QueryParser
      .parse(doc)
      .getOrElse(ast.Document.emptyStub)
      .definitions
      .flatMap {
        case definition: TypeSystemDefinition => Some(definition)
        case _ => None
      }
      .flatMap {
        case definition: TypeDefinition => Some(definition)
        case _ => None
      }
      .flatMap {
        case obj: ObjectTypeDefinition if (!Set("Query", "Mutation", "Subscription").contains(obj.name)) =>
          Some((obj.name, generate.Object(obj)))
        case interface: InterfaceTypeDefinition => Some((interface.name, generate.Interface(interface)))
        case input: InputObjectTypeDefinition => Some(
          (if (input.name.contains("Input")) input.name else s"${input.name}Input", generate.Input(input))
        )
        case _: ScalarTypeDefinition => None
        case _: UnionTypeDefinition => None
        case _: EnumTypeDefinition => None
        case _ => None
      }
      .map { case (filename, content) => (s"$filepath/$filename.scala", content) }
      .map { case (path, content) => IO.write(path, content) }
      .foreach {
        case Failure(e) => println(s"(x) $e")
        case Success(path) => println(s"(+) $path")
      }
}
