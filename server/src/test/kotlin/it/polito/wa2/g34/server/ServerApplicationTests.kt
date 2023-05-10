package it.polito.wa2.g34.server

import it.polito.wa2.g34.server.product.*
import it.polito.wa2.g34.server.profile.*
import it.polito.wa2.g34.server.sales.Sale
import it.polito.wa2.g34.server.sales.SaleRepository
import it.polito.wa2.g34.server.sales.toDTO
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.dto.toDTO
import it.polito.wa2.g34.server.ticketing.entity.Priority
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.entity.Ticket
import it.polito.wa2.g34.server.ticketing.repository.TicketRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime

@Testcontainers
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@Sql(scripts = ["file:src/main/resources/test_population.sql"])
class ServerApplicationTests {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")
            //.withInitScript("test_population.sql")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
            //registry.add("spring.jpa.defer-datasource-initialization" ){true}
        }
    }

    @LocalServerPort
    protected var port: Int = 0
    @Autowired
    lateinit var restTemplate: TestRestTemplate
    @Autowired
    lateinit var profileRepository: ProfileRepository
    @Autowired
    lateinit var productRepository: ProductRepository
    @Autowired
    lateinit var saleRepository: SaleRepository
    @Autowired
    lateinit var brandRepository: BrandRepository
    @Autowired
    lateinit var ticketRepository: TicketRepository
    


    /*@Test
    @DisplayName("PostTicket ID validation test")
    fun someTest() {
        val baseurl = "http://localhost:${port}"
        //profileRepository.save(Profile("abc@def.it","Gigi Finizio", 34))
        //val profile = profileRepository.findById("abc@def.it")
        val result = restTemplate.getForEntity<String>("/profiles/abc@def.it")
        println(result.body)
    }



    @Test
    @DisplayName("PostTicket priority validation test")
    fun postTicketPriorityValidation() {
        data class invalidIdTicket(
            var id: Long?,
            var priority: String,
            var state: String,
            var creator: ProfileDTO,
            var expert: ProfileDTO?,
            var product: ProductDTO,
            var sale_id: String
        )
        val profile = profileRepository.getReferenceById("mario@rossi.it")
        val expert = profileRepository.getReferenceById("francesco@gialli.it")
        val product = productRepository.getReferenceById("0194252708002")
        val result = restTemplate.postForEntity("/api/ticket/",invalidIdTicket(
            null,
            "randomString",
            "OPEN",
            profile.toDTO(),
            expert.toDTO(),
            product.toDTO(),
            "abc"
        )
        ,String::class.java)
        println(result.body)
        assertEquals(result.statusCode,HttpStatus.BAD_REQUEST)
    }

    @Test
    @DisplayName("PostTicket ID validation test")
    fun postTicketIDValidation() {
        data class invalidIdTicket(
            var id: String,
            var priority: String,
            var state: String,
            var creator: ProfileDTO,
            var expert: ProfileDTO?,
            var product: ProductDTO,
            var sale_id: String
        )
        val profile = profileRepository.getReferenceById("mario@rossi.it")
        val expert = profileRepository.getReferenceById("francesco@gialli.it")
        val product = productRepository.getReferenceById("0194252708002")
        val result = restTemplate.postForEntity("/api/ticket/",invalidIdTicket(
            "test",
            "2",
            "OPEN",
            profile.toDTO(),
            expert.toDTO(),
            product.toDTO(),
            "abc"
        )
            ,String::class.java)
        println(result.body)
        assertEquals(result.statusCode,HttpStatus.BAD_REQUEST)
    }

     */

    @Test
    @DisplayName("PostTicket correct behaviour 2")
    fun postTicketCorrectBehaviour2() {
        brandRepository.save(Brand(
            0,
            "Apple"
        ))
        val brand = brandRepository.findAll().first()
        productRepository.save(Product(
            "0194252708002",
            brand,
            "Apple iPhone 13",
            "Il tuo nuovo superpotere.Il nostro sistema a doppia fotocamera più evoluto di sempre.Migliora del 47% la qualità delle immagini riprese in notturna.",
            "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/refurb-iphone-13-pro-graphite-2023?wid=2000&hei=1897&fmt=jpeg&qlt=95&.v=1679072987081"

        ))
        val product = productRepository.findAll().first()
        profileRepository.save(Profile("customer@gmail.com", "Perfect Customer", 25, Role.CUSTOMER))
        val customerprofile = profileRepository.findAll().last()
        saleRepository.save(Sale(
            "",
            product,
            customerprofile,
            LocalDateTime.now(),
            LocalDateTime.now().plusYears(2)
        ))
        val sale = saleRepository.findAll().first()
        profileRepository.save(Profile("exper@gmail.com", "Perfect Expert", 25, Role.EXPERT))
        val expertprofile = profileRepository.findAll().last()
        val ticketToBeInserted = Ticket(
            null,
            priority = Priority.LOW,
            state = State.OPEN,
            creator = customerprofile,
            expert =expertprofile,
            product = product,
            sale = sale
        )

        val result = restTemplate.postForEntity("/api/ticket/", ticketToBeInserted.toDTO(),String::class.java)
        println(result.body)
        assertEquals(result.statusCode,HttpStatus.OK)
        cleanDB()
    }

    /*@Test
    @DisplayName("PostTicket correct behaviour")
    fun postTicketCorrectBehaviour() {
        data class validTicket(
            var id: Long?,
            var priority: String,
            var state: String,
            var creator: ProfileDTO,
            var expert: ProfileDTO?,
            var product: ProductDTO,
            var sale_id: String,
        )
        val profile = profileRepository.getReferenceById("mario@rossi.it")
        profileRepository.save(Profile("customer@gmail.com", "Perfect Customer", 25, Role.CUSTOMER))
        val customerprofile = profileRepository.findAll().last()
        profileRepository.save(Profile("exper@gmail.com", "Perfect Expert", 25, Role.EXPERT))
        val expertprofile = profileRepository.findAll().last()
        val expert = profileRepository.getReferenceById("francesco@gialli.it")
        val product = productRepository.getReferenceById("0194252708002")
        saleRepository.save((Sale("",product,profile, LocalDateTime.now(),LocalDateTime.now().plusYears(2))))
        val sale = saleRepository.findAll().first()
        val ticketToInsert = TicketDTO(
            0.toLong(),
            "LOW",
            "OPEN",
            customerprofile.toDTO(),
            expertprofile.toDTO(),
            product.toDTO(),
            sale.id!!
        )
        val result = restTemplate.postForEntity("/api/ticket/", ticketToInsert,String::class.java)
        println(result.body)
        assertEquals(result.statusCode,HttpStatus.BAD_REQUEST)
    }

     */

    fun cleanDB(){
        this.ticketRepository.deleteAll()
        this.saleRepository.deleteAll()
        this.profileRepository.deleteAll()
        this.productRepository.deleteAll()
        this.brandRepository.deleteAll()
    }

}
