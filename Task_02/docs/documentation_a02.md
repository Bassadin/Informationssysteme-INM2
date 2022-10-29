# Task 02 Informationssysteme (Piepmeyer) - Redis

Link to repository: https://github.com/Bassadin/Informationssysteme-INM2/tree/main/Task_02

## Subtasks

### Subtask 01

I'm using docker to start Redis.
Trying out the `redis-cli`:

```shell
127.0.0.1:6379> set DE Germany
OK
127.0.0.1:6379> get DE
"Germany"
127.0.0.1:6379>
```

### Subtask 02

I set my docker container's port upon creating it. Connecting with telnet:

```shell
get DE
$7
Germany
set FR France
+OK
get FR
$8
France
```

## Considerations

- Use RedisJSON? https://redis.io/docs/stack/json/