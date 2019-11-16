package com.etandon.jobcoin.infra.datasources

import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}
import scala.io
import scala.io.Codec

object AddressFileReader extends LazyLogging {
  def read(): Option[List[String]] = {
    Try(
      io.Source.fromURL(getClass.getResource("address-datasource.csv"))(
        Codec.UTF8)) match {
      case Success(res) =>
        val parsedLines = res
          .getLines()
          .toList
        if (parsedLines.exists(_.isEmpty)) {
          logger.error("Could not read and parse resource file")
          None
        } else Some(parsedLines)
      case Failure(e) =>
        println(s"Could not read resource file: $e")
        logger.error(s"Could not read resource file: $e")
        None
    }
  }
}
