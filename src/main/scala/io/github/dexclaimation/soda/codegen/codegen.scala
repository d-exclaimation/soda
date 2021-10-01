//
//  SodaGen.scala
//  soda
//
//  Created by d-exclaimation on 3:09 PM.
//

package io.github.dexclaimation.soda

import io.github.dexclaimation.soda.common.FS
import sangria.ast
import sangria.ast._
import sangria.parser.QueryParser

import java.nio.file.Paths
import scala.util.{Failure, Success}

package object codegen {

  /**
   * Generate from existing graphql dsl file
   *
   * @param from Path to schema.
   * @param pkg  Package name of generated file.
   * @param path Path for generation (excluding the package name).
   */
  def generateSchema(
    from: String,
    pkg: String = "schema",
    path: String = "./src/main/scala/"
  ): Unit = {
    val filepath = Paths.get(path, pkg).toAbsolutePath.toString
    val schema = FS.read(from)
    generateFrom(schema, filepath, pkg)
  }

  /**
   * Generate code from graphql sdl (in string) to a file path
   *
   * @param doc      The graphql sdl as string.
   * @param filepath Filepath including the pkg.
   * @param pkg      Package name.
   */
  def generateFrom(doc: String, filepath: String, pkg: String): Unit = QueryParser
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
      case obj: ObjectTypeDefinition if !isRoot(obj) =>
        Some((obj.name, generate.Object(obj, pkg)))
      case itf: InterfaceTypeDefinition =>
        Some((itf.name, generate.Interface(itf, pkg)))
      case ipt: InputObjectTypeDefinition =>
        Some((generate.Input.inputName(ipt), generate.Input(ipt, pkg)))
      case uni: UnionTypeDefinition =>
        Some((uni.name, generate.Union(uni, pkg)))
      case enum: EnumTypeDefinition =>
        Some((enum.name, generate.Enum(enum, pkg)))
      case s: ScalarTypeDefinition =>
        Some((s.name, generate.Scalar(s, pkg)))
      case _ => None
    }
    .map { case (filename, content) => (s"$filepath/$filename.scala", content) }
    .map { case (path, content) => FS.write(path, content) }
    .foreach {
      case Failure(e) => println(s"(x) $e")
      case Success(path) => println(s"(+) $path")
    }


  private def isRoot(obj: ObjectTypeDefinition): Boolean =
    Set("Query", "Mutation", "Subscription").contains(obj.name)
}
