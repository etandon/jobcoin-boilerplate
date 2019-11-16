package com.etandon.jobcoin.api.routes.impl

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.etandon.jobcoin.api.routes.traits.MixerEndpoints
import com.etandon.jobcoin.infra.datasources.AddressFileReader

import scala.concurrent.{ExecutionContext, Future}

class MixerServer()(implicit val ec: ExecutionContext,
                    as: ActorSystem,
                    am: ActorMaterializer
) extends BaseEndpointsServer with MixerEndpoints
{
  val routes: Route =
    assignAddress.implementedByAsync { case _ =>
      Future.successful(AddressFileReader.read.map(_.mkString).getOrElse("Not Found"))
      }
}
