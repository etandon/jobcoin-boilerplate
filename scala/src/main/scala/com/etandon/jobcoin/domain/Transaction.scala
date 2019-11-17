package com.etandon.jobcoin.domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.deriveDecoder
import io.circe.generic.semiauto._
import scala.util.control.NonFatal


case class Transaction(timestamp: LocalDateTime, toAddress: String, amount: String)

object Transaction {
  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  implicit val dateEncoder = Encoder.encodeString.contramap[LocalDateTime](_.format(formatter))
  implicit val dateDecoder = Decoder.decodeString.emap[LocalDateTime]{ s =>
    try {
      Right(LocalDateTime.parse(s, formatter))
    } catch {
      case NonFatal(e) => Left(e.getMessage)
    }
  }
  implicit val transactionDecoder: Decoder[Transaction] = deriveDecoder[Transaction]
  implicit val transactionEncoder: Encoder[Transaction] = deriveEncoder[Transaction]
}
