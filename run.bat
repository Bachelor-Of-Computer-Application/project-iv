@echo off
setlocal
set CONNECTOR=lib\mysql-connector-j.jar

if not exist "%CONNECTOR%" (
    echo Please place MySQL Connector/J at %CONNECTOR%
    echo Example file name: mysql-connector-j.jar
    pause
    exit /b 1
)

if not exist out mkdir out
for /r src %%f in (*.java) do echo %%f>>sources.txt
javac -cp "%CONNECTOR%" -d out @sources.txt
del sources.txt
java -cp "out;%CONNECTOR%" com.gamehub.Main
endlocal
