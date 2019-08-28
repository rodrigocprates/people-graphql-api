package com.example.graphql.peoplegraphqlapi.repository;

import com.example.graphql.peoplegraphqlapi.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
}
