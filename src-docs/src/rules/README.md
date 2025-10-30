# Rules
This page is going to list and describe which rules are implemented within swagger-brake, and will be considered
as a breaking change in an API.

Did not find the rule you're looking for? It might not be implemented, feel free to file a ticket on 
[GitHub](https://github.com/docktape/swagger-brake/issues).

## R001 - StandardApiToBetaApiRule
The rule addresses the case when a normal - non-beta API - is marked as beta in the new API, meaning that an exposed
API has been switched into beta mode to introduce potential breaking changes.

swagger.yaml:
```yaml
paths:
  /pet:
    post:
      summary: "Add a new pet to the store"
      operationId: "addPet"
```

swagger2.yaml:
```yaml
paths:
  /pet:
    post:
      summary: "Add a new pet to the store"
      operationId: "addPet"
      x-beta-api: true
```
## R002 - PathDeletedRule
This rule captures use-case when a complete API has been deleted from the new descriptor even though
it was present in the original API descriptor.

In case the API that's being deleted is marked as `deprecated`, it won't be counted as a breaking change.

swagger.yaml:
```yaml
paths:
  /pet/findByStatus:
    get:
      summary: "Finds Pets by status"
      operationId: "findPetsByStatus"
      produces:
      - "application/json"
      responses:
        200:
          description: "successful operation"
  /pet/findByTags:
    get:
      summary: "Finds Pets by tags"
      operationId: "findPetsByTags"
      produces:
        - "application/json"
      responses:
        200:
          description: "successful operation"
```

swagger2.yaml:
```yaml
paths:
  /pet/findByTags:
    get:
      summary: "Finds Pets by tags"
      operationId: "findPetsByTags"
      produces:
        - "application/json"
      responses:
        200:
          description: "successful operation"
```

## R003 - RequestMediaTypeDeletedRule
The rule is responsible for detecting the case when any of the existing path media types is deleted. For example if the
API supports `application/json` and `application/xml` for the consumes attribute, and in the new API, any of those 2 is
removed, it will be marked as a breaking change.

Also, in case the new API extends the allowed media types by using the `*` symbol, that's considered as a backward-compatible 
change and will not trigger a break. 

swagger.yaml
```yaml
paths:
  /pet:
    post:
      summary: "Add a new pet to the store"
      operationId: "addPet"
      consumes:
      - "application/json"
      - "application/xml"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - in: "body"
        name: "body"
        description: "Pet object that needs to be added to the store"
        required: true
        schema:
          $ref: "#/definitions/Pet"
```

swagger2.yaml
```yaml
paths:
  /pet:
    post:
      summary: "Add a new pet to the store"
      operationId: "addPet"
      consumes:
      - "application/json"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - in: "body"
        name: "body"
        description: "Pet object that needs to be added to the store"
        required: true
        schema:
          $ref: "#/definitions/Pet"
```

## R004 - RequestParameterDeletedRule
The rule handles the case when a request parameter is deleted from an API.

swagger.yaml
```yaml
paths:
 /pet/findByStatus:
    get:
      tags:
      - "pet"
      summary: "Finds Pets by status"
      description: "Multiple status values can be provided with comma separated strings"
      operationId: "findPetsByStatus"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - name: "status"
        in: "query"
        description: "Status values that need to be considered for filter"
        required: true
        type: "array"
        items:
          type: "string"
          enum:
          - "available"
          - "pending"
          - "sold"
          default: "available"
        collectionFormat: "multi"
```

swagger2.yaml
```yaml
paths:
  /pet/findByStatus:
    get:
      tags:
      - "pet"
      summary: "Finds Pets by status"
      description: "Multiple status values can be provided with comma separated strings"
      operationId: "findPetsByStatus"
      produces:
      - "application/xml"
      - "application/json"
```

## R005 - RequestParameterEnumValueDeletedRule
The rule captures the case when a request parameter is enum typed and one of the enum values are missing from the
new API, i.e. one enum constant has been deleted.

swagger.yaml
```yaml
paths:
 /pet/findByStatus:
    get:
      tags:
      - "pet"
      summary: "Finds Pets by status"
      description: "Multiple status values can be provided with comma separated strings"
      operationId: "findPetsByStatus"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - name: "status"
        in: "query"
        description: "Status values that need to be considered for filter"
        required: true
        type: "array"
        items:
          type: "string"
          enum:
          - "available"
          - "pending"
          - "sold"
          default: "available"
        collectionFormat: "multi"
```

swagger2.yaml
```yaml
paths:
 /pet/findByStatus:
    get:
      tags:
      - "pet"
      summary: "Finds Pets by status"
      description: "Multiple status values can be provided with comma separated strings"
      operationId: "findPetsByStatus"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - name: "status"
        in: "query"
        description: "Status values that need to be considered for filter"
        required: true
        type: "array"
        items:
          type: "string"
          enum:
          - "available"
          - "sold"
          default: "available"
        collectionFormat: "multi"
```

## R006 - RequestParameterInTypeChangedRule
In case a request parameter's "in" type changes, it's considered as a breaking change. An example is when you are 
passing a parameter via a path variable and in the new API it's a query parameter.

swagger.yaml
```yaml
paths:
 /pet/findByStatus:
    get:
      tags:
      - "pet"
      summary: "Finds Pets by status"
      description: "Multiple status values can be provided with comma separated strings"
      operationId: "findPetsByStatus"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - name: "status"
        in: "query"
        description: "Status values that need to be considered for filter"
        required: true
        type: "array"
        items:
          type: "string"
          enum:
          - "available"
          - "pending"
          - "sold"
          default: "available"
        collectionFormat: "multi"
```

swagger2.yaml
```yaml
paths:
 /pet/findByStatus:
    get:
      tags:
      - "pet"
      summary: "Finds Pets by status"
      description: "Multiple status values can be provided with comma separated strings"
      operationId: "findPetsByStatus"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - name: "status"
        in: "header"
        description: "Status values that need to be considered for filter"
        required: true
        type: "array"
        items:
          type: "string"
          enum:
          - "available"
          - "sold"
          default: "available"
        collectionFormat: "multi"
```

## R007 - RequestParameterRequiredRule
This rule is handling the use-cases with the `required` attribute. When a newly introduced parameter is marked as required,
it's considered as a breaking change. Also, if an existing parameter is marked as required, same thing, it's a breaking change.

swagger.yaml
```yaml
paths:
  /pet:
    post:
      tags:
      - "pet"
      summary: "Add a new pet to the store"
      description: ""
      operationId: "addPet"
      consumes:
      - "application/json"
      - "application/xml"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - in: "body"
        name: "body"
        description: "Pet object that needs to be added to the store"
        required: true
        schema:
          $ref: "#/definitions/Pet"
```

swagger2.yaml
```yaml
paths:
  /pet:
    post:
      tags:
      - "pet"
      summary: "Add a new pet to the store"
      description: ""
      operationId: "addPet"
      consumes:
      - "application/json"
      - "application/xml"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - in: "body"
        name: "body"
        description: "Pet object that needs to be added to the store"
        required: true
        schema:
          $ref: "#/definitions/Pet"
      - in: "query"
        name: "test"
        description: "Test description"
        required: true
        type: "string"
```

## R008 - RequestParameterTypeChangedRule
This rule is about the case when a request parameter's type has changed, i.e. it was originally an `array`, and in the
new API, it's a `string`.

swagger.yaml
```yaml
paths:
  /pet/findByTags:
    get:
      tags:
      - "pet"
       summary: "Finds Pets by tags"
      description: "Muliple tags can be provided with comma separated strings. Use\
        \ tag1, tag2, tag3 for testing."
      operationId: "findPetsByTags"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - name: "tags"
        in: "query"
        description: "Tags to filter by"
        required: true
        type: "array"
        items:
          type: "string"
        collectionFormat: "multi"
```

swagger2.yaml
```yaml
paths:
  /pet/findByTags:
    get:
      tags:
      - "pet"
       summary: "Finds Pets by tags"
      description: "Muliple tags can be provided with comma separated strings. Use\
        \ tag1, tag2, tag3 for testing."
      operationId: "findPetsByTags"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - name: "tags"
        in: "query"
        description: "Tags to filter by"
        required: true
        type: "string"
```

## R009 - RequestTypeAttributeRemovedRule
The rule detects when a schema attribute has been removed from the new API and the schema is being used as the request
body.

swagger.yaml (OpenAPI3)
```yaml
components:
  schemas:
    Dog:
      type: object
      properties:
        id:
          type: integer
        breed:
          type: string
          enum: [Dingo, Husky, Retriever, Shepherd]
      required:
        - id
```

swagger2.yaml (OpenAPI3)
```yaml
components:
  schemas:
    Dog:
      type: object
      properties:
        breed:
          type: string
          enum: [Dingo, Husky, Retriever, Shepherd]
```

## R010 - RequestTypeChangedRule
This rule handles the case when a property type within a schema definition changes and the schema is used as a request
body.

swagger.yaml (OpenAPI3)
```yaml
components:
  schemas:
    Dog:
      type: object
      properties:
        id:
          type: string
```

swagger2.yaml (OpenAPI3)
```yaml
components:
  schemas:
    Dog:
      type: object
      properties:
        id:
          type: integer
```

## R011 - RequestTypeEnumValueDeletedRule
The rule detects a removed enum value within a schema definition that is used as a request body.

swagger.yaml
```yaml
definitions:
  Order:
    type: "object"
    properties:
      id:
        type: "integer"
        format: "int64"
      petId:
        type: "integer"
        format: "int64"
      quantity:
        type: "integer"
        format: "int32"
      shipDate:
        type: "string"
        format: "date-time"
      status:
        type: "string"
        description: "Order Status"
        enum:
        - "placed"
        - "approved"
        - "delivered"
      complete:
        type: "boolean"
        default: false
```

swagger2.yaml
```yaml
definitions:
  Order:
    type: "object"
    properties:
      id:
        type: "integer"
        format: "int64"
      petId:
        type: "integer"
        format: "int64"
      quantity:
        type: "integer"
        format: "int32"
      shipDate:
        type: "string"
        format: "date-time"
      status:
        type: "string"
        description: "Order Status"
        enum:
        - "placed"
        - "delivered"
      complete:
        type: "boolean"
        default: false
```

## R012 - ResponseDeletedRule
The rule detects when a response definition has been deleted from the new API, i.e. when the API originally supported 
400, 404 and 405 responses while the new one only supports 400 and 405.

swagger.yaml
```yaml
paths:
  /pet:
    put:
      summary: "Update an existing pet"
      operationId: "updatePet"
      consumes:
      - "application/json"
      produces:
      - "application/json"
      parameters:
      - in: "body"
        name: "body"
        description: "Pet object that needs to be added to the store"
        required: true
        schema:
          $ref: "#/definitions/Pet"
      responses:
        400:
          description: "Invalid ID supplied"
        404:
          description: "Pet not found"
        405:
          description: "Validation exception"
```

swagger2.yaml
```yaml
paths:
  /pet:
    put:
      summary: "Update an existing pet"
      operationId: "updatePet"
      consumes:
      - "application/json"
      produces:
      - "application/json"
      parameters:
      - in: "body"
        name: "body"
        description: "Pet object that needs to be added to the store"
        required: true
        schema:
          $ref: "#/definitions/Pet"
      responses:
        400:
          description: "Invalid ID supplied"
        405:
          description: "Validation exception"
```

## R013 - ResponseMediaTypeDeletedRule
Similarly to [RequestMediaTypeDeletedRule](README.md#r003---requestmediatypedeletedrule), this rule detects the case when
a specific response MIME type was supported in the old API but was removed from the new one.

swagger.yaml
```yaml
paths:
  /pet/findByStatus:
    get:
      tags:
      - "pet"
      summary: "Finds Pets by status"
      description: "Multiple status values can be provided with comma separated strings"
      operationId: "findPetsByStatus"
      produces:
      - "application/xml"
      - "application/json"
      parameters:
      - name: "status"
        in: "query"
        description: "Status values that need to be considered for filter"
        required: true
        type: "array"
        items:
          type: "string"
          enum:
          - "available"
          - "pending"
          - "sold"
          default: "available"
        collectionFormat: "multi"
```

swagger2.yaml
```yaml
paths:
  /pet/findByStatus:
    get:
      tags:
      - "pet"
      summary: "Finds Pets by status"
      description: "Multiple status values can be provided with comma separated strings"
      operationId: "findPetsByStatus"
      produces:
      - "application/json"
      parameters:
      - name: "status"
        in: "query"
        description: "Status values that need to be considered for filter"
        required: true
        type: "array"
        items:
          type: "string"
          enum:
          - "available"
          - "pending"
          - "sold"
          default: "available"
        collectionFormat: "multi"
```

## R014 - ResponseTypeAttributeRemovedRule
This is the pair of [RequestTypeAttributeRemovedRule](README.md#r009---requesttypeattributeremovedrule).

The rule detects when a schema attribute has been removed from the new API and the schema is being used as the response
body.

swagger.yaml (OpenAPI3)
```yaml
components:
  schemas:
    Dog:
      type: object
      properties:
        id:
          type: integer
        breed:
          type: string
          enum: [Dingo, Husky, Retriever, Shepherd]
      required:
        - id
```

swagger2.yaml (OpenAPI3)
```yaml
components:
  schemas:
    Dog:
      type: object
      properties:
        breed:
          type: string
          enum: [Dingo, Husky, Retriever, Shepherd]
```

## R015 - ResponseTypeChangedRule
This is the pair of [RequestTypeChangedRule](README.md#r010---requesttypechangedrule).

This rule handles the case when a property type within a schema definition changes and the schema is used as a response
body.

swagger.yaml (OpenAPI3)
```yaml
components:
  schemas:
    Dog:
      type: object
      properties:
        id:
          type: string
```

swagger2.yaml (OpenAPI3)
```yaml
components:
  schemas:
    Dog:
      type: object
      properties:
        id:
          type: integer
```

## R016 - ResponseTypeEnumValueDeletedRule
This rule is the pair of [RequestTypeEnumValueDeletedRule](README.md#r011---requesttypeenumvaluedeletedrule).

The rule detects a removed enum value within a schema definition that is used as a response body.

swagger.yaml
```yaml
definitions:
  Order:
    type: "object"
    properties:
      id:
        type: "integer"
        format: "int64"
      petId:
        type: "integer"
        format: "int64"
      quantity:
        type: "integer"
        format: "int32"
      shipDate:
        type: "string"
        format: "date-time"
      status:
        type: "string"
        description: "Order Status"
        enum:
        - "placed"
        - "approved"
        - "delivered"
      complete:
        type: "boolean"
        default: false
```

swagger2.yaml
```yaml
definitions:
  Order:
    type: "object"
    properties:
      id:
        type: "integer"
        format: "int64"
      petId:
        type: "integer"
        format: "int64"
      quantity:
        type: "integer"
        format: "int32"
      shipDate:
        type: "string"
        format: "date-time"
      status:
        type: "string"
        description: "Order Status"
        enum:
        - "placed"
        - "delivered"
      complete:
        type: "boolean"
        default: false
```

## R017 - RequestParameterConstraintChangeRule
This is a wrapper rule that checks for any type of constraint violation. For specific constraints, check out the
[Constraints](../constraints/README.md) section.