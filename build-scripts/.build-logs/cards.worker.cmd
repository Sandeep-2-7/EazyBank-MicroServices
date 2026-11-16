@echo off
cd /d "S:\Inside\SHIFT\Java_Madhan\Microservices\cards"
call mvn compile jib:dockerBuild > "S:\Inside\SHIFT\Java_Madhan\Microservices\build-scripts\.build-logs\cards.log" 2>&1
echo %errorlevel% > "S:\Inside\SHIFT\Java_Madhan\Microservices\build-scripts\.build-logs\cards.done"
