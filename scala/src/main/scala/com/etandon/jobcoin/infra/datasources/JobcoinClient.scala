package com.etandon.jobcoin.infra.datasources

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpEntity, HttpMethod, HttpMethods, HttpRequest, RequestEntity, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import com.etandon.jobcoin.config.Configuration
import com.etandon.jobcoin.domain.{Addresses, Status, Transaction, Transfer}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.{ExecutionContext, Future}

class JobcoinClient()(implicit actorSystem: ActorSystem,materializer: Materializer,config: Configuration) {

  private def httpGetRequest(url: String, method: HttpMethod, entity: RequestEntity = HttpEntity.Empty) = HttpRequest(
    method,
    Uri(url),
    entity = entity
  )
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  // Docs:
  // https://github.com/playframework/play-ws
  // https://www.playframework.com/documentation/2.6.x/ScalaJsonCombinators
  def getTransactions() = {
    Http()
    .singleRequest(httpGetRequest(config.api.transactionsUrl,HttpMethods.GET))
    .flatMap { res =>
      Unmarshal(res.entity)
        .to[List[Transaction]]
    }
  }
  def getAddress(user: String) = {
    Http()
      .singleRequest(httpGetRequest(s"${config.api.addressesUrl}/$user",HttpMethods.GET))
      .flatMap { res =>
        Unmarshal(res.entity)
          .to[Addresses]
      }.map(a => (user, a))
  }

  def getAddresses(users: List[String]) = Future.sequence(users.map(getAddress(_)))

  def postTransaction(transaction: Transfer) = {
    Marshal(transaction).to[RequestEntity].flatMap(entity =>
        Http()
          .singleRequest(httpGetRequest(config.api.transactionsUrl, HttpMethods.POST, entity))
          .flatMap { res =>
            Unmarshal(res.entity)
              .to[Status]
          }
    )
  }

  def postTransactions(transactions: List[Transfer]) = Future.sequence(transactions.map(postTransaction(_)))


}