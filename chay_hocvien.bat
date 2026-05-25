@echo off
chcp 65001 > nul
title Quan Ly Hoc Vien
set JAVA_HOME=C:\Program Files\Java\jdk-25.0.2
set MVN=C:\maven\apache-maven-3.9.9\bin\mvn.cmd
cd /d "%~dp0App"
"%MVN%" -q compile
"%MVN%" exec:java -Dexec.mainClass=com.trungtam.AppLauncher
pause
