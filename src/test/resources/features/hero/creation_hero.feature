Feature: Créer un héros

  En tant que Joueur,
  Je veux créer le profil d'un héros,
  Afin de l'incarner dans l'univers de jeu.

  Scenario: Création d'un héros
    Given Aucun héros n'existe encore
    When Je crée un héros de nom "Yaris", espèce "HUMAN" et de classe "WARRIOR"
    Then l'identité du héros est enregistrée avec le nom "Yaris", l'espèce "HUMAN" et la classe "WARRIOR"
    And l'événement "HeroCreatedEvent" est publié avec l'identité du héros, l'espèce "HUMAN" et la classe "WARRIOR"

  Scenario: Unicité du nom de l'héros
    Given J'ai créé un héros de nom "Bat"
    When Je crée un héros de nom "Bat"
    Then Le nouveau héros n'est pas créé
    And Une erreur d'unicité est levée
    And Aucun évènement n'est publié