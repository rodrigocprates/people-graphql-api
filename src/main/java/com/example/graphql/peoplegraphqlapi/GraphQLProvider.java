package com.example.graphql.peoplegraphqlapi;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    private static final String QUERY_TYPE = "Query";
    private static final String MUTATION_TYPE = "Mutation";

    private static final String OPERATION_NAME_QUERY_PEOPLE = "people";
    private static final String OPERATION_NAME_QUERY_PERSON_BY_ID = "personById";
    private static final String OPERATION_NAME_MUTATION_NEW_PERSON = "newPerson";

    private GraphQL graphQL;

    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {

        return RuntimeWiring.newRuntimeWiring()

                .type(newTypeWiring(QUERY_TYPE)
                        .dataFetcher(OPERATION_NAME_QUERY_PEOPLE, graphQLDataFetchers.getPeopleDataFetcher()))

                .type(newTypeWiring(QUERY_TYPE)
                        .dataFetcher(OPERATION_NAME_QUERY_PERSON_BY_ID, graphQLDataFetchers.getPersonByID()))

                // test alternatives to define a mutation
                .type(newTypeWiring(MUTATION_TYPE)
                        .dataFetcher(OPERATION_NAME_MUTATION_NEW_PERSON, graphQLDataFetchers.createNewPerson()))

                .build();
    }
}
