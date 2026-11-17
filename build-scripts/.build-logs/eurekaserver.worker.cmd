@echo off
cd /d "S:\Inside\SHIFT\Java_Madhan\Microservices\eurekaserver"
call mvn compile jib:dockerBuild > "S:\Inside\SHIFT\Java_Madhan\Microservices\build-scripts\.build-logs\eurekaserver.log" 2>&1
echo %errorlevel% > "S:\Inside\SHIFT\Java_Madhan\Microservices\build-scripts\.build-logs\eurekaserver.done"
