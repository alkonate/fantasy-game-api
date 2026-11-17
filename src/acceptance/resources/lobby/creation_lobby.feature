Feature: Création du lobby

  En tant que Joueur,
  Je veux créer un lobby,
  Afin de rassembler des joueurs pour une aventure.

  Scenario: Création d'un lobby
    When Je crée un lobby
    Then Le lobby est créé avec:
      | host    | moi |
      | players | moi |
    And L'événement "LobbyCreatedEvent" est publié avec l'identité du lobby

  # Client : un joueur ne peut être que dans un seul lobby à la fois
  Scenario: Refus de la création pour un joueur déjà dans un lobby
    Given Je suis déjà dans un lobby
    When Je crée un lobby
    Then Le lobby n'est pas créé
    And Une erreur de joueur déjà dans un lobby est levée
    And Aucun événement n'est publié