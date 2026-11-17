# API RPG fantasy au tour par tour (projet de session 420-310-MV)

Le serveur est la source de vérité : l'API exprime des actions du jeu (« Thorin attaque l'Orc »), jamais une
modification directe de l'état (« les PV de l'Orc valent 4 »). Stockage en mémoire seulement.

## Documents (à lire seulement quand la tâche le demande)
- `doc/regles-domaine.md` : résumé des règles du jeu (lobby, héros, espèces, classes, équipement, combat,
  sorts, créatures, XP, API attendue) + modèle du héros. À lire avant de toucher au domaine.
- `doc/conventions-tests.md` : conventions des tests unitaires et Cucumber. À lire avant d'écrire des tests.
- `doc/Projet_de_session_API_RPG_A26.pdf` : sujet complet, fait foi. 17 pages, coûteux : ne l'ouvrir que pour
  vérifier un point précis (`pdftotext` ou seulement les pages utiles).
- Ne jamais inventer une règle du domaine : si c'est ambigu, l'ajouter aux « Questions ouvertes » et demander.

## Commandes
Java 21, Maven (pas de wrapper). Pas encore de couche web. Utiliser `-q` pour réduire la sortie.
- `mvn -q test` : unitaires (`-Dtest=NameTest` pour un seul).
- `mvn -q verify` : unitaires + acceptation Cucumber (`*AcceptanceTest`, failsafe ;
  `-Dit.test=HeroProfileAcceptanceTest` pour un seul contexte).
- `src/acceptance/*` est compilé aussi par `mvn test` (build-helper) : du code d'acceptance cassé bloque les unitaires.

## Structure
`src/main/java/com/example/fantasygame/api/module/<contexte>/` avec `model/` (agrégat, repository,
`exception/`, `valueobject/`), `application/` (`<Action>UseCase`, `<Action>Command`, `<Contexte>Service`,
gateways), `infrastructure/` (`InMemory<Agrégat>Repository`). `shared/contracts/<contexte>/` : événements
d'intégration. Acceptance : `src/acceptance/{resources,java}/.../<contexte>/` (runner, `context/`, `dsl/`,
`step/`, `infrastructure/` pour les faux). Le contexte hero utilise encore `Exception`, à renommer.

## Décisions d'architecture (DDD)
- Un contexte borné par domaine : joueur, héros, lobby, partie, combat. Un contexte séparé seulement s'il a
  ses propres règles, cycle de vie ou processus (une copie locale réduite à un identifiant = découpage trop fin).
- Agrégats référencés par identifiant ; un agrégat ne vérifie que ses invariants. Vérifications externes
  (joueur existe, héros appartient au joueur) dans le use case via une **gateway** du contexte demandeur
  (`PlayerGateway`). Pas de copie locale alimentée par événements.
- Un use case ne contient aucune règle du jeu : charger, vérifier l'externe, appeler le domaine, sauvegarder, publier.
- Commande = DTO de types simples (String, Integer, UUID, Map) ; le use case convertit en value objects.
- `playerId` doit venir de l'identité authentifiée, pas du corps de la requête.
- Événements d'intégration : identité de l'agrégat seulement, en `UUID`/`String`, jamais de types du domaine.
- Modificateur de caractéristique = `Math.floorDiv(score - 10, 2)` (jamais la division entière de Java).

## Questions ouvertes (à confirmer avec le client)
- Plancher des caractéristiques après une pénalité d'espèce (valeur finale de 2 possible).
- Double espace dans un nom (« Jean  Pierre ») : permis ?
- XP au-delà du niveau 5 : plafonnée ou seulement le niveau ?
- Que survit à la fin d'une partie (niveau, XP, inventaire) ?
- Authentification : d'où vient `playerId` ?
- Lobby : le créateur rejoint-il automatiquement et a-t-il un rôle ? Un joueur dans plusieurs lobbys ?
  Qu'est-ce qui rend la partie lançable ? Minimum de joueurs ? Lobby fermé après le lancement ?
  Que devient le lobby quand le créateur ou le dernier joueur part ?
- Restrictions d'équipement : les « armes de mêlée » du Guerrier incluent-elles le bâton et la dague ?

## État d'avancement
- Fait : contexte héros (création du profil : nominal, nom déjà utilisé, joueur inconnu, espèce inconnue).
- Fait : contexte lobby (création : nominal avec `LobbyCreatedEvent`, joueur inconnu, joueur déjà dans un lobby).
  Hypothèses non confirmées : le créateur devient `host` et premier joueur ; un joueur n'est que dans un seul lobby.
- À faire : rejoindre, choisir un héros, lancer la partie, puis partie, rencontres, combat.
