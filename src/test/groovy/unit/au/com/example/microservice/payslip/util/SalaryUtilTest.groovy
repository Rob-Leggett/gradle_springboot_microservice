package au.com.example.microservice.payslip.util

import spock.lang.Specification

import java.time.Month;

class SalaryUtilTest extends Specification {

    def SalaryUtil salaryUtil

    def TaxUtil mockTaxUtil

    void setup() {
        mockTaxUtil = Mock(TaxUtil)

        salaryUtil = new SalaryUtil("taxUtil": mockTaxUtil)
    }

    def "should calculate monthly gross income successfully"() {
        given:
        def income = 12006

        when:
        def result = salaryUtil.calculateMonthlyGrossIncome(income);

        then:
        result == 1001
    }

    def "should calculate monthly income tax successfully"() {
        given:
        def income = 12006
        def month = Month.JULY

        def brackets = [] as ArrayList
        brackets.add(new TaxRatesConfiguration.Bracket(5001, 60000, 0.789f, 3000))

        when:
        def result = salaryUtil.calculateMonthlyIncomeTax(income, month);

        then:
        1 * mockTaxUtil.getTaxBrackets(month) >> brackets
        1 * mockTaxUtil.determineTaxBracket(income, brackets) >> brackets.get(0)
        result == 711
    }

    def "should calculate net income successfully"() {
        given:
        def gross = 10000
        def tax = 3000

        when:
        def result = salaryUtil.calculateNetIncome(gross, tax)

        then:
        result == 7000
    }

    def "should calculate superannuation successfully"() {
        given:
        def gross = 10000
        def superannuationPercentage = 10

        when:
        def result = salaryUtil.calculateSuperannuation(gross, superannuationPercentage)

        then:
        result == 1000
    }

    def "should calculate previous max successfully"() {
        given:
        def min = 2001

        when:
        def result = salaryUtil.calculatePreviousMax(min)

        then:
        result == 2000
    }

    def "should calculate previous max when min is 0 successfully"() {
        given:
        def min = 0

        when:
        def result = salaryUtil.calculatePreviousMax(min)

        then:
        result == 0
    }
}
