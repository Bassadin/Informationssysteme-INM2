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
// Show some articles in MongoDB

CALL apoc.mongo.find('mongodb://mongo:27017/a05.articles')
YIELD value
RETURN value.id, value.title, value.authors LIMIT 10;
```

```neo4j
// Import articles from Mongo into neo4j with bathcing in parallel

CALL apoc.periodic.iterate(
    "
        CALL apoc.mongo.find('mongodb://mongo:27017/a05.articles')
        YIELD value
    ",
    "
        MERGE (newArticle:Article {
            id: value.id,
            title: value.title
        })
        WITH newArticle, value
        UNWIND value.authors AS eachAuthorData
        // Might need some fallbacks for authors with different data based on id here
        MERGE (newAuthor:Person {
            name: eachAuthorData.name,
            id: eachAuthorData.id,
            org: CASE WHEN eachAuthorData.org IS NULL THEN '' ELSE eachAuthorData.org END
        })
        MERGE (newAuthor)-[:IS_AUTHOR_OF]->(newArticle);
    ",
    {
        batchSize:50000,
        parallel: true
    });
```

```neo4j
// Show some articles from neo4j graph

MATCH (a:Article)
RETURN a.id, a.title LIMIT 10;
```
