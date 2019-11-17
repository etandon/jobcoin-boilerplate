package com.etandon.jobcoin.app

import java.time.LocalDateTime

import akka.actor.Actor

class WithdrawlActor extends Actor {

  def receive = {
    case task: String => println(s"$task: ${LocalDateTime.now}")
    case addressService: AddressService => {
      println(s"${LocalDateTime.now}: ${addressService.getAddressMap}")
    }
  }

}
