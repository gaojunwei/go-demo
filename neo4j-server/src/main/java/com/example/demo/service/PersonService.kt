package com.example.demo.service

import com.example.demo.model.Person
import com.example.demo.repository.PersonRepository
import jakarta.annotation.Resource
import org.springframework.stereotype.Service

@Service
class PersonService {
    @Resource
    private lateinit var personRepository: PersonRepository

    fun save(person: Person): Person {
        return personRepository.save(person)
    }

    fun findAll(): List<Person> {
        return personRepository.findAll()
    }
}