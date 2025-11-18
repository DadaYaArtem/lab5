#!/bin/bash
# Скрипт для запуска консольных тестов

echo "=== Компиляция проекта ==="
mkdir -p target/classes
find src/main/java -name "*.java" | xargs javac -d target/classes

if [ $? -ne 0 ]; then
    echo "Ошибка компиляции!"
    exit 1
fi

echo -e "\n=== Запуск встроенных тестов ==="
echo "15" | java -cp target/classes com.grapheditor.ConsoleMain

echo -e "\n=== Тесты завершены ==="
