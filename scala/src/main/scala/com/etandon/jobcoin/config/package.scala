package com.etandon.jobcoin

package object config {
  case class Configuration(server: ServerConfig, api: ApiConfig)

  case class ServerConfig(host: String, port: Int)
  case class ApiConfig(baseUrl: String, addressesUrl: String,transactionsUrl: String )

  }
