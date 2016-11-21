package au.com.example.microservice.framework

import au.com.example.microservice.App
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringApplicationConfiguration(App)
@WebIntegrationTest
@ActiveProfiles("local")
class FunctionalSpec extends Specification implements HttpTrait {

    @Value('${local.server.port:8080}')
    int port

    def setup() {
        setUri("http://localhost:${port}/")
    }
}
