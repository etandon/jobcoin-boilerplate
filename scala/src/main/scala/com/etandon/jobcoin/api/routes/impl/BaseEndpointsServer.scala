package com.etandon.jobcoin.api.routes.impl

import akka.http.scaladsl.server.Route
import endpoints.akkahttp.server
import endpoints.akkahttp.server.circe.JsonSchemaEntities

trait BaseEndpointsServer
  extends server.Endpoints
    with JsonSchemaEntities {
  def routes: Route
}
