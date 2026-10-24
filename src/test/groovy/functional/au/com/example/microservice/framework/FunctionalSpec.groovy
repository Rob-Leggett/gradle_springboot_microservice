package au.com.example.microservice.framework

import au.com.example.microservice.App
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest(classes = App, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
class FunctionalSpec extends Specification implements HttpTrait {

    @Value('${local.server.port}')
    int port

    def setup() {
        setUri("http://localhost:${port}/")
    }
}
