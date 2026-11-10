# API RPG fantasy au tour par tour (projet de session 420-310-MV)

Le sujet complet est dans `doc/Projet_de_session_API_RPG_A26.pdf`. Il fait foi. Pour le consulter, chercher
dans sa version texte `doc/sujet.txt` (générée par `pdftotext -layout`) plutôt que de lire le PDF ; la
régénérer si le PDF change. Les clarifications du
client font partie du besoin. Ne jamais inventer une règle du domaine : si le document est ambigu,
noter la question dans la section « Questions ouvertes » et demander.

Principe directeur : le serveur est la source de vérité. L'API exprime des actions du jeu
(« Thorin attaque l'Orc »), jamais une modification directe de l'état (« les PV de l'Orc valent 4 »).
Stockage en mémoire seulement (aucune base de données dans cette version).

## Commandes
Java 21, Maven (pas de wrapper dans le dépôt). Aucune application à lancer : pas encore de couche web.
- `mvn test` : tests unitaires seulement (`src/test`).
- `mvn verify` : tests unitaires + tests d'acceptation Cucumber (classes `*AcceptanceTest`, via failsafe).
- Un seul contexte : `mvn verify -Dit.test=HeroProfileAcceptanceTest` (unitaires : `mvn test -Dtest=NameTest`).
- `src/acceptance/java` et `src/acceptance/resources` sont ajoutés aux sources de test par
  `build-helper-maven-plugin`. Ils sont donc compilés aussi par `mvn test` : un code d'acceptance qui ne
  compile pas bloque les tests unitaires.

## Structure
```
src/main/java/com/example/fantasygame/api/
  module/<contexte>/            hero, lobby, ...
    model/                      agrégat, interface du repository
      exception/                exceptions métier (hero utilise encore `Exception`, à renommer)
      valueobject/              value objects et enums du domaine
    application/                <Action>UseCase (interface), <Action>Command (record), <Contexte>Service, gateways
    infrastructure/             adapters en mémoire (InMemory<Agrégat>Repository)
  shared/                       InMemoryEventBus
    contracts/                  IntegrationEvent, IntegrationEventBus
      <contexte>/               événements d'intégration publiés par ce contexte
src/test/java/...               tests unitaires, même arborescence que main
src/acceptance/
  resources/<contexte>/...      fichiers .feature (Gherkin en français)
  java/.../module/<contexte>/
    <Contexte>AcceptanceTest    runner @Suite (sélectionne les features et le glue du contexte)
    context/                    état partagé d'un scénario, câblage du service avec les faux
    dsl/                        <Contexte>Dsl : givens, actions, assertions
    step/                       steps Cucumber (une ligne vers le DSL)
    infrastructure/             faux : FakePlayerGateway, TestInMemoryEventBus, TestInMemory<Agrégat>Repository
```

## Décisions d'architecture (DDD)
- Un contexte borné par domaine : joueur, héros (profil du héros), lobby, partie, combat. Un contexte
  séparé ne se justifie que s'il a ses propres règles, son propre cycle de vie ou son propre processus.
  Une copie locale qui ne contient qu'un identifiant est un signe de découpage trop fin.
- Les agrégats se référencent entre eux par identifiant. Un agrégat ne vérifie que ses propres invariants.
  Les vérifications externes (le joueur existe, le héros appartient au joueur) sont faites dans le
  use case, via une **gateway** définie par le contexte qui en a besoin (`PlayerGateway`).
  Pas de copie locale alimentée par événements pour savoir si une entité existe.
- Un use case ne contient aucune règle du jeu : il charge, vérifie le externe, appelle le domaine,
  sauvegarde, publie les événements.
- La commande est un DTO de types simples (String, Integer, UUID, Map). Le use case convertit en
  value objects (`Species.from`, `HeroClass.from`, `Abilities.chosenFromNames`, `new Name(...)`).
- `playerId` doit venir de l'identité authentifiée, pas du corps de la requête (authentification non
  précisée dans le document : question ouverte).
- Les événements d'intégration ne portent que l'identité de l'agrégat (notification, pas de copie du contenu).
  Ils gardent des `UUID` et des `String`, jamais les types du domaine.

## Modèle du héros (valeurs importantes)
- `HeroProfile` : identité `HeroProfileId`, `PlayerId`, `Name`, `Species`, `HeroClass`, `Abilities`,
  `Experience`, `MaxHitPoints`. Constructeur privé, création par `HeroProfile.create(...)`.
- `Abilities` est un `Set<Ability>` ; `Ability(AbilityType type, int score)`. Valeurs choisies : 3 à 18 ;
  le bonus d'espèce s'applique ensuite ; plafond de 20. Modificateur = `Math.floorDiv(score - 10, 2)`
  (jamais la division entière de Java).
- PV max au niveau 1 = PV de base de la classe + modificateur de la Constitution **finale**, minimum 1.
- Le niveau se déduit de l'XP (`Experience.level()`), il n'est pas stocké séparément.
- Le nom est unique dans tout le système : vérifié dans le use case, pas dans `Name`.
- Le trim du nom se fait à l'entrée (use case) ; `Name` reste strict. Pas encore implémenté.
- `Species` et `HeroClass` sont des enums qui portent leurs règles (bonus, PV de base, magie, équipement).

## Conventions de tests unitaires
- Noms en anglais : `shouldX_whenY`.
- Un comportement par test. Un `@ParameterizedTest` ne regroupe que des cas d'une même règle.
- Chaque règle se teste sur le value object qui la porte (`NameTest`, `AbilityTest`, `ExperienceTest`...).
  `HeroProfileTest` ne teste que l'assemblage.
- Tester les valeurs limites (2 / 30 / 31 caractères, 299 / 300 XP, 18 + bonus contre le plafond de 20).
- Dès qu'il y aura des dés (combat), ils seront injectés pour pouvoir fixer un d20 à 1 ou 20, des
  dégâts précis, un soin précis. Pas de dés dans le code actuel.

## Conventions des tests d'acceptation (Cucumber)
- Un runner et un glue séparés par contexte. Les mêmes phrases peuvent exister dans deux contextes.
- Steps minces : une ligne par step qui appelle le DSL. Le DSL (`<Contexte>Dsl`) fait la traduction et les assertions.
- Méthodes du DSL nommées par une action au présent :
  - Given : prépare l'état directement (repository, faux) : `save...`, `delete...`, `register...`
    (`savePlayerInLobby`, `saveLobbyCreatedBy("Alice")`, `deleteAllHeroProfiles`, `registerDefaultPlayer`) ;
  - When : le nom du use case appelé (`createLobby`, `joinLobby`, `createHeroProfileNamed("Yal")`) ;
  - Then : préfixe `assert`.
- Le `Given` prépare l'état via le repository, jamais via le use case (pas d'événement, pas de dépendance au comportement testé).
- Seul le `When` appelle le use case. Il intercepte l'exception et la range dans le contexte.
- Un seul `Then` avec un tableau clé/valeur pour décrire un résultat. Les clés reprennent les noms du code
  (`name`, `maxHp`...), les valeurs reprennent les codes du domaine (`HUMAN`, `WARRIOR`).
