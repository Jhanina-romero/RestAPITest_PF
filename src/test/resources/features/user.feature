Feature: User
  Scenario: como usuario quiero verificar el crud del project por api

    Given using basic authentication in todo.ly
    When send POST request "/api/user.json" with body
    """
    {
      "Email": "user78@gmail.com",
      "FullName": "Jhanina",
      "Password": "pASswoRd"
    }
    """
    Then response code should be 200
    And the attribute "FullName" should be "Jhanina"
    And save "Id" in the variable "$ID_USER"
    When send PUT request "/api/user/$ID_USER.json" with body
    """
    {
      "FullName":"Jhanina Romero"
    }
    """
    Then response code should be 200
    And the attribute "FullName" should be "Jhanina Romero"
    When send DELETE request "/api/user/$ID_USER.json" with body
    """
    """
    Then response code should be 200
    And the attribute "FullName" should be "Jhanina Romero"