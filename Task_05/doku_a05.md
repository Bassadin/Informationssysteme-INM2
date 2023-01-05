# Dokumentation A05

## Docker setup

I worked with `docker-compose`, using `docker-compose up -d` to start the Neo4j container in the background.

My `docker-compose.yml` file has declarations for mounted volumes, such as the `/data` mountpoint as well as the `/plugins` and `/import` mountpoints.

## Import

### MongoDB Integration (This didn't work)

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

### Import from JSON

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

### Show some Articles

```neo4j
// Show some articles from neo4j as graph

MATCH (art:Article)<--(auth:Author)
RETURN art, auth LIMIT 100;
```

### Delete everything (debugging, mostly just did `docker-compose down -v` to delete everything)

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

I'm not sure if these results are correct, but I verified some of the results with the website <https://mathscinet.ams.org/mathscinet/collaborationDistance.html>.

```neo4j
// Find all authors that have a degree of relationship to Paul Erdős of less than 5

// https://neo4j.com/docs/cypher-manual/current/syntax/patterns/#cypher-pattern-varlength
MATCH (author:Author)-[rel:IS_AUTHOR_OF*1..5]-(erdos:Author)
WHERE erdos.name = "Paul Erdős"
RETURN author, rel;
```

### Task d)

Hier war wieder nicht angegeben, ob es auch mehrere Autoren geben darf, die die meisten Artikel geschrieben haben. Ich habe es so gelöst, dass nur ein Autor zurückgegeben wird, der die meisten Artikel geschrieben hat.
Nicht ganz sicher, wie man das mit mehreren Autoren lösen könnte.

```neo4j
// Get the Authors with the most articles

MATCH (author:Author)-[rel:IS_AUTHOR_OF]->(article:Article)
RETURN author, count(rel) as relation_count
ORDER BY relation_count DESC
LIMIT 1;
```
