package au.com.example.microservice.payslip

import au.com.example.microservice.payslip.exception.UnsupportedFileTypeException
import au.com.example.microservice.payslip.model.*
import org.springframework.context.MessageSource
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

import java.time.Month

class PayslipControllerTest extends Specification {

    def PayslipController payslipController

    def PayslipService mockPayslipService

    def PayslipFileService mockPayslipFileService

    def MessageSource mockMessageSource;

    void setup() {
        mockPayslipService = Mock(PayslipServiceImpl)

        mockPayslipFileService = Mock(PayslipFileServiceImpl)

        mockMessageSource = Mock(ResourceBundleMessageSource)

        payslipController = new PayslipController(
                "payslipService": mockPayslipService,
                "payslipFileService": mockPayslipFileService,
                "messageSource": mockMessageSource)
    }

    def "should receive valid income summary and return payslip summary successfully"() {
        given:
        def incomeSummary = new IncomeSummary(new Employee("Test", "User"), Month.JUNE, new Income(50000, 9))

        // mock responses
        def payslipSummary = new PayslipSummary(new Employee("Test", "User"), Month.JUNE, new Salary(5000, 4100, 900, 420))

        when:
        def result = payslipController.payslipSummary(incomeSummary)

        then:
        result instanceof ResponseEntity
        result.getStatusCode() == HttpStatus.OK
        1 * mockPayslipService.calculate(incomeSummary) >> payslipSummary

    }

    def "should receive valid income summaries and return payslip summaries successfully"() {
        given:
        def incomeSummary = new IncomeSummary(new Employee("Test", "User"), Month.JUNE, new Income(50000, 9))

        def incomeSummaries = [] as ArrayList
        incomeSummaries.add(incomeSummary)

        def incomeSummariesWrapper = new IncomeSummariesWrapper()
        incomeSummariesWrapper.setIncomeSummaries(incomeSummaries)

        // mock responses
        def payslipSummary = new PayslipSummary(new Employee("Test", "User"), Month.JUNE, new Salary(5000, 4100, 900, 420))

        when:
        def result = payslipController.payslipSummaries(incomeSummariesWrapper)

        then:
        result instanceof ResponseEntity
        result.getStatusCode() == HttpStatus.OK
        1 * mockPayslipService.calculate(incomeSummary) >> payslipSummary

    }

    def "should receive valid multipart file and return payslip summaries successfully"() {
        given:
        def line = "Test,User,50000,9,JUNE\n"

        def multipartFile = new MockMultipartFile("salaries.csv", "salaries.csv", "text/csv", line.getBytes())

        // mock responses
        def incomeSummary = new IncomeSummary(new Employee("Test", "User"), Month.JUNE, new Income(50000, 9))

        def incomeSummaries = [] as ArrayList
        incomeSummaries.add(incomeSummary)

        def payslipSummary = new PayslipSummary(new Employee("Test", "User"), Month.JUNE, new Salary(5000, 4100, 900, 420))

        def payslipSummaries = [] as ArrayList
        payslipSummaries.add(payslipSummary)

        def resource = Mock(Resource)

        def isFileTypeSupported = true

        when:
        def result = payslipController.payslipSummariesBatch(multipartFile)

        then:
        result instanceof ResponseEntity
        result.getStatusCode() == HttpStatus.OK
        result.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE) == "text/csv"
        1 * mockPayslipFileService.isSupportedFileType(multipartFile.getContentType()) >> isFileTypeSupported
        1 * mockPayslipFileService.convert(multipartFile) >> incomeSummaries
        1 * mockPayslipService.calculate(incomeSummary) >> payslipSummary
        1 * mockPayslipFileService.convert(payslipSummaries) >> resource
    }

    def "should throw UnsupportedFileTypeException when unsupported file content type has been provided"() {
        given:
        def line = "Test,User,50000,9,JUNE\nFoo,Bar,20000,10,JULY"

        def multipartFile = new MockMultipartFile("salaries.csv", "salaries.csv", "application/json", line.getBytes())

        // mock responses
        def message = "Exception Message Thrown"

        def isFileTypeSupported = false

        when:
        payslipController.payslipSummariesBatch(multipartFile)

        then:
        thrown UnsupportedFileTypeException
        1 * mockPayslipFileService.isSupportedFileType(multipartFile.getContentType()) >> isFileTypeSupported
        1 * mockMessageSource.getDefaultMessage('error.filetype.unsupported') >> message
    }
}
