# Система управления учебными заданиями с антиплагиатом

## Описание
Система предназначена для управления учебным процессом в школах, включая создание классов, назначение заданий ученикам, проверку работ на плагиат и анализ результатов. Основные функции:
- Управление классами, учениками и учителями
- Распределение и проверка заданий
- Автоматическая проверка работ на антиплагиат
- Формирование отчетов о схожести работ

## Основные функции
1. **Управление пользователями:**
    - Регистрация учеников и учителей
    - Разграничение ролей (STUDENT/TEACHER)
    - Привязка учеников к классам

2. **Работа с заданиями:**
    - Создание заданий с указанием сроков выполнения
    - Автоматический контроль актуальности заданий
    - Загрузка решений учениками

3. **Антиплагиат система:**
    - Автоматическая проверка загруженных работ
    - Формирование отчетов о схожести решений
    - Повторное сканирование по требованию

4. **Аналитика:**
    - Просмотр результатов проверки
    - Сравнение работ внутри класса
    - Выявление схожих решений

## Технологический стек
- **Backend:** Java 21, Spring Boot 3.5
- **База данных:** PostgreSQL 16
- **Брокер сообщений:** Apache Kafka
- **Хранилище файлов:** AWS S3-совместимое хранилище
- **Аутентификация:** JWT `(не реализовано)`
- **Контейнеризация:** Docker
- **Документация API:** OpenAPI

## Архитектура системы
Система состоит из двух основных компонентов:
1. **Основной сервис:**
    - Управление пользователями, классами и заданиями
    - Работа с файлами (загрузка/скачивание)
    - Отправка заданий на проверку в антиплагиат
    - Прием результатов проверки

2. **Сервис антиплагиата:**
    - Скачивание файлов из хранилища
    - Анализ текстового содержимого
    - Расчет процента схожести работ
    - Отправка результатов в основной сервис

## Инструкция по запуску
### Требования:
- Git
- Docker и Docker Compose

### Запуск:
1. **Клонировать репозиторий:**
```bash
git clone https://github.com/IT-Egor/antiplagiacode.git
cd antiplagiacode
```

2. **Запустить сервисы:**
```bash
docker-compose up -d
```

## Пример использования
### Расчет процента схожести файлов
1. **создание пользователей**
```bash
curl -X POST http://localhost:8080/api/v1/user \
  -H "Content-Type: application/json" \
  -d '{
    "username": "new_student1",
    "password": "pass123",
    "role": "STUDENT"
  }'
```
```bash
curl -X POST http://localhost:8080/api/v1/user \
  -H "Content-Type: application/json" \
  -d '{
    "username": "new_student2",
    "password": "pass123",
    "role": "STUDENT"
  }'
```
```bash
curl -X POST http://localhost:8080/api/v1/user \
  -H "Content-Type: application/json" \
  -d '{
    "username": "new_student3",
    "password": "pass123",
    "role": "STUDENT"
  }'
```

2. **создание класса**
```bash
curl -X POST http://localhost:8080/api/v1/class \
  -H "Content-Type: application/json" \
  -d '{
    "name": "11И"
  }'
```

3. **привязка учеников к классу**
```bash
curl -X POST "http://localhost:8080/api/v1/student/post-many?studentIds=1&studentIds=2&studentIds=3&classId=1" \
  -H "Content-Type: application/json"
```

4. **создание задания**
```bash
curl -X POST http://localhost:8080/api/v1/task \
  -H "Content-Type: application/json" \
  -d '{
    "name": "IT homework",
    "startDate": "yyyy-mm-dd",
    "endDate": "yyyy-mm-dd",
    "description": "description",
    "classId": 1
  }'
```
Укажите дату начала и конца задания. Дата должна быть позже и равна сегодняшней

5. **загрузка файла**
```bash
curl -X POST "http://localhost:8080/api/v1/file?studentId=1&taskId=1" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/your/file.txt"
```
```bash
curl -X POST "http://localhost:8080/api/v1/file?studentId=2&taskId=1" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/your/file.txt"
```
```bash
curl -X POST "http://localhost:8080/api/v1/file?studentId=3&taskId=1" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/your/file.txt"
```
Где: `/path/to/your/file.txt` — замените на реальный путь к файлу на Вашем устройстве  
Для получения различных результатов стоит прикрепить 3 различных файла

6. **просмотр всех результатов сравнения**
```bash
curl "http://localhost:8080/api/v1/comparison-result?page=0&size=10&sort=id,asc"
```

7. **скачивание загруженного файла**
```bash
curl "http://localhost:8080/api/v1/file/download/1"
```

## Документация API
Документация API будет доступна после запуска сервисов по адресам:
- JSON API docs: http://localhost:8080/v3/api-docs
- Swagger UI: http://localhost:8080/swagger-ui.html