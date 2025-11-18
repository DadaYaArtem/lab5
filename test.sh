#!/bin/bash
# Скрипт для компиляции и запуска тестов без Maven

echo "=== Компиляция основного кода ==="
mkdir -p target/classes
find src/main/java -name "*.java" | xargs javac -d target/classes

if [ $? -ne 0 ]; then
    echo "Ошибка компиляции основного кода!"
    exit 1
fi

echo -e "\n=== Загрузка JUnit ==="
JUNIT_JAR="target/junit-4.13.2.jar"
HAMCREST_JAR="target/hamcrest-core-1.3.jar"

if [ ! -f "$JUNIT_JAR" ]; then
    echo "Скачивание JUnit..."
    wget -q -O "$JUNIT_JAR" https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar
fi

if [ ! -f "$HAMCREST_JAR" ]; then
    echo "Скачивание Hamcrest..."
    wget -q -O "$HAMCREST_JAR" https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
fi

echo -e "\n=== Компиляция тестов ==="
mkdir -p target/test-classes
find src/test/java -name "*.java" | xargs javac -cp "target/classes:$JUNIT_JAR:$HAMCREST_JAR" -d target/test-classes

if [ $? -ne 0 ]; then
    echo "Ошибка компиляции тестов!"
    exit 1
fi

echo -e "\n=== Запуск тестов ==="
java -cp "target/classes:target/test-classes:$JUNIT_JAR:$HAMCREST_JAR" org.junit.runner.JUnitCore \
    com.grapheditor.model.GraphTest \
    com.grapheditor.model.NodeTest \
    com.grapheditor.model.EdgeTest \
    com.grapheditor.algorithms.GraphAlgorithmsTest

echo -e "\n=== Тесты завершены ==="
