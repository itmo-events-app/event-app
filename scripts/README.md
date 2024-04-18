clean - останавлвает и удаляет контейнеры postgres, minio, event-app. Удаляет image event-app и gradle артефакт.  
start - собирает gradle-ом event-app. Собирает image event-app. Запускает контейнеры event-app, postgres, minio. 
Есть опция -d для запуска в фоновом режиме.  
launch-db - запуск контейнера postgres для подключения при дебаге. Включена опция автоудаления.  
launch-db - запуск контейнера minio для подключения при дебаге. Включена опция автоудаления.  
<br>
<br>
При запуске в одной директории с docker-compose файлом должен быть .env файл, в котором задаются переменные окружения для запуска:  
EMAIL_USERNAME - имя почты, с которой будут приходить уведомления.  
EMAIL_PASSWORD - пароль от почты, с которой будут приходить  уведомления.  
MINIO_URL - url, по которому можно обращаться к minio.  
MINIO_PORT - порт minio.  
MINIO_ACCESS_KEY - логин от консоли minio.  
MINIO_SECRET_KEY - пароль от консоли minio.  
TASK_URL - url, по которому можно обратиться к задаче, без id задачи.  
IP - ip сервера, на котором запущено приложение.  
<br>
<div style="text-align:left"><img src=../images/env-docker-compose.png/></div>
<br>

В случае отсутствия будут применены следующие значения по умолчанию:  
EMAIL_USERNAME=itmo.event-app@mail.ru  
EMAIL_PASSWORD=TrXn37gvRKwAMfRmtdRd  
MINIO_URL=http://minio:9000  
MINIO_PORT=9000  
MINIO_ACCESS_KEY=minio-admin  
MINIO_SECRET_KEY=minio-admin  
TASK_URL=http://localhost:8080/task/  
IP=158.160.165.10  