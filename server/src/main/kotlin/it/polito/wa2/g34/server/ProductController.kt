package it.polito.wa2.g34.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController {
    @GetMapping("/test")
    fun getAll(): String {
        return "It Works!"
    }
}