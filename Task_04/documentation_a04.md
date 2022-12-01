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

### Task 4f) and g) - Maximum amount of articles for an author and who is that author

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
      articles_count: {
        $count: {},
      },
      name: {
        $first: "$authors.name",
      },
      org: {
        $first: "$authors.org",
      },
    },
  },
  {
    $setWindowFields: {
      sortBy: { articles_count: -1 },
      output: {
        articles_amount_rank: {
          $rank: {},
        },
      },
    },
  },
  {
    $match: {
      articles_amount_rank: 1,
    },
  },
]
```
