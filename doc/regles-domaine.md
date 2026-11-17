# Règles du domaine (résumé du PDF, sections 2 à 18)

Résumé de `doc/Projet_de_session_API_RPG_A26.pdf` (le PDF fait foi). Lire ce fichier d'abord ; n'ouvrir
le PDF que pour vérifier un point précis (`pdftotext` ou seulement les pages utiles).

- **Lobby / partie** : lobby de 4 joueurs max ; chaque joueur y sélectionne un de ses héros ; quand les joueurs
  sont prêts, la partie est lancée et les héros sélectionnés forment le groupe. Un seul combat actif par
  partie. Partie terminée si tous les héros du groupe sont vaincus. Repos entre les combats seulement :
  restaure PV et magie, retire les effets temporaires.
- **Héros** : création au niveau 1, 0 XP, PV et magie au maximum. L'état est conservé entre les combats
  (pas de soin automatique). Nom : 2 à 30 caractères, lettres et espaces, pas d'espace au début ni à la fin, unique.
  Six caractéristiques (FOR, DEX, CON, INT, SAG, CHA).
- **Espèces** : Humain +1 partout ; Elfe +2 DEX, +1 INT ; Nain +2 CON, +1 FOR ; Orc +2 FOR, +1 CON, -1 INT.
- **Classes** (PV de base / magie / équipement initial / aptitude dominante) :
  Guerrier 12 / 0 / épée longue + mailles / FOR ; Rôdeur 10 / 0 / arc + cuir / DEX ;
  Mage 6 / 10 / bâton, sans armure / INT ; Clerc 10 / 8 / masse + mailles / SAG.
- **Équipement autorisé** : Guerrier armes de mêlée, armures légères et lourdes ; Rôdeur arcs, dagues, épées,
  armures légères ; Mage bâtons, dagues, aucune armure ; Clerc masses, bâtons, armures légères et lourdes.
- **PV** : actuels entre 0 et le max. À 0 PV : hors combat, aucune action ; un soin le ramène en jeu dès 1 PV.
  **Magie** : entre 0 et le max ; consommée par un sort ; refus si magie insuffisante.
- **Inventaire** : 10 objets max par héros, chaque exemplaire compte ; refus si plein ; il faut posséder
  un objet pour l'utiliser ou l'équiper. Une arme et une armure principales à la fois ; la classe doit les
  permettre. Changer d'arme : libre hors combat, coûte l'action principale en combat.
- **Armes** (dégâts, caractéristique) : épée longue 1d8 FOR ; hache 1d10 FOR ; masse 1d6 FOR ; dague 1d4 DEX ;
  arc 1d8 DEX ; bâton 1d6 FOR.
- **CA** : sans armure 10 + mod DEX ; cuir 11 + mod DEX ; mailles 16 (sans DEX).
- **Potion de soin** : 2d4 + 2 PV, sans dépasser le max ; consommée ; coûte l'action principale en combat.
- **Rencontres** : Patrouille gobeline (3 Gobelins) ; Campement orc (2 Orcs, 1 Gobelin) ; Antre du Troll
  (1 Troll). Une rencontre remportée ne peut pas être recommencée dans la même partie.
- **Créatures** (PV / CA / init / XP / attaque ; cible) : Gobelin 7 / 12 / +2 / 50 / dague rouillée +3, 1d6+1 ;
  héros actif avec le moins de PV. Orc 15 / 13 / +1 / 100 / hache +4, 1d8+2 ; héros actif avec le plus de PV.
  Troll 35 / 15 / 0 / 300 / massue +6, 2d6+3 ; héros actif au hasard. Les créatures n'ont ni classe, ni niveau,
  ni inventaire, ni les six caractéristiques. Comportements extensibles (blessé, capacité spéciale, soutien...).
  Bestiaire : en code d'abord, mais la source (JSON, BD, service) ne doit pas changer le combat.
