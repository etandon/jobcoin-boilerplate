package com.etandon.jobcoin

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.etandon.jobcoin.api.routes.ApiServer
import com.etandon.jobcoin.app.{AddressService, WithdrawlActor}
import com.etandon.jobcoin.config.Configuration
import com.etandon.jobcoin.infra.datasources.AddressFileReader
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.concurrent.{ExecutionContext, Future}

class JobcoinApp(config: Configuration) extends LazyLogging {
  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val conf: Configuration = config

  val addressService = new AddressService(AddressFileReader.read.getOrElse(Set.empty))
  val apiServer = new ApiServer(addressService)
  val binding: Future[Http.ServerBinding] =
    Http()
      .bindAndHandle(apiServer.routes, config.server.host, config.server.port)

  val withdrawlActor = actorSystem.actorOf(Props(new WithdrawlActor()))

  val cancellable =
    actorSystem.scheduler
      .scheduleOnce(Duration.Zero, withdrawlActor, (addressService,None))

  sys.addShutdownHook {
    binding
      .flatMap(_.unbind())
      .onComplete(_ => actorSystem.terminate())
    cancellable.cancel()
  }


}