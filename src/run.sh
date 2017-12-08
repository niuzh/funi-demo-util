#!/bin/bash
#program
#    This program is used to startup java.jar
#
for (( i=1; i<=5; i++ ))
do
        echo "i=$i"
        java -cp /home/niu/Documents/funi-demo-util-1.0-SNAPSHOT.jar funi.demo.util.ScreenCaptureUtil /home/niu/Documents
        sleep 5
done
