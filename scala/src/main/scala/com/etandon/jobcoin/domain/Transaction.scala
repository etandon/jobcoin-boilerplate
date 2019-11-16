package com.etandon.jobcoin.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._


case class Transaction()

object Transaction extends App{
  import io.circe.Decoder
  import io.circe.generic.semiauto.deriveDecoder
  implicit val transactionDecoder: Decoder[Transaction] = deriveDecoder[Transaction]
  implicit val transactionEncoder: Encoder[Transaction] = deriveEncoder[Transaction]
}
