# Project Title

A set of processes {Pi}, with i ranging from 1, 10 running on a set of servers. These processes are jointly responsible for maintaining the value of a global counter
 
Each process Pi listens for user commands on a port which are as follows:
 
 - "inc" - increment the global counter by 1
 - "dec" - decrement the global counter by 1
 - "get" - the current value of global counter
A client can open a connection to any of the processes Pi and invoke any of the user commands
Each process Pi must "eventually" be able to give the correct current value of the global counter
Each process Pi takes the following args:
 - "-id" identifier i ranging from 1, 10
 - "-file" a file containing IP, and two ports for all processes {Pi}
 - First is the listen port for user commands
 - Second is the listen port for internal communication between processes
 - The configuration of Pi will be found at line i in the file
 - Example
  - Cmd Line for Process with ID 1: ./process_exe -id 1 -file processes.txt
                                                - File : processes.txt (10 lines for 10 processes)
                                               
--- snippet begin ---
10.10.10.10 6800 6801
10.10.10.10 6802 6803
--- snippet end ---
- Assumptions:
    - Processes do not crash
    - There can be network partitions between processes, but assume that partitions recover with some known bound
    - The commands are latency sensitive
    -  Client connecting to a process will either get a connection failure or the 	      command will succeed. There are no “silent” failures

## Building and Running

cd GlobalCounter/src
javac *.java
java GlobalCounterMain -id 1 -file /home/abbas/javaprogs/GlobalCounter/res/processes.txt
Same can be done for other process by just changing the -id to 2,3 etc.

How to test 
To test we have made a test client UserCommandClient.java
To run the test client 
cd GlobalCounter/src
javac UserCommandClient.java
java UserCommandClient

## Algorithm 

	- Each Process maintain its local copy of how many inc request(addCounter) and how many dec request (subCounter) are recieved by each process.
	- When the client asks for get request the global count is calculated and returned as follows :- 
				count = sum(addCounters) - sum(subCounter) 
	- When any of the process receives a "inc" request it increments its addCounter[currProcessNumber] by 1
	- When any of the process receives a "dec" request it increments its subCounter[currProcessNumber] by 1
	- When any process increments its addCounter[currProcessNumber] or subCounter[currProcessNumber] it publishes it state (complete arrays addCounter and subCounter) to other processes.
	-When any process receives a sync request it will read the incoming values of addCounter and subCounter and then merge it with its own addCounter and subCounter.The merge here is simple as addCounter and subCounter are always incrementing values and hence we can take the highest value between the incoming array and the one currently present with the process.
	
## Description & Responsiblities of the various classes.
 -GlobalCounterMain - This is starting point of the processes. Its main responsiblities are 
 				* Parse the input command line arguments and validate them.
 				* Create all the objects required for the program to work.
 				* Start the UserCommnd server
 				* Start the Sync server
 - InputFileParser - This class parses the file containing the list of processes.
 -GlobalDataHolder - This responsiblities of this class are 
 				* Holds the current state of the global inc, dec arrays.
 				* Provides a ability to merge its state with values coming from another process.
 				* Publish its state with the help of SyncStatePublisher to other processes.
-SyncStatePublisher & SyncCommandProcessorRunnable - The responsiblity of these classes is to publish its current state to other processes in different threads.

-SyncCommandProcessor & SyncCommandProcessorRunnable - The responsiblity of this classes is to listen for sync commands and merge the values coming from other processes with its own GlobalDataHolder.

-UserCommandProcessor &  UserCommandProcessorRunnable - The responsiblity of this classes is to listen for user commands and update/return values based on the user command accordingly.






