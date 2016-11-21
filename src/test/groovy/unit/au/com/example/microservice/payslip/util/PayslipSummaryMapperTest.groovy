package au.com.example.microservice.payslip.util

import au.com.example.microservice.payslip.model.Employee
import au.com.example.microservice.payslip.model.PayslipSummary
import au.com.example.microservice.payslip.model.Salary
import spock.lang.Specification

import java.time.Month;

class PayslipSummaryMapperTest extends Specification {

    def PayslipSummaryMapper payslipSummaryMapper

    void setup() {

        payslipSummaryMapper = new PayslipSummaryMapper()
    }

    def "should map payslip summary and return as string successfully"() {
        given:
        def payslipSummary = new PayslipSummary(
                new Employee("Test", "User"),
                Month.JANUARY,
                new Salary(5000, 2000, 900, 400))

        when:
        def result = payslipSummaryMapper.mapToString.apply(payslipSummary);

        then:
        result == "Test,User,JANUARY,5000,900,2000,400\n"
    }
}
