#!/bin/bash
# Полная демонстрация всех возможностей

echo "=== ПОЛНАЯ ДЕМОНСТРАЦИЯ ГРАФОВОГО РЕДАКТОРА ==="
echo ""
echo "При запуске автоматически создаются 7 примеров:"
echo "  1. Дерево (звезда с 5 узлами)"
echo "  2. Треугольник (цикл)"
echo "  3. K4 (полный граф)"
echo "  4. Путь P5"
echo "  5. Ориентированный граф"
echo "  6. K2 (для произведений)"
echo "  7. Квадрат (гамильтонов цикл)"
echo ""
echo "Сейчас будет выполнена демонстрация всех функций..."
echo ""
read -p "Нажмите Enter для продолжения..."

# Демонстрация 1: Список графов
echo ""
echo "=== 1. СПИСОК ВСЕХ ГРАФОВ ==="
cat > /tmp/demo1.txt << 'EOF'
14
0
EOF
java -cp target/classes com.grapheditor.ConsoleMain < /tmp/demo1.txt 2>/dev/null | grep -A 20 "Список графов"

# Демонстрация 2: Проверка на дерево
echo ""
echo "=== 2. ПРОВЕРКА ЯВЛЯЕТСЯ ЛИ ГРАФ ДЕРЕВОМ ==="
echo "Проверяем граф 'Дерево'..."
cat > /tmp/demo2.txt << 'EOF'
7
3
0
EOF
java -cp target/classes com.grapheditor.ConsoleMain < /tmp/demo2.txt 2>/dev/null | grep -A 2 "Граф"

echo ""
echo "Проверяем граф 'Треугольник' (цикл, не дерево)..."
cat > /tmp/demo3.txt << 'EOF'
7
1
0
EOF
java -cp target/classes com.grapheditor.ConsoleMain < /tmp/demo3.txt 2>/dev/null | grep -A 2 "Граф"

# Демонстрация 3: Гамильтонов цикл
echo ""
echo "=== 3. ПОИСК ГАМИЛЬТОНОВА ЦИКЛА ==="
echo "В графе 'Квадрат'..."
cat > /tmp/demo4.txt << 'EOF'
8
5
0
EOF
java -cp target/classes com.grapheditor.ConsoleMain < /tmp/demo4.txt 2>/dev/null | grep -A 3 "Поиск"

# Демонстрация 4: Диаметр, радиус, центр
echo ""
echo "=== 4. ДИАМЕТР, РАДИУС, ЦЕНТР ==="
echo "Для полного графа K4..."
cat > /tmp/demo5.txt << 'EOF'
9
4
0
EOF
java -cp target/classes com.grapheditor.ConsoleMain < /tmp/demo5.txt 2>/dev/null | grep -A 5 "Диаметр"

# Демонстрация 5: Декартово произведение
echo ""
echo "=== 5. ДЕКАРТОВО ПРОИЗВЕДЕНИЕ ==="
echo "K2 × K2..."
cat > /tmp/demo6.txt << 'EOF'
10
2
2
0
EOF
java -cp target/classes com.grapheditor.ConsoleMain < /tmp/demo6.txt 2>/dev/null | grep -A 3 "Создан граф"

# Демонстрация 6: Информация о графе
echo ""
echo "=== 6. ИНФОРМАЦИЯ О ГРАФЕ 'K4' ==="
cat > /tmp/demo7.txt << 'EOF'
6
4
0
EOF
java -cp target/classes com.grapheditor.ConsoleMain < /tmp/demo7.txt 2>/dev/null | grep -A 20 "Информация о графе"

# Очистка
rm /tmp/demo*.txt

echo ""
echo "=== ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА ==="
echo ""
echo "Все примеры графов уже созданы при запуске!"
echo "Никакого ручного ввода не требуется."
echo ""
echo "Запустите программу для интерактивной работы:"
echo "  java -cp target/classes com.grapheditor.ConsoleMain"
