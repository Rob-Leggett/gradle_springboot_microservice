package au.com.example.microservice.payslip.util

import au.com.example.microservice.payslip.exception.InvalidLineItemException
import org.springframework.context.MessageSource
import org.springframework.context.support.ResourceBundleMessageSource
import spock.lang.Specification

class IncomeSummaryMapperTest extends Specification {

    def IncomeSummaryMapper incomeSummaryMapper

    def MessageSource mockMessageSource

    void setup() {

        mockMessageSource = Mock(ResourceBundleMessageSource)

        incomeSummaryMapper = new IncomeSummaryMapper(
                "messageSource": mockMessageSource
        )
    }

    def "should map string and return as income summary successfully"() {
        given:
        def line = "Test,User,50000,9,JUNE"

        when:
        def result = incomeSummaryMapper.mapToIncomeSummary.apply(line);

        then:
        result.getEmployee().getFirstName() == "Test"
        result.getEmployee().getLastName() == "User"
        result.getMonth().name() == "JUNE"
        result.getIncome().getGross() == 50000
        result.getIncome().getSuperannuationPercentage() == 9
    }

    def "should throw InvalidLineItemException if first name not supplied successfully"() {
        given:
        def line = ",User,50000,9,JUNE"

        def message = "Exception Thrown"

        when:
        incomeSummaryMapper.mapToIncomeSummary.apply(line);

        then:
        thrown InvalidLineItemException
        1 * mockMessageSource.getDefaultMessage('error.line.item.invalid') >> message
    }

    def "should throw InvalidLineItemException if last name not supplied successfully"() {
        given:
        def line = "Test,,50000,9,JUNE"

        def message = "Exception Thrown"

        when:
        incomeSummaryMapper.mapToIncomeSummary.apply(line);

        then:
        thrown InvalidLineItemException
        1 * mockMessageSource.getDefaultMessage('error.line.item.invalid') >> message
    }

    def "should throw InvalidLineItemException if gross not supplied successfully"() {
        given:
        def line = "Test,User,,9,JUNE"

        def message = "Exception Thrown"

        when:
        incomeSummaryMapper.mapToIncomeSummary.apply(line);

        then:
        thrown InvalidLineItemException
        1 * mockMessageSource.getDefaultMessage('error.line.item.invalid') >> message
    }

    def "should throw InvalidLineItemException if superannuation percentage not supplied successfully"() {
        given:
        def line = "Test,User,50000,,JUNE"

        def message = "Exception Thrown"

        when:
        incomeSummaryMapper.mapToIncomeSummary.apply(line);

        then:
        thrown InvalidLineItemException
        1 * mockMessageSource.getDefaultMessage('error.line.item.invalid') >> message
    }

    def "should throw InvalidLineItemException if month not supplied successfully"() {
        given:
        def line = "Test,User,50000,9,"

        def message = "Exception Thrown"

        when:
        incomeSummaryMapper.mapToIncomeSummary.apply(line);

        then:
        thrown InvalidLineItemException
        1 * mockMessageSource.getDefaultMessage('error.line.item.invalid') >> message
    }

    def "should throw InvalidLineItemException if line does has less then 5 items comma separated successfully"() {
        given:
        def line = "Test,User,9,JUNE"

        def message = "Exception Thrown"

        when:
        incomeSummaryMapper.mapToIncomeSummary.apply(line);

        then:
        thrown InvalidLineItemException
        1 * mockMessageSource.getDefaultMessage('error.line.item.invalid') >> message
    }

    def "should throw InvalidLineItemException if line does has more then 5 items comma separated successfully"() {
        given:
        def line = "Test,User,50000,9,JUNE,EXTRA"

        def message = "Exception Thrown"

        when:
        incomeSummaryMapper.mapToIncomeSummary.apply(line);

        then:
        thrown InvalidLineItemException
        1 * mockMessageSource.getDefaultMessage('error.line.item.invalid') >> message
    }
}
