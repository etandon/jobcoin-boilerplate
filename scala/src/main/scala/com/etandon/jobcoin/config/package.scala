package com.etandon.jobcoin

package object config {
  case class Configuration(server: ServerConfig)

  case class ServerConfig(host: String, port: Int)
  }
