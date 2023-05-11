package it.polito.wa2.g34.server

import it.polito.wa2.g34.server.product.*
import it.polito.wa2.g34.server.profile.*
import it.polito.wa2.g34.server.sales.Sale
import it.polito.wa2.g34.server.sales.SaleRepository
import it.polito.wa2.g34.server.sales.toDTO
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.dto.UpdateTicketStatusDTO
import it.polito.wa2.g34.server.ticketing.dto.toDTO
import it.polito.wa2.g34.server.ticketing.entity.Priority
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.repository.StateHistoryRepository
import it.polito.wa2.g34.server.ticketing.repository.TicketRepository
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime

@Testcontainers
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    @Autowired
    lateinit var stateHistoryRepository: StateHistoryRepository

    @Test
    @DisplayName("Ticket must be created only in open status")
    fun ticketMustBeCreatedOnlyInOpenStatus() {
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
        class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.CLOSED.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )


        val result = restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        println(result.body)
        assertEquals(HttpStatus.BAD_REQUEST,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("Ticket creator must be a customer")
    fun ticketCreatorMustBeACustomer() {
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
        profileRepository.save(Profile("customer@gmail.com", "Perfect Customer", 25, Role.EXPERT))
        val customerprofile = profileRepository.findAll().last()
        saleRepository.save(Sale(
            "",
            product,
            customerprofile,
            LocalDateTime.now(),
            LocalDateTime.now().plusYears(2)
        ))
        val sale = saleRepository.findAll().first()
        class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )


        val result = restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        println(result.body)
        assertEquals(HttpStatus.BAD_REQUEST,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("Ticket expert must be null in open phase")
    fun ticketExpertMustBeNullInOpenPhase() {
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
        profileRepository.save(Profile("expert@gmail.com", "Perfect Expert", 25, Role.EXPERT))
        val expertprofile = profileRepository.findAll().last()
        class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.OPEN.name,
            customerprofile.email,
            expertprofile.email,
            product.ean,
            sale.id!!
        )


        val result = restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        println(result.body)
        assertEquals(HttpStatus.BAD_REQUEST,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("Ticket priority must be null in open phase")
    fun ticketPriorityMustBeNullInOpenPhase() {
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
        class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        val ticketToBeInserted = testTicket(
            null,
            Priority.LOW.name,
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )


        val result = restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        println(result.body)
        assertEquals(HttpStatus.BAD_REQUEST,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("Ticket product must exists")
    fun ticketProductMustExists() {
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
        class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        product.ean = (1..13).map { 'z' }.joinToString("")
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )


        val result = restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        println(result.body)
        assertEquals(HttpStatus.BAD_REQUEST,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("Ticket sale must exists")
    fun ticketSaleMustExists() {
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
        class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        sale.id = "z"
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )


        val result = restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        println(result.body)
        assertEquals(HttpStatus.BAD_REQUEST,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("PostTicket correct behaviour")
    fun postTicketCorrectBehaviour() {
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
        class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        val result = restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        println(result.body)
        assertEquals(HttpStatus.OK,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("Ticket creator must have a valid Email")
    fun ticketCreatorMustHaveAValidEmail() {
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
        class correctTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator: ProfileDTO,
            var expert: ProfileDTO?,
            var product: ProductDTO,
            var sale_id: String,
        )
        class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        customerprofile.email = "abc"
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        val result = restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        println(result.body)
        assertEquals(HttpStatus.BAD_REQUEST,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("getTicket must have a Long parameter")
    fun getTicketMustHaveALongParameter() {
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
        class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().first()
        val result = restTemplate.getForEntity("/api/ticket/z",String::class.java)
        println(result.body)
        assertEquals(HttpStatus.BAD_REQUEST,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("getTicket correct behaviour")
    fun getTicketCorrectBehaviour() {
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
        @Serializable
        data class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().last()
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}",String::class.java)
        println(result.body)
        ticketToBeInserted.id = ticket.id
        assertEquals(Json.encodeToString(ticketToBeInserted),result.body)
        cleanDB()
    }

    @Test
    @DisplayName("getTicket for not existing ticket must return not found")
    fun getTicketforNotExistingTicketMustReturnNotFound() {
        val result = restTemplate.getForEntity("/api/ticket/${(0..Long.MAX_VALUE).random()}",String::class.java)
        println(result.body)
        assertEquals(HttpStatus.NOT_FOUND,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("Put Ticket start correct behaviour")
    fun putTicketStartCorrectBehaviour() {
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
        profileRepository.save(Profile("manager@gmail.com", "Perfect Manager", 25, Role.MANAGER))
        val managerProfile = profileRepository.findAll().last()
        @Serializable
        data class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().last()
        restTemplate.put("/api/ticket/${ticket.id}/start/${expertprofile.email}?priority=${Priority.HIGH.name}",UpdateTicketStatusDTO(ticket.id!!,managerProfile.email,null), ticketToBeInserted, String::class.java)
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}",String::class.java)
        println(result.body)
        ticketToBeInserted.id = ticket.id
        ticketToBeInserted.state = State.IN_PROGRESS.name
        ticketToBeInserted.expert_mail = expertprofile.email
        ticketToBeInserted.priority = Priority.HIGH.name
        assertEquals(Json.encodeToString(ticketToBeInserted),result.body)
        cleanDB()
    }

    @Test
    @DisplayName("Put Ticket priority must not be null after start")
    fun PutTicketPriorityMustNotBeNullAfterStart() {
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
        profileRepository.save(Profile("manager@gmail.com", "Perfect Manager", 25, Role.MANAGER))
        val managerProfile = profileRepository.findAll().last()
        @Serializable
        data class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().last()
        restTemplate.put("/api/ticket/${ticket.id}/start/${expertprofile.email}",UpdateTicketStatusDTO(ticket.id!!,managerProfile.email,null), ticketToBeInserted,String::class.java)
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}",String::class.java)
        println(result.body)
        ticketToBeInserted.id = ticket.id
        ticketToBeInserted.state = State.IN_PROGRESS.name
        ticketToBeInserted.expert_mail = expertprofile.email
        assertNotNull(Json.decodeFromString<TicketDTO>(result.body!!).priority)
        cleanDB()
    }

    @Test
    @DisplayName("Put Ticket must be called only with OPEN and REOPEN status")
    fun PutTicketMustBeCalledOnlyWithOPENAndREOPENStatus() {
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
        profileRepository.save(Profile("manager@gmail.com", "Perfect Manager", 25, Role.MANAGER))
        val managerProfile = profileRepository.findAll().last()
        @Serializable
        data class testTicket(
            var id: Long? = null,
            var priority: String?,
            var state: String,
            var creator_email: String,
            var expert_mail: String?,
            var product_ean: String,
            var sale_id: String,
        )
        val ticketToBeInserted = testTicket(
            null,
            null,
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().last()
        restTemplate.put("/api/ticket/${ticket.id}/start/${expertprofile.email}",UpdateTicketStatusDTO(ticket.id!!,managerProfile.email,null), ticketToBeInserted,String::class.java)

        val result : ResponseEntity<String> = restTemplate.exchange(
            "/api/ticket/${ticket.id}/start/${expertprofile.email}",
            HttpMethod.PUT,
            HttpEntity(UpdateTicketStatusDTO(ticket.id!!,managerProfile.email,null)),
            ticketToBeInserted,
            String::class.java,
            arrayOf("")
        )
        println(result.statusCode.toString())
        assertEquals(HttpStatus.FORBIDDEN.value(), result.statusCode.value())
        cleanDB()
    }


    fun cleanDB(){
        this.stateHistoryRepository.deleteAll()
        this.ticketRepository.deleteAll()
        this.saleRepository.deleteAll()
        this.profileRepository.deleteAll()
        this.productRepository.deleteAll()
        this.brandRepository.deleteAll()
    }

}
