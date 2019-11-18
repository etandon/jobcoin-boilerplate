# Scala Jobcoin
Simple base project for the Jobcoin project using Scala and SBT. It accepts return address as arguments and prints out a deposit address to the user for them to send their funds to. The rest of the application is left unimplemented.

### Run
`sbt run`

Note: If sbt run does not work run the main method in Main.scala

The project consists of 2 major parts: 
1) API to assign coin mixer owned address to the set of addresses provided by the user:

Get Request: http://localhost:8080/api/assign?addresses=Sam1,Sam2,Sam3,Sam4

Sample response: 

`{
    "assignedAddress": "d4a17a7a-3b40-495b-88b8-d71adc12b850"
}`
![Alt text](docs/AssignAddressPostman.png?raw=true "Optional Title")

2) Core mixer functionality: 


### Test
`sbt test`
