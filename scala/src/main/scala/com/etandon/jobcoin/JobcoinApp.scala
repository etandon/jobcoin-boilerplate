package com.etandon.jobcoin

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.etandon.jobcoin.api.routes.ApiServer
import com.etandon.jobcoin.config.Configuration
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

class JobcoinApp(config: Configuration) extends LazyLogging {
  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val conf: Configuration = config

  val apiServer = new ApiServer
  val binding: Future[Http.ServerBinding] =
    Http()
      .bindAndHandle(apiServer.routes, config.server.host, config.server.port)

  sys.addShutdownHook {
    binding
      .flatMap(_.unbind())
      .onComplete(_ => actorSystem.terminate())
  }

}