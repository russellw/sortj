call mvn package
if %errorlevel% neq 0 goto :eof

python test\test.py
