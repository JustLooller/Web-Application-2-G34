package it.polito.wa2.g34.server
import it.polito.wa2.g34.server.product.*
import it.polito.wa2.g34.server.profile.*
import it.polito.wa2.g34.server.sales.Sale
import it.polito.wa2.g34.server.sales.SaleRepository
import it.polito.wa2.g34.server.sales.toDTO
import it.polito.wa2.g34.server.ticketing.dto.MessageDTO
import it.polito.wa2.g34.server.ticketing.dto.TicketDTO
import it.polito.wa2.g34.server.ticketing.dto.UpdateTicketStatusDTO
import it.polito.wa2.g34.server.ticketing.dto.toDTO
import it.polito.wa2.g34.server.ticketing.entity.Priority
import it.polito.wa2.g34.server.ticketing.entity.State
import it.polito.wa2.g34.server.ticketing.repository.MessageRepository
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
//@Sql(scripts = ["file:src/main/resources/test_population.sql"])
class ServerApplicationTestsMarco {
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
    lateinit var messageRepository: MessageRepository

    @Autowired
    lateinit var historyRepository: StateHistoryRepository

    @Test
    @DisplayName("Stop a Ticket correctly")
    fun putTicketStopCorrectBehaviour() {
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
        restTemplate.put("/api/ticket/${ticket.id}/stop",UpdateTicketStatusDTO(ticket.id!!,expertprofile.email,null), ticketToBeInserted,String::class.java)
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}",String::class.java)
        println(result.body)
        ticketToBeInserted.id = ticket.id
        ticketToBeInserted.state = State.OPEN.name
        ticketToBeInserted.priority = null
        ticketToBeInserted.expert_mail = null
        assertEquals(Json.encodeToString(ticketToBeInserted),result.body)
        cleanDB()
    }

    @Test
    @DisplayName("Stop Ticket must be called only from IN PROGRESS")
    fun putTicketStopWrongState1() {
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
        println(ticket)
        restTemplate.put("/api/ticket/${ticket.id}/stop",UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null), ticketToBeInserted,String::class.java)
        val result : ResponseEntity<String> = restTemplate.exchange(
            "/api/ticket/${ticket.id}/reopen",
            HttpMethod.PUT,
            HttpEntity(UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null)),
            ticketToBeInserted,
            String::class.java,
            arrayOf("")
        )
        println(result.body)
        assertEquals(HttpStatus.FORBIDDEN.value(), result.statusCode.value())
    }
    @Test
    @DisplayName("Close a Ticket correctly 1")
    fun putTicketCloseCorrectBehaviour() {
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
        restTemplate.put("/api/ticket/${ticket.id}/close",UpdateTicketStatusDTO(ticket.id!!,expertprofile.email,null), ticketToBeInserted,String::class.java)
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}",String::class.java)
        println(result.body)
        ticketToBeInserted.id = ticket.id
        ticketToBeInserted.state = State.CLOSED.name
        // sembra che fallisce perché riceve correttamente come creator il customer ma si aspetta manager, non capisco perchè ticketToBeInserted.creator_email = managerProfile.email
        assertEquals(Json.encodeToString(ticketToBeInserted),result.body)
        cleanDB()
    }

    @Test
    @DisplayName("Close a Ticket correctly2")
    fun putTicketCloseCorrectBehaviour2() {
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
        restTemplate.put("/api/ticket/${ticket.id}/close",UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null), ticketToBeInserted,String::class.java)
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}",String::class.java)
        println(result.body)
        ticketToBeInserted.id = ticket.id
        ticketToBeInserted.state = State.CLOSED.name
        assertEquals(Json.encodeToString(ticketToBeInserted),result.body)
        cleanDB()
    }


    @Test
    @DisplayName("Close Ticket require to set null priority and expert")
    fun putTicketClosePriorityExpertMustBeReset() {
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
        restTemplate.put("/api/ticket/${ticket.id}/close",UpdateTicketStatusDTO(ticket.id!!,expertprofile.email,null), ticketToBeInserted,String::class.java)
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}",String::class.java)
        println(result.body)
        ticketToBeInserted.id = ticket.id
        ticketToBeInserted.state = State.CLOSED.name
        ticketToBeInserted.priority = null
        ticketToBeInserted.expert_mail = null
        assertEquals(Json.encodeToString(ticketToBeInserted),result.body)
        cleanDB()
    }


    @Test
    @DisplayName("Reopen a Ticket correctly 1")
    fun putTicketReopenCorrectBehaviour() {
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
        restTemplate.put("/api/ticket/${ticket.id}/close",UpdateTicketStatusDTO(ticket.id!!,expertprofile.email,null), ticketToBeInserted,String::class.java)
        restTemplate.put("/api/ticket/${ticket.id}/reopen",UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null), ticketToBeInserted,String::class.java)
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}",String::class.java)
        println(result.body)
        ticketToBeInserted.id = ticket.id
        ticketToBeInserted.state = State.REOPENED.name
        ticketToBeInserted.priority = Priority.LOW.name
        ticketToBeInserted.expert_mail = expertprofile.email
        assertEquals(Json.encodeToString(ticketToBeInserted),result.body)
        cleanDB()
    }

    @Test
    @DisplayName("Reopen a Ticket correctly2")
    fun putTicketReopenCorrectBehaviour2() {
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
        restTemplate.put("/api/ticket/${ticket.id}/resolve",UpdateTicketStatusDTO(ticket.id!!,expertprofile.email,null), ticketToBeInserted,String::class.java)
        restTemplate.put("/api/ticket/${ticket.id}/reopen",UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null), ticketToBeInserted,String::class.java)
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}",String::class.java)
        println(result.body)
        ticketToBeInserted.id = ticket.id
        ticketToBeInserted.state = State.REOPENED.name
        ticketToBeInserted.priority = Priority.LOW.name
        ticketToBeInserted.expert_mail = expertprofile.email
        assertEquals(Json.encodeToString(ticketToBeInserted),result.body)
        cleanDB()
    }


    @Test
    @DisplayName("Reopen Ticket must be called only with CLOSED and RESOLVED status")
    fun putTicketReopenWrongState1() {
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
        restTemplate.put("/api/ticket/${ticket.id}/reopen",UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null), ticketToBeInserted,String::class.java)
        val result : ResponseEntity<String> = restTemplate.exchange(
            "/api/ticket/${ticket.id}/reopen",
            HttpMethod.PUT,
            HttpEntity(UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null)),
            ticketToBeInserted,
            String::class.java,
            arrayOf("")
        )
        println(result.statusCode.toString())
        assertEquals(HttpStatus.FORBIDDEN.value(), result.statusCode.value())
    }


    @Test
    @DisplayName("Reopen Ticket must be called only with CLOSED and RESOLVED status")
    fun putTicketReopenWrongState2() {
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
        restTemplate.put("/api/ticket/${ticket.id}/reopen",UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null), ticketToBeInserted,String::class.java)
        val result : ResponseEntity<String> = restTemplate.exchange(
            "/api/ticket/${ticket.id}/reopen",
            HttpMethod.PUT,
            HttpEntity(UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null)),
            ticketToBeInserted,
            String::class.java,
            arrayOf("")
        )
        println(result.statusCode.toString())
        assertEquals(HttpStatus.FORBIDDEN.value(), result.statusCode.value())
    }


    @Test
    @DisplayName("Resolve a Ticket correctly 1")
    fun putTicketResolveCorrectBehaviour() {
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
        restTemplate.put("/api/ticket/${ticket.id}/resolve",UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null), ticketToBeInserted,String::class.java)
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}",String::class.java)
        println(result.body)
        ticketToBeInserted.id = ticket.id
        ticketToBeInserted.state = State.RESOLVED.name
        assertEquals(Json.encodeToString(ticketToBeInserted),result.body)
        cleanDB()
    }

    @Test
    @DisplayName("Resolve a Ticket correctly2")
    fun putTicketResolveCorrectBehaviour2() {
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
        restTemplate.put("/api/ticket/${ticket.id}/resolve",UpdateTicketStatusDTO(ticket.id!!,expertprofile.email,null), ticketToBeInserted,String::class.java)
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}",String::class.java)
        println(result.body)
        ticketToBeInserted.id = ticket.id
        ticketToBeInserted.state = State.RESOLVED.name
        ticketToBeInserted.expert_mail = expertprofile.email
        ticketToBeInserted.priority = Priority.LOW.name
        assertEquals(Json.encodeToString(ticketToBeInserted),result.body)
        cleanDB()
    }


    @Test
    @DisplayName("Resolve Ticket must be called only with OPEN, REOPEN and IN PROGRESS status")
    fun putTicketResolveWrongState1() {
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
        restTemplate.put("/api/ticket/${ticket.id}/close",UpdateTicketStatusDTO(ticket.id!!,expertprofile.email,null), ticketToBeInserted,String::class.java)
        restTemplate.put("/api/ticket/${ticket.id}/reopen",UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null), ticketToBeInserted,String::class.java)
        val result : ResponseEntity<String> = restTemplate.exchange(
            "/api/ticket/${ticket.id}/reopen",
            HttpMethod.PUT,
            HttpEntity(UpdateTicketStatusDTO(ticket.id!!,customerprofile.email,null)),
            ticketToBeInserted,
            String::class.java,
            arrayOf("")
        )
        println(result.statusCode.toString())
        assertEquals(HttpStatus.FORBIDDEN.value(), result.statusCode.value())
    }

    @Test
    @DisplayName("PostMessage With No Attachment correct behaviour")
    fun postMessageWithNoAttachmentCorrectBehaviour() {
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
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().last()

        class testMessage(
            var id: Long,
            var text: String,
            var timestamp: LocalDateTime,
            var user_mail: String,
            var attachment: String?,
            var ticket_id: Long
        )
        val messageToBeInserted = testMessage(
            0,
            "Ciao questo è il primo messaggio del ticket",
            LocalDateTime.now(),
            customerprofile.email,
            null,
            ticket.id!!
        )


        val result = restTemplate.postForEntity("/api/ticket/${ticket.id}/message", messageToBeInserted,String::class.java)
        assertEquals(HttpStatus.OK,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("PostMessage With Attachment correct behaviour")
    fun postMessageWithAttachmentCorrectBehaviour() {
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
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().last()

        class testMessage(
            var id: Long,
            var text: String,
            var timestamp: LocalDateTime,
            var user_mail: String,
            var attachment: String?,
            var ticket_id: Long
        )
        val messageToBeInserted = testMessage(
            0,
            "Ciao questo è il primo messaggio del ticket",
            LocalDateTime.now(),
            customerprofile.email,
            "res/photo.png",
            ticket.id!!
        )


        val result = restTemplate.postForEntity("/api/ticket/${ticket.id}/message", messageToBeInserted,String::class.java)
        assertEquals(HttpStatus.OK,result.statusCode)
        cleanDB()
    }

    @Test
    @DisplayName("Message must be created from valid account")
    fun postMessageWithInvalidAccount() {
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
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().last()

        customerprofile.email = "INVALID"
        class testMessage(
            var id: Long,
            var text: String,
            var timestamp: LocalDateTime,
            var user_mail: String,
            var attachment: String?,
            var ticket_id: Long
        )
        val messageToBeInserted = testMessage(
            0,
            "Ciao questo è il primo messaggio del ticket",
            LocalDateTime.now(),
            customerprofile.email,
            "res/photo.png",
            ticket.id!!
        )


        val result = restTemplate.postForEntity("/api/ticket/${ticket.id}/message", messageToBeInserted,String::class.java)
        println(result.body)
        assertEquals(HttpStatus.BAD_REQUEST,result.statusCode)
        cleanDB()
    }


    @Test
    @DisplayName("Message must be created with a valid ticketID")
    fun postMessageWithInvalidTicket() {
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
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().last()

        class testMessage(
            var id: Long,
            var text: String,
            var timestamp: LocalDateTime,
            var user_mail: String,
            var attachment: String?,
            var ticket_id: Long
        )
        val messageToBeInserted = testMessage(
            0,
            "Ciao questo è il primo messaggio del ticket",
            LocalDateTime.now(),
            customerprofile.email,
            "res/photo.png",
            200
        )


        val result = restTemplate.postForEntity("/api/ticket/${ticket.id}/message", messageToBeInserted,String::class.java)
        println(result.body)
        assertEquals(HttpStatus.BAD_REQUEST,result.statusCode)
        cleanDB()
    }

    /* DA VALUTARE
    @Test
    @DisplayName("Message must be created by the creator of a ticket or by the assigned expert")
    fun postMessageFromDifferentAccount() {
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
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().last()

        profileRepository.save(Profile("customer2@gmail.com", "Bad Customer", 25, Role.CUSTOMER))
        val customerprofile2 = profileRepository.findAll().last()
        class testMessage(
            var id: Long,
            var text: String,
            var timestamp: LocalDateTime,
            var user_mail: String,
            var attachment: String?,
            var ticket_id: Long
        )
        val messageToBeInserted = testMessage(
            0,
            "Ciao questo è il primo messaggio del ticket",
            LocalDateTime.now(),
            customerprofile2.email,
            "res/photo.png",
            ticket.id!!
        )


        val result = restTemplate.postForEntity("/api/ticket/${ticket.id}/message", messageToBeInserted,String::class.java)
        println(result.body)
        assertEquals(HttpStatus.BAD_REQUEST,result.statusCode)
        cleanDB()
    } */


    @Test
    @DisplayName("get list of messages")
    fun getMessagesWithAttachmentCorrectBehaviour() {
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
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().last()

        val messageToBeInserted = MessageDTO(
            0,
            "Ciao questo è il primo messaggio del ticket",
            LocalDateTime.now(),
            customerprofile.email,
            "res/photo.png",
            ticket.id!!
        )
        restTemplate.postForEntity("/api/ticket/${ticket.id}/message", messageToBeInserted,String::class.java)


        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}/messages", String::class.java)
        assertEquals(HttpStatus.OK,result.statusCode)

        cleanDB()
    }


    @Test
    @DisplayName("Get list of messages with wrong ticket id")
    fun getMessagesWithAWrongTicketID() {
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
            State.OPEN.name,
            customerprofile.email,
            null,
            product.ean,
            sale.id!!
        )

        restTemplate.postForEntity("/api/ticket/", ticketToBeInserted,String::class.java)
        val ticket = ticketRepository.findAll().last()

        val messageToBeInserted = MessageDTO(
            0,
            "Ciao questo è il primo messaggio del ticket",
            LocalDateTime.now(),
            customerprofile.email,
            "res/photo.png",
            ticket.id!!
        )
        restTemplate.postForEntity("/api/ticket/${ticket.id}/message", messageToBeInserted,String::class.java)

        ticket.id=100

        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}/messages", String::class.java)
        assertEquals(HttpStatus.NOT_FOUND,result.statusCode)
        cleanDB()
    }



    @Test
    @DisplayName("Get the history state of a Ticket")
    fun getTicketHistoryCorrectBehaviour() {
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
        restTemplate.put("/api/ticket/${ticket.id}/resolve",UpdateTicketStatusDTO(ticket.id!!,expertprofile.email,null), ticketToBeInserted,String::class.java)
        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}/history",String::class.java)
        println("SONO QUI")
        println(result.body)
        assertEquals(HttpStatus.OK,result.statusCode)
        cleanDB()
    }


    @Test
    @DisplayName("Get the history state with wrong TicketID")
    fun getTicketHistoryWrongTicketID() {
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
        restTemplate.put("/api/ticket/${ticket.id}/resolve",UpdateTicketStatusDTO(ticket.id!!,expertprofile.email,null), ticketToBeInserted,String::class.java)

        ticket.id = 200

        val result = restTemplate.getForEntity("/api/ticket/${ticket.id}/history",String::class.java)
        println(result.body)
        assertEquals(HttpStatus.NOT_FOUND,result.statusCode)
        cleanDB()
    }



    fun cleanDB(){
        this.messageRepository.deleteAll()
        this.historyRepository.deleteAll()
        this.ticketRepository.deleteAll()
        this.saleRepository.deleteAll()
        this.profileRepository.deleteAll()
        this.productRepository.deleteAll()
        this.brandRepository.deleteAll()
    }

}
