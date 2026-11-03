Feature: Création du profil du héros

  En tant que Joueur,
  Je veux créer le profil d'un héros,
  Afin de l'incarner dans l'univers de jeu.

  Scenario: Création du profil d'un héros
    Given Aucun profil de héros n'existe encore
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