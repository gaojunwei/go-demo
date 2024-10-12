package com.example.demo.model

import org.springframework.data.annotation.Id
import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Property

@Node("Person")
class Person {
    // Getters and Setters
    @Id
    @GeneratedValue
    var id: Long? = null

    @Property
    var name: String? = null

    @Property
    var role: String? = null

    constructor(id: Long, name: String, role: String) {
        this.id = id
        this.name = name
        this.role = role
    }
}