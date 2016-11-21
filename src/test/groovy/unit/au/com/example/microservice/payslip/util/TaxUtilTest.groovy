package au.com.example.microservice.payslip.util

import spock.lang.Specification

import java.time.Month;

class TaxUtilTest extends Specification {

    def TaxUtil taxUtil

    def TaxRatesConfiguration mockTaxRatesConfiguration

    void setup() {
        mockTaxRatesConfiguration = Mock(TaxRatesConfiguration)

        taxUtil = new TaxUtil("taxRatesConfiguration": mockTaxRatesConfiguration)
    }

    def "should get previous tax bracket successfully"() {
        given:
        def month = Month.JUNE

        def brackets = [] as ArrayList
        brackets.add(new TaxRatesConfiguration.Bracket())

        when:
        def result = taxUtil.getTaxBrackets(month)

        then:
        1 * mockTaxRatesConfiguration.getPrevious() >> brackets
        result instanceof ArrayList
    }

    def "should get current tax bracket successfully"() {
        given:
        def month = Month.JULY

        def brackets = [] as ArrayList
        brackets.add(new TaxRatesConfiguration.Bracket())

        when:
        def result = taxUtil.getTaxBrackets(month)

        then:
        1 * mockTaxRatesConfiguration.getCurrent() >> brackets
        result instanceof ArrayList
    }

    def "should determine tax bracket successfully"() {
        given:
        def income = 30000

        def brackets = [] as ArrayList
        brackets.add(new TaxRatesConfiguration.Bracket(0, 20000, 0.123f, 1000))
        brackets.add(new TaxRatesConfiguration.Bracket(20001, 40000, 0.456f, 2000))
        brackets.add(new TaxRatesConfiguration.Bracket(40001, 60000, 0.789f, 3000))

        when:
        def result = taxUtil.determineTaxBracket(income, brackets)

        then:
        result.getMin() == 20001
        result.getMax() == 40000
        result.getCents() == 0.456f
        result.getOffset() == 2000
    }

    def "should not determine tax bracket successfully"() {
        given:
        def income = 80000

        def brackets = [] as ArrayList
        brackets.add(new TaxRatesConfiguration.Bracket(0, 20000, 0.123f, 1000))
        brackets.add(new TaxRatesConfiguration.Bracket(20001, 40000, 0.456f, 2000))
        brackets.add(new TaxRatesConfiguration.Bracket(40001, 60000, 0.789f, 3000))

        when:
        def result = taxUtil.determineTaxBracket(income, brackets)

        then:
        result == null
    }
}
