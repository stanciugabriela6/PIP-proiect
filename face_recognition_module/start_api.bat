@echo off
cd /d "%~dp0"
echo Pornire API Face Recognition pe http://127.0.0.1:8000 ...
C:\msys64\ucrt64\bin\python.exe -m uvicorn app:app --host 127.0.0.1 --port 8000
pause