package com.etandon.jobcoin.infra.datasources

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import com.etandon.jobcoin.config.Configuration
import com.etandon.jobcoin.domain.{Addresses, Transaction}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.ExecutionContext

class JobcoinClient()(implicit actorSystem: ActorSystem,materializer: Materializer,config: Configuration) {

  private def httpRequest(url: String) = HttpRequest(
    HttpMethods.GET,
    Uri(url)
  )
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  // Docs:
  // https://github.com/playframework/play-ws
  // https://www.playframework.com/documentation/2.6.x/ScalaJsonCombinators
  def getTransactions() = {
    Http()
    .singleRequest(httpRequest(config.api.transactionsUrl))
    .flatMap { res =>
      Unmarshal(res.entity)
        .to[List[Transaction]]
    }
  }
  def getAddresses(user: String) = {
    Http()
      .singleRequest(httpRequest(s"${config.api.addressesUrl}/$user"))
      .flatMap { res =>
        Unmarshal(res.entity)
          .to[Addresses]
      }
  }
}