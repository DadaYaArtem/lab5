@echo off
REM Скрипт запуска графового редактора для Windows

echo Компиляция проекта...
if not exist target\classes mkdir target\classes

dir /s /b src\*.java > sources.txt
javac -d target/classes @sources.txt
del sources.txt

if %errorlevel% equ 0 (
    echo Компиляция успешна!
    echo Создание JAR файла...
    cd target\classes
    jar cfe ..\graph-editor.jar com.grapheditor.Main .
    cd ..\..
    echo JAR файл создан: target\graph-editor.jar
    echo Запуск приложения...
    java -jar target\graph-editor.jar
) else (
    echo Ошибка компиляции!
    pause
    exit /b 1
)
