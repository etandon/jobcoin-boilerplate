package com.etandon.jobcoin.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Transfer(fromAddress: String, toAddress: String, amount: String)

object Transfer {
  implicit val transferDecoder: Decoder[Transfer] = deriveDecoder[Transfer]
  implicit val transferEncoder: Encoder[Transfer] = deriveEncoder[Transfer]
}
