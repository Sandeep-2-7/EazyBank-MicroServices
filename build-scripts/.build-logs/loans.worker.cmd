@echo off
cd /d "S:\Inside\SHIFT\Java_Madhan\Microservices\loans"
call mvn compile jib:dockerBuild > "S:\Inside\SHIFT\Java_Madhan\Microservices\build-scripts\.build-logs\loans.log" 2>&1
echo %errorlevel% > "S:\Inside\SHIFT\Java_Madhan\Microservices\build-scripts\.build-logs\loans.done"
