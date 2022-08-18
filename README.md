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
Produce message to `poc` topic.
And it will repeat 5 times.

```shell
curl -X POST \
  -H "Content-Type: application/vnd.kafka.json.v2+json" \
  -d '{
      "records": [{
        "value": {"foo":"bar"}
    }]
  }' \
  'localhost:8082/topics/poc'
```

### Third
Check console logs.
