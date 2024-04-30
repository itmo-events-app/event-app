Инструкция по развертыванию находится в директории /scripts.
Для запуска нужны gradle версии 8, docker версии 22+, docker compose версии 3+

Для локального запуска нужно указать local профиль в VM options: -Dspring.profiles.active=local
<div style="text-align:left"><img src=images/img.png/></div>

Также заранее нужно создать контейнеры postgres и minio. Инструкция как это сделать есть в директории /scripts