- On ne montre dans un scénario que ce qui est pertinent ; le reste est caché dans des valeurs par défaut du DSL.
- Les règles de détail (validation de format, tableaux espèce / classe, plafond) restent en test unitaire.
  En acceptance, un seul scénario de donnée invalide pour prouver le câblage.
- Un seul contexte Cucumber par scénario (PicoContainer) ; le service et le DSL partagent les mêmes instances de faux.
- Titres de scénarios : un résultat ou un refus avec sa cause (« Refus d'un nom de héros déjà utilisé »).
- Un scénario écrit mais pas encore implémenté est tagué `@todo` ; le runner l'exclut (`not @todo`).

## Règles du lobby (sujet + clarifications du client)
- Un lobby accueille au maximum 4 joueurs (sujet).
- Le créateur rejoint automatiquement le lobby et en est l'hôte (`host`).
- Un joueur n'est que dans un seul lobby à la fois.
- La partie est lançable avec au minimum 1 joueur (l'hôte), et seulement si tous les joueurs ont choisi leur héros.
- Le lobby est fermé après le lancement de la partie : on ne peut plus le rejoindre.
- Quand le créateur quitte le lobby, le lobby est fermé.
- On ne peut rejoindre qu'un lobby ouvert (refus distincts pour verrouillé et fermé).

## Lancer une partie : saga orchestrée (décision d'architecture)
Le lancement traverse deux contextes (lobby, partie). Un orchestrateur enchaîne :
1. le lobby vérifie ses règles (au moins l'hôte, tous les héros choisis) et passe à **verrouillé** :
   plus personne ne peut rejoindre ni partir ;
2. le contexte partie crée la partie avec les joueurs et leurs héros ;
3. succès : le lobby passe à **fermé** ; échec : compensation, le lobby revient à **ouvert**.
Il n'y a donc pas de scénario « le lobby se ferme quand la partie est lancée » : la fermeture est une étape
de la saga, testée dans les scénarios de l'orchestrateur. `LobbyStatus` : `OPEN`, `LOCKED`, `CLOSED`.
L'orchestrateur et les transitions (verrouiller, fermer, déverrouiller) viendront avec la story « lancer la partie ».

## Questions ouvertes (à confirmer avec le client)
- Plancher des caractéristiques après une pénalité d'espèce (valeur finale de 2 possible).
- Double espace dans un nom (« Jean  Pierre ») : permis ?
- XP au-delà du niveau 5 : plafonnée ou seulement le niveau ?
- Que survit à la fin d'une partie (niveau, XP, inventaire) ?
- Authentification : d'où vient `playerId` ?
- Lancer une partie : qu'est-ce qui peut faire échouer la création de la partie (et donc déclencher la
  compensation de la saga) ? Un héros supprimé entre-temps ?
- Restrictions d'équipement : les « armes de mêlée » du Guerrier incluent-elles le bâton et la dague ?

## État d'avancement
- Fait : contexte héros (création du profil : nominal, nom déjà utilisé, joueur inconnu, espèce inconnue).
- Fait : contexte lobby (création du lobby : nominal avec événement `LobbyCreatedEvent`, joueur inconnu,
  joueur déjà dans un lobby).
- Fait (branche `feat/join_lobby`) : rejoindre un lobby (nominal avec événement `PlayerJoinedLobbyEvent`,
  lobby plein, lobby verrouillé, lobby fermé, joueur inconnu, lobby inexistant, joueur déjà dans un lobby).
  `Lobby` porte un `LobbyStatus` ; `Lobby.restore(...)` reconstruit un lobby stocké
  sans vérifier de règle (utilisé par les Given). Le lancement de la partie n'existe pas encore.
- À faire : choisir un héros, lancer la partie, puis partie, rencontres, combat.