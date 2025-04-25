Yggdrasil - проект для удалённого управления сервисами, лежащими в закрытой сети/сети с серым ip адресом

Локальная сборка:
```
./gradlew bootJar
mv ./build/libs/yggdrasil-0.0.1-SNAPSHOT.jar {где удобнее будет, чтобы jar файл лежал}
```

Docker сборка
```
sudo docker build -t yggdrasil:v2 .
```


telegram.config (для запуска в Docker не обязательно т.к. может подтягиваеться из окружения):
```
telegram.bot.token={your bot token}
telegram.bot.username={your bot name}
```

Запуск
```
# sudo права не обязательны, если запуск идёт от пользователя, который без них может работать с systemctl
# На Windows обязательны права админинстратора
sudo java -jar ./yggdrasil.jar --server.port={порт, на котором будет работать YggdrasilBranch}
```

Пример Docker Compose
```
version: "3"

networks:
  yggdarsil:
    external: false

services:
  server:
    image: ghcr.io/killbane1232/yggdrasil:latest
    container_name: yggdrasil
    restart: always
    networks:
      - yggdarsil
    environment:
      TELEGRAMTOKEN: "{your token}"
      TELEGRAMUSERNAME: "{your bot name}"
    ports:
      - "8081:8080"
  branch:
    image: ghcr.io/killbane1232/yggdrasil_branch:latest
    container_name: yggdrasilBranch
    restart: always
    networks:
      - yggdarsil
    volumes:
      - ./config:/app/config
      - /var/run/docker.sock:/var/run/docker.sock
    environment:
      BRANCHNAME: "{your branch name}"
    ports:
      - "8081:8080"
```

