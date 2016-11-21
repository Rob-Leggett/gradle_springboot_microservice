package au.com.example.microservice.specs

import au.com.example.microservice.framework.FunctionalSpec
import org.springframework.mock.web.MockMultipartFile

class PayslipSpec extends FunctionalSpec {

    def "should return payslip summary given valid income summary is provided"() {
        given:
        def body = '{ "employee": { "firstName": "David", "lastName": "Rudd"}, "month": "MARCH", "income": { "gross": 60050, "superannuationPercentage": 9}}'

        when:
        payslipSummary(body)

        then:
        assert getStatus() == 200
        def data = getData()
        data.employee.firstName == "David"
        data.employee.lastName == "Rudd"
        data.month == "MARCH"
        data.salary.gross == 5004
        data.salary.net == 4082
        data.salary.superannuation == 450
        data.salary.tax == 922
    }

    def "should handle invalid income summary data being provided"() {
        given:
        def body = '{ "employee": { "firstName": "David"}, "month": "MARCH", "income": { "gross": 60050, "superannuationPercentage": 9}}'

        when:
        payslipSummary(body)

        then:
        assert getStatus() == 400
        def data = getData()
        data.errors[0].code == "NOT_NULL_CONSTRAINT_VIOLATION"
        data.errors[0].detail == "Last name cannot be null"
        data.errors[0].source["pointer"] == "employee.lastName"
    }

    def "should return payslip summaries given valid income summaries provided"() {
        given:
        def body = '{ "incomeSummaries": [{"employee": { "firstName": "David", "lastName": "Rudd"}, "month": "MARCH", "income": { "gross": 60050, "superannuationPercentage": 9}}, {"employee": { "firstName": "Ryan", "lastName": "Chen"}, "month": "MARCH", "income": { "gross": 120000, "superannuationPercentage": 10}}]}'

        when:
        payslipSummaries(body)

        then:
        assert getStatus() == 200
        def data = getData()
        data.payslipSummaries[0].employee.firstName == "David"
        data.payslipSummaries[0].employee.lastName == "Rudd"
        data.payslipSummaries[0].month == "MARCH"
        data.payslipSummaries[0].salary.gross == 5004
        data.payslipSummaries[0].salary.net == 4082
        data.payslipSummaries[0].salary.superannuation == 450
        data.payslipSummaries[0].salary.tax == 922
    }

    def "should handle invalid income summaries data being provided"() {
        given:
        def body = '{ "incomeSummaries": [{"employee": { "firstName": "David", "lastName": "Rudd"}, "month": "MARCH", "income": { "gross": 60050, "superannuationPercentage": 9}}, {"employee": { "firstName": "Ryan" }, "month": "MARCH", "income": { "gross": 120000, "superannuationPercentage": 10}}]}'

        when:
        payslipSummaries(body)

        then:
        assert getStatus() == 400
        def data = getData()
        data.errors[0].code == "NOT_NULL_CONSTRAINT_VIOLATION"
        data.errors[0].detail == "Last name cannot be null"
        data.errors[0].source["pointer"] == "incomeSummaries[1].employee.lastName"
    }

    def "should handle payslip summaries given valid employees details has been supplied"() {
        given:
        def line = "Test,User,50000,9,JUNE\n"

        def multipartFile = new MockMultipartFile("salaries.csv", "salaries.csv", "text/csv", line.getBytes())

        when:
        payslipSummariesBatch(multipartFile)

        then:
        assert getStatus() == 200
    }

    def "should handle invalid content type file being provided"() {
        given:
        def line = "Test,User,50000,9,JUNE\n"

        def multipartFile = new MockMultipartFile("salaries.csv", "salaries.csv", "application/json", line.getBytes())

        when:
        payslipSummariesBatch(multipartFile)

        then:
        assert getStatus() == 400
        def data = getData()
        data.errors[0].code == "VALIDATION_ERROR"
        data.errors[0].detail == "The file type you supplied is unsupported, please ensure the content-type is text/csv or application/csv"
    }

    def "should handle invalid file content being provided"() {
        given:
        def line = "Test,User,,9,JUNE\n"

        def multipartFile = new MockMultipartFile("salaries.csv", "salaries.csv", "text/csv", line.getBytes())

        when:
        payslipSummariesBatch(multipartFile)

        then:
        assert getStatus() == 400
        def data = getData()
        data.errors[0].code == "VALIDATION_ERROR"
        data.errors[0].detail == "The line item entered in invalid Test,User,,9,JUNE, please update this entry before continuing"
    }
}