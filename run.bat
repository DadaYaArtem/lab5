@echo off
REM Скрипт запуска графового редактора для Windows

echo Компиляция проекта...
if not exist target\classes mkdir target\classes

dir /s /b src\*.java > sources.txt
javac -d target/classes @sources.txt
del sources.txt

if %errorlevel% equ 0 (
    echo Компиляция успешна!
    echo Запуск приложения...
    java -cp target/classes com.grapheditor.Main
) else (
    echo Ошибка компиляции!
    pause
    exit /b 1
)
