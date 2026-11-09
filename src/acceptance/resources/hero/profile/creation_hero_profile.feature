Feature: Création du profil du héros

  En tant que Joueur,
  Je veux créer le profil d'un héros,
  Afin de l'incarner dans l'univers de jeu.

  Scenario: Création du profil d'un héros
    Given Je suis enregistré comme joueur
    And Aucun profil de héros n'existe encore
    When Je crée un profil de héros avec:
      | name      | Yal                                                                              |
      | species   | HUMAN                                                                            |
      | heroClass | WARRIOR                                                                          |
      | abilities | STRENGTH 4, DEXTERITY 9, CONSTITUTION 4, INTELLIGENCE 12, WISDOM 8, CHARISMA 15 |
    Then Le profil du héros est enregistré avec:
      | name            | Yal                                                                              |
      | species         | HUMAN                                                                            |
      | heroClass       | WARRIOR                                                                          |
      | abilities       | STRENGTH 5, DEXTERITY 10, CONSTITUTION 5, INTELLIGENCE 13, WISDOM 9, CHARISMA 16 |
      | level           | 1                                                                                |
      | xp              | 0                                                                                |
      | maxHp           | 9                                                                                |
      | maxMagic        | 0                                                                                |
      | dominantAbility | STRENGTH                                                                         |
      | startingWeapon  | LONGSWORD                                                                        |
      | startingArmor   | CHAIN_MAIL                                                                       |
    And L'événement "HeroProfileCreatedEvent" est publié avec l'identité du profil

  Scenario: Unicité du nom du héros
    Given Je suis enregistré comme joueur
    And Un profil de héros de nom "Yal" existe déjà
    When Je crée un profil de héros de nom "Yal"
    Then Le profil n'est pas créé
    And Une erreur d'unicité est levée
    And Aucun événement n'est publié

  Scenario: Refus de la création pour un joueur inconnu
    Given Je ne suis pas enregistré comme joueur
    And Aucun profil de héros n'existe encore
    When Je crée un profil de héros de nom "Yal"
    Then Le profil n'est pas créé
    And Une erreur de joueur introuvable est levée
    And Aucun événement n'est publié

  Scenario: Refus d'une espèce de héros inconnue
    Given Je suis enregistré comme joueur
    And Aucun profil de héros n'existe encore
    When Je crée un profil de héros d'espèce "DRAGON"
    Then Le profil n'est pas créé
    And Une erreur d'espèce inconnue est levée
    And Aucun événement n'est publié