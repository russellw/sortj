call mvn package
if %errorlevel% neq 0 goto :eof

python integration_test\test.py