- **Combat** : deux camps ; participants fixés au début, aucun ajout ensuite. Initiative : héros 1d20 + mod DEX,
  créature 1d20 + bonus ; ordre décroissant, fixe pour tout le combat. Rounds à partir de 1 ; chaque participant
  actif joue une fois par round ; les hors combat sont sautés. Toujours connaître le round, l'ordre et le
  participant actif. Une action principale par tour : attaquer, sort, potion, changer d'arme, se défendre
  (+ terminer son tour).
- **Attaque** : cible ennemie encore active. Héros : 1d20 + mod de la caractéristique de l'arme + maîtrise
  (+2 au niveau 1) ; touche si ≥ CA. 1 naturel : rate toujours. 20 naturel : touche, critique (dés doublés,
  bonus fixe non doublé : 1d8+3 → 2d8+3). Dégâts du héros : dé de l'arme + mod de la caractéristique ;
  créatures : valeurs du bestiaire. PV jamais négatifs. Se défendre : +2 CA jusqu'au début de son prochain tour.
- **Sorts** (coût, cible, effet) : Projectile magique (Mage) 2, un ennemi, 1d6 + mod INT, touche toujours ;
  Boule de feu (Mage) 4, toutes les créatures, 2d6 + mod INT chacune, coût payé une fois ; Soin (Clerc) 3,
  lui ou un autre héros (même à 0 PV), 1d8 + mod SAG ; Bénédiction (Clerc) 4, tous les héros présents,
  +1 CA jusqu'à la fin du prochain tour de chaque héros. Cibles et effets extensibles.
- **Effets temporaires** : Empoisonné 1d4 dégâts au début de chacun de ses tours, durée en tours ; Étourdi
  passe son prochain tour puis l'effet disparaît ; Défense renforcée (+2 se défendre, +1 Bénédiction).
- **Dés** : d4, d6, d8, d10, d20 ; aléatoires en jeu, injectables en test.
- **Fin du combat** : vérifiée après toute action qui peut mettre hors combat. Victoire des héros si toutes
  les créatures sont à 0 PV, défaite si tous les héros sont à 0 PV. Ensuite plus aucune action ni round ;
  le résultat est conservé.
- **XP et niveaux** : XP totale de la rencontre partagée également entre les héros participants, division
  entière (100 / 3 = 33). Seuils : niv 2 = 300, 3 = 900, 4 = 2700, 5 = 6500 (max niveau 5). Montée de niveau :
  PV max + (PV de base / 2 + mod CON), minimum +1.
- **Récompenses** : potions, armes, armures ; les joueurs les attribuent à un héros (ajout à son inventaire).
- **Journal** : historique lisible des événements d'une partie (« Thorin attaque l'Orc avec son épée longue. »,
  « L'attaque touche. », « Gobelin A est hors combat. »...).
- **Stockage** : en mémoire ; retrouver joueurs, héros, lobbys, parties, progression, inventaire, rencontres
  terminées, combat actuel.
- **API attendue** : créer un joueur ou un héros, lister les héros d'un joueur ; créer, rejoindre ou quitter un lobby ;
  sélectionner un héros, lancer la partie ; consulter partie, héros, inventaire, équipement, rencontres, combat,
  initiative, participant actif ; commencer une rencontre ; attaquer, lancer un sort, utiliser une potion,
  changer d'arme, se défendre, finir son tour ; récupérer les récompenses ; se reposer ; consulter le journal.
  Erreurs compréhensibles sans détails techniques (introuvable, nom invalide, lobby plein, inventaire plein,
  équipement incompatible, magie insuffisante, mauvais tour, cible invalide, hors combat, combat terminé...).
- **Hors portée** : carte, distances, temps réel, joueur contre joueur, économie, fabrication.
- **Évolution** : nouvelles espèces, classes, armes, sorts, effets, créatures, comportements, rencontres, sources
  de données sans réécriture importante. L'équipe doit aussi proposer 3 User Stories originales avec critères
  d'acceptation (section 20).

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
