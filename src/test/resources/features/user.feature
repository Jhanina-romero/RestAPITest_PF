Feature: Users
  Scenario: como usuario quiero verificar el crud del los usuarios por api

    Given a user created
    When send POST request "/api/user.json" with body
    """
    {
      "Email":"<random_email>",
      "FullName":"jhanina romero",
      "Password":"test123*"
    }
    """
    Then response code should be 200
    And attribute for "FullName" should be "jhanina romero"
    And save "Id" in the variable "$USER_ID"
    When authenticate with user created
    Then response code should be 200
    When send PUT request "/api/user/$USER_ID.json" with body
    """
    {
      "FullName":"jhanina"
    }
    """
    Then response code should be 200
    And attribute for "FullName" should be "jhanina"
    When send DELETE request "/api/user/$USER_ID.json" with body
    """
    """
    Then response code should be 200
    And attribute for "FullName" should be "jhanina"