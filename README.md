## Scope description

# To run

- JDK 8
- Run PeopleGraphqlApiApplication

## H2 database

- URL: http://localhost:8080/h2-console
- JDBC Url client: jdbc:h2:mem:test
- User/pass: sa / <nopass>

## Graphiql *

- http://localhost:8080/graphiql

## Playground

- http://localhost:8080/playground

## Sample queries and mutations

### Queries

* get people
> query {
  people {
    id
    name
  }
}
>

- get person by id
> query {
    personById(id: 1) {
      name
    }
  }

### Mutation

- body
> mutation NewPerson($input: PersonInput!) {
  newPerson(person: $input) {
    name
  }
}

- query variable (input)
> {
"input": {
  "name": "Rodrigo Prates",
  "email": "thatsmyemail@gmail.com"
 }
}


