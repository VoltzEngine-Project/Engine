::UNIVERSAL ELECTRICITY BUILD SCRIPT
@echo off
echo Promotion Type?
set /p PROMOTION=

echo FTP Server?
set /p SERVER=
echo FTP Username?
set /p USERNAME=
echo FTP Password?
set /p PASSWORD=

set /p MODVERSION=<modversion.txt
set /p CurrentBuild=<buildnumber.txt
set /a BUILD_NUMBER=%CurrentBuild%+1
echo %BUILD_NUMBER% >buildnumber.txt

if %PROMOTION%==* (
	echo %MODVERSION% >recommendedversion.txt
)

set API_NAME=UniversalElectricity_v%MODVERSION%.%BUILD_NUMBER%.zip
set FILE_NAME=BasicComponents_v%MODVERSION%.%BUILD_NUMBER%.jar
set DEV_NAME=BCDev_v%MODVERSION%.%BUILD_NUMBER%.jar
set BACKUP_NAME=UniversalElectricity_v%MODVERSION%.%BUILD_NUMBER%_backup.zip

echo Starting to build %FILE_NAME%

::BUILD
runtime\bin\python\python_mcp runtime\recompile.py %*
runtime\bin\python\python_mcp runtime\reobfuscate.py %*

::ZIP-UP
cd reobf\minecraft\
7z a "..\..\builds\%FILE_NAME%" "*"
cd ..\..\

cd eclipse\Minecraft\bin\
7z a "..\..\..\builds\%DEV_NAME%" "universalelectricity"
cd ..\..\..\

cd resources\
7z a "..\builds\%FILE_NAME%" "*"
7z a "..\builds\%API_NAME%" "*"
7z a "..\builds\%DEV_NAME%" "*"
7z a "..\builds\%BACKUP_NAME%" "*"
cd ..\
cd src\
7z a "..\builds\%API_NAME%" "*\universalelectricity\"

7z a "..\builds\%BACKUP_NAME%" "*\universalelectricity\"
cd ..\

::UPDATE INFO FILE
echo %PROMOTION% %API_NAME% %FILE_NAME% %DEV_NAME%>>info.txt

::GENERATE FTP Script
echo open %SERVER%>ftpscript.txt
echo %USERNAME%>>ftpscript.txt
echo %PASSWORD%>>ftpscript.txt
echo binary>>ftpscript.txt
echo put "recommendedversion.txt">>ftpscript.txt
echo put "builds\%FILE_NAME%">>ftpscript.txt
echo put "builds\%DEV_NAME%">>ftpscript.txt
echo put "builds\%API_NAME%">>ftpscript.txt
echo put info.txt>>ftpscript.txt
echo quit>>ftpscript.txt
ftp.exe -s:ftpscript.txt
del ftpscript.txt

echo Done building %FILE_NAME%

pause