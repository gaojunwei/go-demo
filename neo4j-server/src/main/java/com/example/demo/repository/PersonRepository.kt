package com.example.demo.repository

import com.example.demo.model.Person
import org.springframework.data.neo4j.repository.Neo4jRepository

interface PersonRepository : Neo4jRepository<Person, Long>