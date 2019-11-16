package com.etandon.jobcoin.config

import com.typesafe.config.ConfigFactory
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._

object JobcoinConfigLoader {
  //TODO: Handle configuration based on different environments (Dev, QA, UAT, Prod)
  def load(): Either[ConfigReaderFailures, Configuration] = {
    val configLoad = ConfigFactory.load()
    pureconfig.loadConfig[Configuration](configLoad, "jobcoin")
  }
}
