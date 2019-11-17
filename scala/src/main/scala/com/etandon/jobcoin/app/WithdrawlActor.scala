package com.etandon.jobcoin.app

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import com.etandon.jobcoin.infra.datasources.JobcoinClient
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

class WithdrawlActor(jobcoinClient: JobcoinClient)(implicit am: ActorMaterializer) extends Actor with LazyLogging {
  import context.dispatcher
  val r = new scala.util.Random
  def receive = {
    case task: String => println(s"$task: ${LocalDateTime.now}")
    case (addressService: AddressService, lastPull: Option[LocalDateTime]) => {
      val now = LocalDateTime.now
      logger.debug(s"$lastPull: $now: ${addressService.getAddressMap}")
      //Future.successful((addressService,now, "Transfer_Complete")).pipeTo(self)(sender())
      jobcoinClient.getTransactions.map{
        case t => {
          logger.debug(s"Transactions: $t")
          (addressService,now, "Transfer_Complete")
        }
      }.pipeTo(self)(sender())
    }
    case (addressService: AddressService, lastPull: LocalDateTime, action: String) => {
      logger.debug(s"Action: $action")
      rescheduleActor(addressService, lastPull)
    }
  }

  def rescheduleActor(addressService: AddressService,lastPull: LocalDateTime) = {
    val delay = r.nextInt(30)
    logger.debug(s"Adding a random delay of $delay")
    context.system.scheduler.scheduleOnce(FiniteDuration(delay, TimeUnit.SECONDS), self, (addressService, Some(lastPull)))
  }

}
