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
// Delete everything

CALL apoc.periodic.iterate(
    "MATCH (n) RETURN n",
    "DETACH DELETE n",
    {
        batchSize:50000
    }
);
```

```neo4j
// Import articles from json into neo4j with batching in parallel

CREATE INDEX author_id_index IF NOT EXISTS
FOR (author:Author) ON (author.id);

// https://neo4j.com/docs/apoc/5/overview/apoc.periodic/apoc.periodic.iterate/#_config_parameters
// https://neo4j.com/labs/apoc/4.2/overview/apoc.load/apoc.load.jsonArray/
CALL apoc.periodic.iterate(
    "
        CALL apoc.load.jsonArray('file:///import/dblp.v12.new.json')
        YIELD value
        RETURN value;
    ",
    "
        CREATE (newArticle:Article {
            id: value.id,
            title: value.title
        })
        WITH newArticle, value
        UNWIND value.authors AS eachAuthorData
        // Might need some fallbacks for authors with different data based on id here
        MERGE (newAuthor:Author {id: eachAuthorData.id})
        ON CREATE
            SET
                newAuthor.name = eachAuthorData.name,
                newAuthor.org = CASE WHEN eachAuthorData.org IS NULL THEN '' ELSE eachAuthorData.org END
        CREATE (newAuthor)-[:IS_AUTHOR_OF]->(newArticle)
        WITH newAuthor, newArticle, value
        CALL apoc.log.info('--> Hello World!');
    ",
    {
        batchSize: 6000
    }
)
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
// TODO remove duplicates
```

```neo4j
// Show some articles from neo4j as graph

MATCH (art:Article)<--(auth:Author)
RETURN art, auth LIMIT 100;
```

## Subtasks Task 3

### Task a)

```neo4j
// Count article nodes

MATCH (article: Article)
RETURN count(article) as count
```

```neo4j
// Count author nodes

MATCH (author: Author)
RETURN count(author) as count
```

```neo4j
// Count author-article relationships

MATCH (author: Author)-[rel:IS_AUTHOR_OF]->(article: Article)
RETURN count(rel) as count
```

### Task b)

```neo4j
// Find the author Paul Erdős

MATCH (author:Author)
WHERE author.name = "Paul Erdős"
RETURN author
```

### Task c)

```neo4j
// Find all authors that have a degree of relationship to Paul Erdős of less than 5

// https://neo4j.com/docs/cypher-manual/current/syntax/patterns/#cypher-pattern-varlength
MATCH (author:Author)-[rel:IS_AUTHOR_OF*1..5]-(erdos:Author)
WHERE erdos.name = "Paul Erdős"
RETURN author, rel;
```

### Task d)

```neo4j
// Get the Authors with the most articles

MATCH (author:Author)-[rel:IS_AUTHOR_OF]->(article:Article)
RETURN author, count(rel) as relation_count
ORDER BY relation_count DESC
LIMIT 1;
```
