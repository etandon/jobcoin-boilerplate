package com.etandon.jobcoin.app

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext

class AddressService()(implicit executionContext: ExecutionContext) extends LazyLogging {
  //TODO: Available and used addresses should be saved in a persistence layer. Right now saving it in class variables to make things simple
  private var availAddresses: Set[String] = Set()
  private var addressMap: Map[String, List[String]] = Map.empty
  def this(initAddresses: Set[String])(implicit ec: ExecutionContext) = {
    this
    this.availAddresses =  initAddresses
  }
  //TODO: Check if the customer address has already been assigned to a mixer address, throw error if assigned?
  def assignAddress(customerAddress: List[String]): Option[String] = {
    val assignedAddress = availAddresses.headOption
    assignedAddress.map { case addr =>
      logger.debug(s"Assigned address: $addr to Customer Addresses: $customerAddress")
      addressMap = addressMap + (addr -> customerAddress)
      availAddresses = availAddresses.tail
      addr
    }
  }

  def getAddressMap = addressMap

}