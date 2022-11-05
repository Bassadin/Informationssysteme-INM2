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

## DB Structure

### "Tables"

- Articles: `arti_[articleID]`
  - Stored as JSON strings (`SET`)
- Authors: `author_[authorID]`
  - Stored as JSON strings (`SET`)
- Amount of authors (exact)
  - Uses `SADD` to create a set with author IDs to count
- Amount of authors (HyperLogLog)
  - Uses `PFADD` to create a HyperLogLog set with author IDs to count
- Article to Author relationship: `r_arti-author_[articleID]`
  - Stored as author IDs (`SET`)
- Author to Article relationship: `r_auth-article_[authorID]`
  - Stored as article IDs (`SET`)
- Article amounts by author: `r_auth-article_amounts`
  - Uses `ZINCRBY` in a Sorted Set
- Referencing Article to Referenced Article relationship: `r_referencing_arti-referenced_arti_[referencingArticleID]`
  - Uses `SADD`
- Referenced Article to Referencing Article relationship: `r_referenced_arti-referencing_arti_[referencedArticleID]
  - Uses `SADD`

## Considerations

- Use RedisJSON? https://redis.io/docs/stack/json/