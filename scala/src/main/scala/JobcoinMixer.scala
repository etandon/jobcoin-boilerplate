package com.gemini.jobcoin

import java.util.UUID

import scala.io.StdIn
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.etandon.jobcoin.config.{Configuration, JobcoinConfigLoader}
import com.etandon.jobcoin.domain.Transfer
import com.etandon.jobcoin.infra.datasources.JobcoinClient

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object JobcoinMixer {
  object CompletedException extends Exception { }

  def main(args: Array[String]): Unit = {
    // Create an actor system
    implicit val actorSystem = ActorSystem()
    implicit val materializer = ActorMaterializer()

    // Load Config
    // Test HTTP client
    JobcoinConfigLoader.load() match {
      case Right(config) => {
        implicit val conf: Configuration = config
        val client = new JobcoinClient()
        //println(Await.result(client.getTransactions,Duration.Inf))
        val transfer = Transfer("092a10ae-9d7d-4d0f-b40a-ede77800c4b5","Eshan1","1")
        println(Await.result(client.postTransaction(transfer),Duration.Inf))
      }
      case Left(e) =>
        println(
          s"Could not start Jobcoin server. Errors occured: ${e.toList
            .map(_.description)}")
        sys.exit(1)
    }


//     println(Await.result(client.getAddresses("Eshan") ,Duration.Inf))

    try {
      while (true) {
        println(prompt)
        val line = StdIn.readLine()

        if (line == "quit") throw CompletedException
        
        val addresses = line.split(",")
        if (line == "") {
          println(s"You must specify empty addresses to mix into!\n$helpText")
        } else {
          val depositAddress = UUID.randomUUID()
          println(s"You may now send Jobcoins to address $depositAddress. They will be mixed and sent to your destination addresses.")
        }
      }
    } catch {
      case CompletedException => println("Quitting...")
    } finally {
      actorSystem.terminate()
    }
  }

  val prompt: String = "Please enter a comma-separated list of new, unused Jobcoin addresses where your mixed Jobcoins will be sent."
  val helpText: String =
    """
      |Jobcoin Mixer
      |
      |Takes in at least one return address as parameters (where to send coins after mixing). Returns a deposit address to send coins to.
      |
      |Usage:
      |    run return_addresses...
    """.stripMargin
}
