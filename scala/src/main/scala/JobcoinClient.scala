package com.gemini.jobcoin

import play.api.libs.ws._
import play.api.libs.ws.ahc._
import play.api.libs.ws.JsonBodyReadables._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import com.typesafe.config.Config
import akka.stream.Materializer

import scala.async.Async._
import scala.concurrent.{ExecutionContext, Future}
import DefaultBodyReadables._

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri}
import com.etandon.jobcoin.domain.Transaction
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

class JobcoinClient(config: Config)(implicit actorSystem: ActorSystem,materializer: Materializer) {

  private val apiAddressesUrl = config.getString("jobcoin.apiAddressesUrl")
  val httpRequest = HttpRequest(
    HttpMethods.GET,
    Uri(apiAddressesUrl)
  )
  implicit val ec: ExecutionContext = actorSystem.dispatcher

  // Docs:
  // https://github.com/playframework/play-ws
  // https://www.playframework.com/documentation/2.6.x/ScalaJsonCombinators
  def testGet() = Http()
    .singleRequest(httpRequest)
    .flatMap { res =>
      Unmarshal(res.entity)
        .to[Transaction]
    }
}