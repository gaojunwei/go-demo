package com.example.demo.runner

import com.example.demo.model.Person
import com.example.demo.service.PersonService
import jakarta.annotation.Resource
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Component
class AppRunner : CommandLineRunner {
    @Resource
    private lateinit var personService: PersonService
    override fun run(vararg args: String) {
        personService.save(Person(1L, "孙悟空", "大圣"))
        personService.save(Person(2L, "唐僧", "金蝉子"))
    }
}