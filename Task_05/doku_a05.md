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
// MongoDB import articles

CALL apoc.mongo.find('mongodb://mongo:27017/a05.articles') YIELD value

CALL apoc.graph.fromDocument(
    value,
    {
        write: true,
        skipValidation: true,
        mappings: {
            `$`: 'Article{!id,title}',
            `$.authors`: 'Author{!id,name,org}'
        }
    }
) YIELD graph AS g1

return g1
```
