rem Python
black .
if %errorlevel% neq 0 goto :eof

rem main
call google-java-format -i src/main/java/sortj/*.java
if %errorlevel% neq 0 goto :eof

call sortj              -i src/main/java/sortj/*.java
if %errorlevel% neq 0 goto :eof

call google-java-format -i src/main/java/sortj/*.java
if %errorlevel% neq 0 goto :eof

rem test
call google-java-format -i src/test/java/sortj/*.java
if %errorlevel% neq 0 goto :eof

git diff
