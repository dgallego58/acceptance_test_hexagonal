Feature: verificar que el servicio acceptance_test este arriba

  Background:
    * url urlBase
    * configure headers = { 'Accept':  'application/json'}

  Scenario: verificar que status sea 200
    Given path '/actuator/health/liveness'
    When method get
    Then status 200
    And match response == { status:  '#notnull'}

  Scenario: verificar que status este UP
    # Happy Path
    Given path '/actuator/health'
    When method get
    Then status 200
    And match response contains { status: 'UP' }

    # Unhappy Path
    Given path '/e-commerce/notify-subscribers'
    And request 'STRING'
    When method put
    Then status 200

    # Unhappy path 2




