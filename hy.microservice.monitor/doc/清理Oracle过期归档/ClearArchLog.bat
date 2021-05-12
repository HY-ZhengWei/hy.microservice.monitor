set ORACLE_SID=orcl
set arch_log=D:\TimingClearArchLog
rman target / cmdfile='%arch_log%\script.txt' log='%arch_log%\del_arch_%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%".log'
forfiles /P %arch_log% /M *.log /S /D -15 /C "cmd /c del /F /s /q @file"