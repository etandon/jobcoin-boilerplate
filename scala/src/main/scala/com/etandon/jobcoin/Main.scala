package com.etandon.jobcoin

import com.etandon.jobcoin.config.JobcoinConfigLoader
import com.typesafe.scalalogging.LazyLogging

object Main extends LazyLogging {
  def main(args: Array[String]) {

    JobcoinConfigLoader.load() match {
      case Right(config) =>
        new JobcoinApp(config)
      case Left(e) =>
        logger.error(
          s"Could not start Jobcoin server. Errors occured: ${e.toList
            .map(_.description)}")
        sys.exit(1)
    }
  }
}
