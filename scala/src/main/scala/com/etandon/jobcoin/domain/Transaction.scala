package com.etandon.jobcoin.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import io.circe._
import io.circe.parser
import io.circe.generic.semiauto.deriveDecoder
import io.circe.syntax._
import io.circe.parser.decode
import io.circe._, io.circe.generic.semiauto._


case class Transaction(timestamp: String, toAddress: String, amount: String)



object Transaction {
  implicit val transactionDecoder: Decoder[Transaction] = deriveDecoder[Transaction]
  implicit val transactionEncoder: Encoder[Transaction] = deriveEncoder[Transaction]
}
