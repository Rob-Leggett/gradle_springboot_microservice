package au.com.example.microservice.payslip.util

import spock.lang.Specification

class TaxRatesConfigurationTest extends Specification {

    def TaxRatesConfiguration configuration

    void setup() {
        configuration = new TaxRatesConfiguration()
    }

    def "should set / get previous successfully"() {
        given:
        def brackets = [] as ArrayList
        brackets.add(new TaxRatesConfiguration.Bracket(10, 20, 0.123f, 9))
        brackets.add(new TaxRatesConfiguration.Bracket(21, 30, 0.123f, 10))
        brackets.add(new TaxRatesConfiguration.Bracket(31, 40, 0.123f, 11))

        when:
        configuration.setPrevious(brackets)

        then:
        def result = configuration.getPrevious()
        result.size() == 3

    }

    def "should set / get current successfully"() {
        given:
        def brackets = [] as ArrayList
        brackets.add(new TaxRatesConfiguration.Bracket(10, 20, 0.123f, 9))
        brackets.add(new TaxRatesConfiguration.Bracket(21, 30, 0.123f, 10))
        brackets.add(new TaxRatesConfiguration.Bracket(31, 40, 0.123f, 11))

        when:
        configuration.setCurrent(brackets)

        then:
        def result = configuration.getCurrent()
        result.size() == 3
    }
}