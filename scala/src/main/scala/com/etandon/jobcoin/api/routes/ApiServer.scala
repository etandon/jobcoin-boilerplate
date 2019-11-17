package com.etandon.jobcoin.api.routes

import com.etandon.jobcoin.api.routes.impl.MixerServer

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.etandon.jobcoin.app.AddressService

import scala.concurrent.ExecutionContext

class ApiServer(addressService: AddressService)(implicit
                  actorSystem: ActorSystem,
                  ec: ExecutionContext,
                  actorMaterializer: ActorMaterializer) {
  private lazy val apiServers = List(new MixerServer(addressService))
  val preflight: Route =
    options {
      complete(HttpResponse(StatusCodes.OK))
    }
  lazy val routes: Route =
        apiServers.map(_.routes).reduce((a, b) => a ~ b)



}
