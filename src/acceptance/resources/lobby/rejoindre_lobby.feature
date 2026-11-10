Feature: Rejoindre un lobby

  En tant que Joueur,
  Je veux rejoindre un lobby,
  Afin de participer à une aventure avec d'autres joueurs.

  Scenario: Arrivée d'un joueur dans un lobby
    Given Je suis enregistré comme joueur
    And Un lobby créé par "Alice" existe
    When Je rejoins le lobby
    Then Le lobby contient:
      | host    | Alice      |
      | players | Alice, moi |
    And L'événement "PlayerJoinedLobbyEvent" est publié avec l'identité du lobby

  # Sujet : « Un lobby peut accueillir un maximum de quatre joueurs. »
  Scenario: Refus d'un joueur dans un lobby plein
    Given Je suis enregistré comme joueur
    And Un lobby existe avec les joueurs "Alice, Bob, Carla, David"
    When Je rejoins le lobby
    Then Le lobby n'est pas modifié
    And Une erreur de lobby plein est levée
    And Aucun événement n'est publié

  # Le lobby est verrouillé pendant le lancement de la partie (saga)
  Scenario: Refus de rejoindre un lobby verrouillé
    Given Je suis enregistré comme joueur
    And Un lobby verrouillé existe
    When Je rejoins le lobby
    Then Le lobby n'est pas modifié
    And Une erreur de lobby verrouillé est levée
    And Aucun événement n'est publié

  # La cause de la fermeture (fin du lancement, départ du créateur) relève d'autres features
  Scenario: Refus de rejoindre un lobby fermé
    Given Je suis enregistré comme joueur
    And Un lobby fermé existe
    When Je rejoins le lobby
    Then Le lobby n'est pas modifié
    And Une erreur de lobby fermé est levée
    And Aucun événement n'est publié

  Scenario: Refus d'un joueur inconnu
    Given Je ne suis pas enregistré comme joueur
    And Un lobby existe
    When Je rejoins le lobby
    Then Le lobby n'est pas modifié
    And Une erreur de joueur introuvable est levée
    And Aucun événement n'est publié

  Scenario: Refus de rejoindre un lobby inexistant
    Given Je suis enregistré comme joueur
    When Je rejoins un lobby qui n'existe pas
    Then Une erreur de lobby introuvable est levée
    And Aucun événement n'est publié

  # Client : un joueur ne peut être que dans un seul lobby à la fois
  Scenario: Refus d'un joueur déjà dans un lobby
    Given Je suis enregistré comme joueur
    And Je suis déjà dans un lobby
    And Un lobby existe
    When Je rejoins le lobby
    Then Le lobby n'est pas modifié
    And Une erreur de joueur déjà dans un lobby est levée
    And Aucun événement n'est publié
