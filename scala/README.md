# Scala Jobcoin

### Run
`sbt run`

Note: If sbt run does not work, then run the main method in Main.scala

The project consists of 2 major parts: 
1) API to assign coin mixer owned address to the set of addresses provided by the user:

Get Request: http://localhost:8080/api/assign?addresses=Sam1,Sam2,Sam3,Sam4

Sample response: 

`{
    "assignedAddress": "d4a17a7a-3b40-495b-88b8-d71adc12b850"
}`
![Alt text](docs/AssignAddressPostman.png?raw=true "Optional Title")

2) Core mixer functionality: 

The mixer core functionality is implemented using the actor: WithdrwalActor.scala.

The actor fetches the balance associated with the addresses created by the mixer. If any of the addresses have a non zero balance, the mixer transfers a random percentage of the balance to one of the randomly selected customer addresses. This happens for every address registered with the mixer that has a non zero balance.
The actor calls itself with the current time snapshot continues to poll the transactions between the last actor run and current timestamp. The process continues to infinitely poll for addresses that have balance to be transferred.

Step 1: Mixer address `092a10ae-9d7d-4d0f-b40a-ede77800c4b5` is assigned to customer address `Eshan1,Eshan2,Eshan3`

Step 2: Assign 200 coins to address `092a10ae-9d7d-4d0f-b40a-ede77800c4b5`
   
![Alt text](docs/Step2.png?raw=true "Optional Title")

Step 3: The mixer runs, picks up a random amount from the address `092a10ae-9d7d-4d0f-b40a-ede77800c4b5` in this case 108 and transfers it to `Eshan3`

![Alt text](docs/Step3.png?raw=true "Optional Title")

Logs:

![Alt text](docs/LogsStep3.png?raw=true "Optional Title")


Step 4: The mixer runs again, this time transfers the remaining balance of 92 to `Eshan2`

![Alt text](docs/Step4.png?raw=true "Optional Title")

Logs:

![Alt text](docs/LogsStep4.png?raw=true "Optional Title")

Step 5: The mixer runs again, but does not have any balance to transfer.

Logs:

![Alt text](docs/LogsStep5.png?raw=true "Optional Title")



### Test
`sbt test`
