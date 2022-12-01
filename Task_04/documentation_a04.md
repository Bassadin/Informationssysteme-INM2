# Task 04 Informationssysteme (Piepmeyer) - Redis

Link to repository: https://github.com/Bassadin/Informationssysteme-INM2

## Subtaks

### Task 4a)

```mongodb
[
  {
    $match: {
      id: 16160,
    },
  },
  {
    $project: {
      title: 1,
    },
  },
]
```

### Task 4b)

```mongodb
[
  {
    $match: {
      id: 1091,
    },
  },
  {
    $project: {
      authors: 1,
    },
  },
]
```

### Task 4c)

```mongodb
[
  {
    $match: {
      "authors.id": 2579341158,
    },
  },
]
```

### Task 4d)

```mongodb
[
  {
    $match: {
      id: 1535888970,
    },
  },
  {
    $lookup: {
      from: "articles",
      localField: "references",
      foreignField: "id",
      as: "articleReferences",
    },
  },
  {
    $project: {
      "articleReferences.title": 1,
    },
  },
]

```

### Task 4e)

```mongodb
[
  {
    $unwind: {
      path: "$authors",
    },
  },
  {
    $group: {
      _id: "$authors.id",
    },
  },
  {
    $count: "authorsCount",
  },
]
```

### Task 4f)

```mongodb

```

### Task 4g)

```mongodb

```