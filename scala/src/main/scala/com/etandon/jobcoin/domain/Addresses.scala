package com.etandon.jobcoin.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Addresses(balance: String, transactions: List[Transaction])

object Addresses{
  implicit val addressesDecoder: Decoder[Addresses] = deriveDecoder[Addresses]
  implicit val addressesEncoder: Encoder[Addresses] = deriveEncoder[Addresses]
}
