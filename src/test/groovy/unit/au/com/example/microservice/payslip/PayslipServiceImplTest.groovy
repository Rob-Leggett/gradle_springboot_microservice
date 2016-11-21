package au.com.example.microservice.payslip

import au.com.example.microservice.payslip.model.Employee
import au.com.example.microservice.payslip.model.Income
import au.com.example.microservice.payslip.model.IncomeSummary
import au.com.example.microservice.payslip.model.PayslipSummary
import au.com.example.microservice.payslip.util.SalaryUtil
import spock.lang.Specification

import java.time.Month

class PayslipServiceImplTest extends Specification {

    def PayslipService payslipService

    def SalaryUtil mockSalaryUtil;

    void setup() {

        mockSalaryUtil = Mock(SalaryUtil)

        payslipService = new PayslipServiceImpl("salaryUtil": mockSalaryUtil)
    }

    def "should calculate the payslip summary from the income summary successfully"() {
        given:
        def gross = 50000
        def superannuationPercentage = 9
        def month = Month.JUNE

        def incomeSummary = new IncomeSummary(
                new Employee("Test", "User"),
                month,
                new Income(gross, superannuationPercentage))

        // mock responses
        def monthlyGross = 5000
        def monthlyTax = 900
        def net = 4100
        def superannuation = 420

        when:
        def result = payslipService.calculate(incomeSummary)

        then:
        result instanceof PayslipSummary
        1 * mockSalaryUtil.calculateMonthlyGrossIncome(gross) >> monthlyGross
        1 * mockSalaryUtil.calculateMonthlyIncomeTax(gross, month) >> monthlyTax
        1 * mockSalaryUtil.calculateNetIncome(monthlyGross, monthlyTax) >> net
        1 * mockSalaryUtil.calculateSuperannuation(monthlyGross, superannuationPercentage) >> superannuation
        result.getEmployee().getFirstName() == "Test"
        result.getEmployee().getLastName() == "User"
        result.getMonth().name() == "JUNE"
        result.getSalary().getGross() == 5000
        result.getSalary().getNet() == 4100
        result.getSalary().getTax() == 900
        result.getSalary().getSuperannuation() == 420
    }
}
