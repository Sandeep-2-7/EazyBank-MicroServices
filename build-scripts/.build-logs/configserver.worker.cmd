@echo off
cd /d "S:\Inside\SHIFT\Java_Madhan\Microservices\configserver"
call mvn compile jib:dockerBuild > "S:\Inside\SHIFT\Java_Madhan\Microservices\build-scripts\.build-logs\configserver.log" 2>&1
echo %errorlevel% > "S:\Inside\SHIFT\Java_Madhan\Microservices\build-scripts\.build-logs\configserver.done"
