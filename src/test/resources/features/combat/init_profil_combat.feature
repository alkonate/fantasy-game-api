Feature: Initialisation profil de combat

  En tant que Referee,
  Je veux dresser le profil combat pour chaque nouveau héros,
  Afin d'Établir son évaluation tactique.

  Scenario:
    Given j'ai reçu l'évènement "heroCreated"
    When j'initialise le profil de combat de l'héros crée
    Then le profil de combat de l'hero est crée
    And un évènement "profilCreat