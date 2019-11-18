package com.etandon.jobcoin.app

import java.time.{LocalDateTime, ZoneOffset}
import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import com.etandon.jobcoin.domain.Transfer
import com.etandon.jobcoin.infra.datasources.JobcoinClient
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration.FiniteDuration

class WithdrawlActor(jobcoinClient: JobcoinClient)(implicit am: ActorMaterializer) extends Actor with LazyLogging {
  import context.dispatcher
  val r = new scala.util.Random
  def receive = {
    case (addressService: AddressService, lastPull: Option[LocalDateTime]) => {
      val now = LocalDateTime.now(ZoneOffset.UTC)
      logger.debug(s"$lastPull: $now: ${addressService.getAddressMap}")
      val response = for {
         trans <- jobcoinClient.getTransactions
         filteredT = trans
                    //TODO: Ideally last pull should be pulled from DB so that the application can pull data from where it left
                    .filter(t => lastPull.forall(lp => t.timestamp.isAfter(lp)))
                    .filter(_.timestamp.isBefore(now))
                    .filter(t => addressService.getAddressMap.contains(t.toAddress) || addressService.getAddressMap.contains(t.fromAddress.getOrElse("Address_Not_Found")))
         filteredAddress = filteredT.map(t => if(addressService.getAddressMap.contains(t.toAddress)) t.toAddress else t.fromAddress.get).toSet.toList
         addressBalance <- jobcoinClient.getAddresses(filteredAddress)
         addressBalanceFiltered = addressBalance.filter(b => b._2.balance.toInt > 0)
         toAddressMap = addressService.getAddressMap
         transferList = addressBalanceFiltered.map(a => Transfer(a._1, getRandomFromList(toAddressMap.get(a._1).get), (1 + r.nextInt(a._2.balance.toInt)).toString))
         status <- jobcoinClient.postTransactions(transferList)
      } yield {
        logger.debug(s"Transactions: $filteredT")
        logger.debug(s"FilteredAddress: $filteredAddress")
        logger.debug(s"Address Balance: $addressBalance")
        logger.debug(s"Address Balance Filtered: $addressBalanceFiltered")
        logger.debug(s"Transfer List: $transferList")
        logger.debug(s"Status: $status")
        (addressService,now, "Transfer_Complete")
      }
        response.pipeTo(self)(sender())
    }
    case (addressService: AddressService, lastPull: LocalDateTime, action: String) => {
      logger.debug(s"Action: $action")
      rescheduleActor(addressService, lastPull)
    }
  }

  def rescheduleActor(addressService: AddressService,lastPull: LocalDateTime) = {
    val delay = 10 + r.nextInt(30)
    logger.debug(s"Adding a random delay of $delay")
    context.system.scheduler.scheduleOnce(FiniteDuration(delay, TimeUnit.SECONDS), self, (addressService, Some(lastPull)))
  }

  def getRandomFromList(list: List[String]): String = list(r.nextInt(list.length))

}
