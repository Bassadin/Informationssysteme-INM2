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