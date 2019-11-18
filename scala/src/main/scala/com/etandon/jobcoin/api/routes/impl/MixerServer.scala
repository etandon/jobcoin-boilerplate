package com.etandon.jobcoin.api.routes.impl

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.etandon.jobcoin.api.data.AssignAddressDTO
import com.etandon.jobcoin.api.routes.traits.MixerEndpoints
import com.etandon.jobcoin.app.AddressService

import scala.concurrent.{ExecutionContext, Future}
/**
 * API routes for the application.
 *
 */
class MixerServer(addressService: AddressService)(implicit val ec: ExecutionContext,
                    as: ActorSystem,
                    am: ActorMaterializer
) extends BaseEndpointsServer with MixerEndpoints
{
  val routes: Route =
    assignAddress.implementedByAsync { case customerAddresses =>
      addressService.assignAddress(customerAddresses.split(",").toList).map{
        case a => Future.successful(AssignAddressDTO(a))
      }.getOrElse(Future.failed(new Exception("Could not assign address")))
      }
}
