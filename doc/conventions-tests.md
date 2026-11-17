# Conventions de tests

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
- Méthodes du DSL qui vérifient : préfixe `assert`.
- Le `Given` prépare l'état via le repository, jamais via le use case (pas d'événement, pas de dépendance au comportement testé).
- Seul le `When` appelle le use case. Il intercepte l'exception et la range dans le contexte.
- Un seul `Then` avec un tableau clé/valeur pour décrire un résultat. Les clés reprennent les noms du code
  (`name`, `maxHp`...), les valeurs reprennent les codes du domaine (`HUMAN`, `WARRIOR`).
- On ne montre dans un scénario que ce qui est pertinent ; le reste est caché dans des valeurs par défaut du DSL.
- Les règles de détail (validation de format, tableaux espèce / classe, plafond) restent en test unitaire.
  En acceptance, un seul scénario de donnée invalide pour prouver le câblage.
- Un seul contexte Cucumber par scénario (PicoContainer) ; le service et le DSL partagent les mêmes instances de faux.
- Titres de scénarios : un résultat ou un refus avec sa cause (« Refus d'un nom de héros déjà utilisé »).
