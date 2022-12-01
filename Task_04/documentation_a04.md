# Task 04 Informationssysteme (Piepmeyer) - Redis

Link to repository: https://github.com/Bassadin/Informationssysteme-INM2

## Subtaks

### Before the others

Created an index for authors.id

### Task 4a) - Title of Article by ID

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

### Task 4b) - Authors of Article by ID

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

### Task 4c) - Articles of Author by ID

```mongodb
[
  {
    $match: {
      "authors.id": 2579341158,
    },
  },
]
```

### Task 4d) - Articles referenced by Article with ID

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

### Task 4e) - Amount of different Authors

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

### Task 4f) - Maximum amount of articles for an author

```mongodb

```

### Task 4g) - Who has the most articles?

```mongodb

```
