#!/bin/bash
# Демонстрационный скрипт консольной версии с автозаполнением

echo "=== Демонстрация консольной версии ==="
echo ""
echo "Автоматически выполняется:"
echo "1. При запуске создаются 7 примеров графов"
echo "2. Можно сразу работать с ними без ввода данных"
echo ""

# Создаем команды для демонстрации
cat > /tmp/demo_commands.txt << 'EOF'
14
6
0
0
EOF

echo "Выполняемые команды:"
echo "  14 - Список графов (покажет все созданные примеры)"
echo "  6  - Информация о графе (выберем первый)"
echo "  0  - Выход"
echo ""
echo "Запуск..."
echo ""

java -cp target/classes com.grapheditor.ConsoleMain < /tmp/demo_commands.txt

rm /tmp/demo_commands.txt
