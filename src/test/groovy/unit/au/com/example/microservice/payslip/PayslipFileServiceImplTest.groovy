package au.com.example.microservice.payslip

import au.com.example.microservice.payslip.model.Employee
import au.com.example.microservice.payslip.model.PayslipSummary
import au.com.example.microservice.payslip.model.Salary
import au.com.example.microservice.payslip.util.IncomeSummaryMapper
import au.com.example.microservice.payslip.util.PayslipSummaryMapper
import org.springframework.core.io.Resource
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

import java.time.Month

class PayslipFileServiceImplTest extends Specification {

    def PayslipFileService payslipFileService

    def IncomeSummaryMapper incomeSummaryMapper

    def PayslipSummaryMapper payslipSummaryMapper

    void setup() {

        incomeSummaryMapper = new IncomeSummaryMapper()

        payslipSummaryMapper = new PayslipSummaryMapper()

        payslipFileService = new PayslipFileServiceImpl(
                "incomeSummaryMapper": incomeSummaryMapper,
                "payslipSummaryMapper": payslipSummaryMapper)
    }

    def "should convert multipart file to a list of income summaries successfully"() {
        given:
        def line = "Test,User,50000,9,JUNE\nFoo,Bar,20000,10,JULY"

        def multipartFile = new MockMultipartFile("salaries.csv", line.getBytes())

        when:
        def result = payslipFileService.convert(multipartFile)

        then:
        result instanceof ArrayList
        result.size() == 2
    }

    def "should support file type text/csv successfully"() {
        given:
        def line = "Test,User,50000,9,JUNE\nFoo,Bar,20000,10,JULY"

        def multipartFile = new MockMultipartFile("salaries.csv", "salaries.csv", "text/csv", line.getBytes())

        when:
        def result = payslipFileService.isSupportedFileType(multipartFile.getContentType())

        then:
        result
    }

    def "should support file type application/csv successfully"() {
        given:
        def line = "Test,User,50000,9,JUNE\nFoo,Bar,20000,10,JULY"

        def multipartFile = new MockMultipartFile("salaries.csv", "salaries.csv", "application/csv", line.getBytes())

        when:
        def result = payslipFileService.isSupportedFileType(multipartFile.getContentType())

        then:
        result
    }

    def "should not support file type that is not text/csv or application/csv successfully"() {
        given:
        def line = "Test,User,50000,9,JUNE\nFoo,Bar,20000,10,JULY"

        def multipartFile = new MockMultipartFile("salaries.csv", "salaries.csv", "application/json", line.getBytes())

        when:
        def result = payslipFileService.isSupportedFileType(multipartFile.getContentType())

        then:
        !result
    }

    def "should convert a list of payslip summaries a resource successfully"() {
        given:
        def payslipSummaries = [] as ArrayList
        payslipSummaries.add(new PayslipSummary(
                new Employee("Test", "User"),
                Month.JUNE,
                new Salary(5000, 4000, 900, 400)))

        when:
        def result = payslipFileService.convert(payslipSummaries)

        then:
        result instanceof Resource
    }
}