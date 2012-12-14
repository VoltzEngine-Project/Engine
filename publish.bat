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

set FILE_NAME=BasicComponents_v%MODVERSION%.%BUILD_NUMBER%.jar
set BACKUP_NAME=UniversalElectricity_v%MODVERSION%.%BUILD_NUMBER%_backup.zip
set API_NAME=UniversalElectricity_v%MODVERSION%.%BUILD_NUMBER%_api.zip

echo Starting to build %FILE_NAME%

::BUILD
runtime\bin\python\python_mcp runtime\recompile.py %*
runtime\bin\python\python_mcp runtime\reobfuscate.py %*

::ZIP-UP
cd reobf\minecraft\
"..\..\..\7za.exe" a "..\..\builds\%FILE_NAME%" "*"
cd ..\..\
cd resources\
"..\..\7za.exe" a "..\builds\%FILE_NAME%" "*"
"..\..\7za.exe" a "..\builds\%BACKUP_NAME%" "*"
cd ..\
cd src\
"..\..\7za.exe" a "..\builds\%API_NAME%" "*\universalelectricity\"

"..\..\7za.exe" a "..\builds\%BACKUP_NAME%" "*\universalelectricity\"
"..\..\7za.exe" a "..\builds\%BACKUP_NAME%" "*\basiccomponents\"
"..\..\7za.exe" a "..\builds\%BACKUP_NAME%" "*\buildcraft\"
"..\..\7za.exe" a "..\builds\%BACKUP_NAME%" "*\railcraft\"
"..\..\7za.exe" a "..\builds\%BACKUP_NAME%" "*\dan200\"
cd ..\

::UPDATE INFO FILE
echo %PROMOTION% %FILE_NAME% %API_NAME%>>info.txt

::GENERATE FTP Script
echo open %SERVER%>ftpscript.txt
echo %USERNAME%>>ftpscript.txt
echo %PASSWORD%>>ftpscript.txt
echo binary>>ftpscript.txt
echo put "recommendedversion.txt">>ftpscript.txt
echo put "builds\%FILE_NAME%">>ftpscript.txt
echo put "builds\%API_NAME%">>ftpscript.txt
echo put info.txt>>ftpscript.txt
echo quit>>ftpscript.txt
ftp.exe -s:ftpscript.txt
del ftpscript.txt

echo Done building %FILE_NAME%

pause