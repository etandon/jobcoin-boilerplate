package com.etandon.jobcoin.app

import java.time.{LocalDateTime, ZoneOffset}
import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import com.etandon.jobcoin.infra.datasources.JobcoinClient
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration.FiniteDuration

class WithdrawlActor(jobcoinClient: JobcoinClient)(implicit am: ActorMaterializer) extends Actor with LazyLogging {
  import context.dispatcher
  val r = new scala.util.Random
  def receive = {
    case task: String => println(s"$task: ${LocalDateTime.now(ZoneOffset.UTC)}")
    case (addressService: AddressService, lastPull: Option[LocalDateTime]) => {
      val now = LocalDateTime.now(ZoneOffset.UTC)
      logger.debug(s"$lastPull: $now: ${addressService.getAddressMap}")
      (for{
         trans <- jobcoinClient.getTransactions
         filteredT = trans
                    //TODO: Ideally last pull should be pulled from DB so that the application can pull data from where it left
                    .filter(t => lastPull.forall(lp => t.timestamp.isAfter(lp)))
                    .filter(_.timestamp.isBefore(now))
                    .filter(t => addressService.getAddressMap.contains(t.toAddress))
         addrBalance <- jobcoinClient.getAddresses(filteredT.map(_.toAddress))
      } yield {
        logger.debug(s"Transactions: $filteredT")
        logger.debug(s"Address Balance: $addrBalance")
        addrBalance.foreach { a =>

        }
        (addressService,now, "Transfer_Complete")
      }).pipeTo(self)(sender())
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
