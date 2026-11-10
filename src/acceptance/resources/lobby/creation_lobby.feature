Feature: Création du lobby

  En tant que Joueur,
  Je veux créer un lobby,
  Afin de rassembler des joueurs pour une aventure.

  Scenario: Création d'un lobby
    Given Je suis enregistré comme joueur
    When Je crée un lobby
    Then Le lobby est créé avec:
      | host    | moi |
      | players | moi |
    And L'événement "LobbyCreatedEvent" est publié avec l'identité du lobby

  Scenario: Refus de la création pour un joueur inconnu
    Given Je ne suis pas enregistré comme joueur
    When Je crée un lobby
    Then Le lobby n'est pas créé
    And Une erreur de joueur introuvable est levée
    And Aucun événement n'est publié

  # Client : un joueur ne peut être que dans un seul lobby à la fois
  Scenario: Refus de la création pour un joueur déjà dans un lobby
    Given Je suis enregistré comme joueur
    And Je suis déjà dans un lobby
    When Je crée un lobby
    Then Le lobby n'est pas créé
    And Une erreur de joueur déjà dans un lobby est levée
    And Aucun événement n'est publié