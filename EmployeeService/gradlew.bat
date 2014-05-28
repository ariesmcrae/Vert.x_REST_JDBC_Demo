@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.

@rem set DEFAULT_JVM_OPTS=-server -XX:InitialHeapSize=4g -XX:MaxHeapSize=4g -XX:+UseG1GC -XX:+AggressiveOpts -XX:GCTimeRatio=19 -XX:NewSize=128m -XX:MaxNewSize=128m -XX:PermSize=64m -XX:MaxPermSize=64m -XX:SurvivorRatio=88 -XX:TargetSurvivorRatio=88 -XX:MaxTenuringThreshold=15 -XX:MaxGCMinorPauseMillis=1 -XX:MaxGCPauseMillis=5 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./gc_heap_dump/ -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintTenuringDistribution -Xloggc:./gc_log.log 
@rem set DEFAULT_JVM_OPTS=-server -XX:InitialHeapSize=2g -XX:MaxHeapSize=2g -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+AggressiveOpts -XX:+CMSParallelRemarkEnabled -XX:+CMSScavengeBeforeRemark -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=65 -XX:CMSWaitDuration=300000 -XX:GCTimeRatio=19 -XX:NewSize=128m -XX:MaxNewSize=128m -XX:PermSize=64m -XX:MaxPermSize=64m -XX:SurvivorRatio=88 -XX:TargetSurvivorRatio=88 -XX:MaxTenuringThreshold=15 -XX:MaxGCMinorPauseMillis=1 -XX:MaxGCPauseMillis=5 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./gc_heap_dump/ -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintTenuringDistribution -Xloggc:./gc_log.log -Dhttp.proxyHost=proxy.blah.blah.com.au -Dhttp.proxyPort=8080 -Dhttp.proxyUser=your_lan_username -Dhttp.proxyPassword=your_lan_password -Dhttps.proxyHost=proxy.blah.blah.com.au -Dhttps.proxyPort=8080 -Dhttps.proxyUser=your_lan_username -Dhttps.proxyPassword=your_lan_password
set DEFAULT_JVM_OPTS=-server -XX:InitialHeapSize=4g -XX:MaxHeapSize=4g -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./gc_heap_dump/


set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe
@rem set JAVA_EXE=/cygdrive/c/dev/tools/java/jdk1.8.0_05/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

@rem Execute Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable GRADLE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%GRADLE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
