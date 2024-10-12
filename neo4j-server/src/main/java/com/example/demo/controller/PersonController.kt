package com.example.demo.controller

import com.example.demo.model.Person
import com.example.demo.service.PersonService
import jakarta.annotation.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/persons")
class PersonController {
    @Resource
    private lateinit var personService: PersonService

    @PostMapping
    fun createPerson(@RequestBody person: Person?): ResponseEntity<Person> {
        return ResponseEntity.ok(personService.save(person!!))
    }

    @GetMapping
    fun allPersons(): ResponseEntity<List<Person>> {
        return ResponseEntity.ok(personService.findAll())
    }
}