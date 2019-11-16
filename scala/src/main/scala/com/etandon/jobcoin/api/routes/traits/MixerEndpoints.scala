package com.etandon.jobcoin.api.routes.traits

import com.etandon.jobcoin.api.routes.jsonschemas.TemplateJsonSchema
import endpoints.algebra.Endpoints


trait MixerEndpoints extends Endpoints with TemplateJsonSchema {

  lazy val assignAddress: Endpoint[Unit, String] =
    endpoint(
      get(path / "api" / "address" / "assign"),
      jsonResponse[String]())
}
