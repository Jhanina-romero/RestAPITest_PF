Feature: Users
  Scenario: como usuario quiero verificar el crud del los usuarios por api

    Given using token in todo.ly
    When send the POST request "/api/user.json" with body
    """
    {
      "Email":"<random_email>",
      "FullName":"jhanina romero",
      "Password":"1234"
    }
    """
    Then the response code should be 200
    And the attribute for "FullName" should be "jhanina romero"
    And save the "Id" in the variable "$USER_ID"
    When send the PUT request "/api/user/$USER_ID.json" with body
    """
    {
      "FullName":"jhanina"
    }
    """
    Then the response code should be 200
    And the attribute for "FullName" should be "jhanina"
    When send the DELETE request "/api/user/$USER_ID.json" with body
    """
    """
    Then the response code should be 200
    And the attribute for "FullName" should be "jhanina"