package com.etandon.jobcoin

import com.etandon.jobcoin.config.JobcoinConfigLoader
import com.typesafe.scalalogging.LazyLogging

/**
 * Main class to start the Application.
 */

object Main extends LazyLogging {
  /**
   * Loads the configuration in the memory, exits f the configuration is not found.
   * If the config is good, bootstraps the application using JobcoinApp
   */
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
