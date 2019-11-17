package com.etandon.jobcoin.api.routes.traits

import com.etandon.jobcoin.api.data.AssignAddressDTO
import com.etandon.jobcoin.api.routes.jsonschemas.TemplateJsonSchema
import endpoints.algebra.{Endpoints, Urls}


trait MixerEndpoints extends Endpoints with TemplateJsonSchema with Urls {

  lazy val assignAddress: Endpoint[String, AssignAddressDTO] =
    endpoint(
      get(path / "api" / "assign" /? qs[String]("addresses")),// segment[String]("list")),
      jsonResponse[AssignAddressDTO]())
}
