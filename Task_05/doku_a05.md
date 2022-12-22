# Dokumentation A05

## Import

`CALL apoc.load.json("file:///import/dblp.v12.new.json")`

### Neo4J Import script

<https://neo4j.com/labs/apoc/4.3/database-integration/mongo/>

```neo4j
// MongoDB import

call apoc.mongo.find('mongodb://mongo:27017/a04.articles') yield value
return value
```
