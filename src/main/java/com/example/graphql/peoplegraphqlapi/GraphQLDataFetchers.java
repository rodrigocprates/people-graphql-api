package com.example.graphql.peoplegraphqlapi;

import com.example.graphql.peoplegraphqlapi.model.Person;
import com.example.graphql.peoplegraphqlapi.repository.PersonRepository;
import com.google.common.collect.Lists;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GraphQLDataFetchers {

    @Autowired
    private PersonRepository repository;

    public DataFetcher getPeopleDataFetcher() {
        // lambda: return (DataFetcher<List<Person>>) environment -> Lists.newArrayList(repository.findAll());

        return new DataFetcher<List<Person>>() {
            @Override
            public List<Person> get(DataFetchingEnvironment environment) {
                return Lists.newArrayList(repository.findAll());
            }
        };
    }

    public DataFetcher getPersonByID() {

        return (DataFetcher<Person>) environment -> repository.findById(environment.getArgument("id")).orElse(null);
    }

    public DataFetcher createNewPerson() {
        return new DataFetcher() {
            @Override
            public Person get(DataFetchingEnvironment environment) {

                // The graphql specification dictates that input object arguments MUST
                // be maps.  You can convert them to POJOs inside the data fetcher if that
                // suits your code better
                Map<String, Object> personInputMap = environment.getArgument("person");

                Person person = new Person();
                person.setName(personInputMap.get("name").toString());
                person.setEmail(personInputMap.get("email").toString());

                // make a call to store (any) to mutate data
                return repository.save(person);
            }
        };
    }
}
