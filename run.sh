#!/bin/bash
# Скрипт запуска графового редактора

echo "Компиляция проекта..."
mkdir -p target/classes
find src -name "*.java" | xargs javac -d target/classes

if [ $? -eq 0 ]; then
    echo "Компиляция успешна!"
    echo "Запуск приложения..."
    java -cp target/classes com.grapheditor.Main
else
    echo "Ошибка компиляции!"
    exit 1
fi
