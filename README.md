# JeeTool
Arduino WSN network management tool

Requires source of Arduino 1.8.3 present in /opt/arduino-1.8.3 folder.

To install aditional libraries follow guide at: https://github.com/arduino/Arduino/blob/master/build/shared/manpage.adoc 
You can either place them directly in coresponding folders, use CLI commands or run GUI. 

JeeTool expects default paths for nodes to be in file /opt/motePaths.txt
Each non-empty is expected to be path to the device (e.g. /dev/ttyUSB0). For convenience purposes, you can use udev to automatically create symlinks to your USB devices using ATTRS atribute. Lines can be commented using #.

Currently JeeTool is configured for Arduino Mini with ATMega328 and all compatible boards (e.g. JeeNodes). This parameter can be changed in ShellExec class by modification of BOARD string. 

USAGE:

 -a,--motes <arg>         path to alternative file of motes, default
                          setting is stored in /opt/motePaths.txt
 -d,--detect              Perform node detection only
 -h,--help                show help and exit
 -i,--id <arg>            select IDs of nodes from motelist, use comma
                          separated list of IDs or ID ranges, such as
                          1,3,5-7,9-15
 -m,--make <arg>          make target at this path, .ino file required
 -s,--silent              show limited text output
 -t,--threads             use threads for paralell upload
 -u,--make_upload <arg>   make target  at this path and upload to nodes,
                          .ino file required
 -v,--verbose             show extended text output
