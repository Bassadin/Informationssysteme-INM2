# Dokumentation A05

## Import

`CALL apoc.load.json("file:///import/dblp.v12.new.json")`

### Neo4J Import script

<https://neo4j.com/labs/apoc/4.3/database-integration/mongo/>

```neo4j
// MongoDB basic example

CALL apoc.mongo.find('mongodb://mongo:27017/a05.articles') YIELD value
RETURN value.id AS article_ids
LIMIT 10;
```

```neo4j
// Show some articles from MongoDB

CALL apoc.mongo.find('mongodb://mongo:27017/a05.articles')
YIELD value
RETURN value.id, value.title, value.authors LIMIT 10;
```

```neo4j
// Import articles from Mongo into neo4j with batching in parallel

CALL apoc.periodic.iterate(
    "
        CALL apoc.mongo.find('mongodb://mongo:27017/a05.articles')
        YIELD value
        CALL apoc.log.info('--> Got MongoDB data!')
        RETURN value
        // TODO: REMOVE THIS LATER
        LIMIT 1000;
    ",
    "
        MERGE (newArticle:Article {
            id: value.id,
            title: value.title
        })
        WITH newArticle, value
        UNWIND value.authors AS eachAuthorData
        // Might need some fallbacks for authors with different data based on id here
        MERGE (newAuthor:Author {
            name: eachAuthorData.name,
            id: eachAuthorData.id,
            org: CASE WHEN eachAuthorData.org IS NULL THEN '' ELSE eachAuthorData.org END
        })
        MERGE (newAuthor)-[:IS_AUTHOR_OF]->(newArticle)
        WITH newAuthor, newArticle, value
        CALL apoc.log.info('--> Hello World!');
    ",
    {
        batchSize: 25000,
        parallel: true
    })
    YIELD
        total,
        committedOperations,
        failedOperations,
        batches,
        failedBatches,
        retries,
        errorMessages,
        batch,
        operations,
        wasTerminated;
```

```neo4j
// Show some articles from neo4j as graph

MATCH (art:Article)<--(auth:Author)
RETURN art, auth LIMIT 100;
```
