package com.etandon.jobcoin.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Status(status: String)

object Status {
  implicit val statusDecoder: Decoder[Status] = deriveDecoder[Status]
  implicit val statusEncoder: Encoder[Status] = deriveEncoder[Status]
}
