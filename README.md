# Project Title

A set of processes {Pi}, with i ranging from 1, 10 running on a set of servers. These processes are jointly responsible for maintaining the value of a global counter
 
                - Each process Pi listens for user commands on a port which are as follows:
 
                                - "inc" - increment the global counter by 1
                                - "dec" - decrement the global counter by 1
                                - "get" - the current value of global counter
 
                - A client can open a connection to any of the processes Pi and invoke any of the user commands
               
                - Each process Pi must "eventually" be able to give the correct current value of the global counter
 
                - Each process Pi takes the following args:
 
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
                               -  Client connecting to a process will either get a connection failure or the command will succeed. There are no “silent” failures
## Building and Running

cd GlobalCounter/src
javac *.java
java GlobalCounterMain -id 1 -file /home/abbas/javaprogs/GlobalCounter/res/processes.txt


