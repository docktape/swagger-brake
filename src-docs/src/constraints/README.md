# Constraints
Constraints are the rules in place one-level lower than the [Rules](../rules/README.md). They focus on the attribute 
definitions, specifically their data-types and the imposed constraints. A simple example is when a `string` attribute
has the following definition with `maxLength`:

```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "string",
    "maxLength": 5
}]
``` 

Did not find the constraint you're looking for? It might not be implemented, feel free to file a ticket on 
[GitHub](https://github.com/docktape/swagger-brake/issues).

## Array constraints
### maxItems constraint
This constraint checks whether the `maxItems` constraint on an array typed attribute has been changed in a backward-incomptabile
way:
* if the old API did not have a `maxItems` constraint at all but the new API has it
* if the old API did have a `maxItems` constraint and the new API has it too but it's a smaller bound

swagger.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "array",
    "items": {
      "type": "string"
    },
    "maxItems": 10
}]
```

swagger2.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "array",
    "items": {
      "type": "string"
    },
    "maxItems": 8
}]
```

### minItems constraint
This constraint checks whether the `minItems` constraint on an array typed attribute has been changed in a backward-incomptabile
way:
* if the old API did not have a `minItems` constraint at all but the new API has it
* if the old API did have a `minItems` constraint and the new API has it too but it's a higher bound

swagger.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "array",
    "items": {
      "type": "string"
    },
    "minItems": 4
}]
```

swagger2.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "array",
    "items": {
      "type": "string"
    },
    "minItems": 8
}]
```

### uniqueItems constraint
This constraint checks for the array's `uniqueItems` attribute and will report a breaking change in case the attribute
has changed from `false`/non-present to `true`.

swagger.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "array",
    "items": {
      "type": "string"
    },
    "uniqueItems": false
}]
```

swagger2.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "array",
    "items": {
      "type": "string"
    },
    "uniqueItems": true
}]
```

## Number constraints
### maximum constraint
This constraint checks whether the `maximum` constraint on a number typed attribute has been changed in a backward-incomptabile
way:
* if the old API did not have a `maximum` constraint at all but the new API has it
* if the old API did have a `maximum` constraint and the new API has it too but it's a smaller bound

swagger.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "integer",
    "maximum": 10
}]
```

swagger2.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "integer",
    "maximum": 8
}]
```

### minimum constraint
This constraint checks whether the `minimum` constraint on a number typed attribute has been changed in a backward-incomptabile
way:
* if the old API did not have a `minimum` constraint at all but the new API has it
* if the old API did have a `minimum` constraint and the new API has it too but it's a higher bound

swagger.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "integer",
    "minimum": 4
}]
```

swagger2.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "integer",
    "minimum": 8
}]
```

## String constraints
### maxLength constraint
This constraint checks whether the `maxLength` constraint on a number typed attribute has been changed in a backward-incomptabile
way:
* if the old API did not have a `maxLength` constraint at all but the new API has it
* if the old API did have a `maxLength` constraint and the new API has it too but it's a smaller bound

swagger.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "string",
    "maxLength": 10
}]
```

swagger2.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "string",
    "maxLength": 8
}]
```

### minLength constraint
This constraint checks whether the `minLength` constraint on a number typed attribute has been changed in a backward-incomptabile
way:
* if the old API did not have a `minLength` constraint at all but the new API has it
* if the old API did have a `minLength` constraint and the new API has it too but it's a higher bound

swagger.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "string",
    "minLength": 4
}]
```

swagger2.json
```json
"parameters": [{
    "name": "orderId",
    "in": "path",
    "description": "ID of pet that needs to be fetched",
    "required": true,
    "type": "string",
    "minLength": 8
}]
```