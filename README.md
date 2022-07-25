POC KListener
=============

How to run
----------

### First
Build sources and run containers.

```shell
./mvnw clean package && docker compose up --build
```

### Second
Open following URL.

http://localhost:8081/ui/docker-kafka-server/topic/poc/data?sort=Oldest&partition=All

### Third
Click [Produce to topic] button, then produce message to topic.
And it will repeat 5 times.

### Fourth
Check console logs.
