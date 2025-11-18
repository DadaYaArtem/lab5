#!/bin/bash
# Скрипт запуска графового редактора

echo "Компиляция проекта..."
mkdir -p target/classes
find src -name "*.java" | xargs javac -d target/classes

if [ $? -eq 0 ]; then
    echo "Компиляция успешна!"
    echo "Создание JAR файла..."
    cd target/classes
    jar cfe ../graph-editor.jar com.grapheditor.Main .
    cd ../..
    echo "JAR файл создан: target/graph-editor.jar"
    echo "Запуск приложения..."
    java -jar target/graph-editor.jar
else
    echo "Ошибка компиляции!"
    exit 1
fi
