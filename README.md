# CarteTresor

Projet Java développé dans le cadre d’un entretien d’embauche.

## Description

Ce projet est une application Java utilisant Maven comme outil de gestion de dépendances et de build.
Il s’agit d’un exemple simple pour démontrer ma capacité à créer un projet Java, l’utilisation de bonnes pratiques et à exécuter du code dans un environnement moderne.

## Prérequis

- Java 17 ou supérieur
- Maven 3.8+

## Installation

1. Cloner le dépôt :
   ```
   git clone <url-du-repo>
   ```
2. Se placer dans le dossier du projet :
   ```
   cd CarteTresor
   ```
3. Compiler le projet :
   ```
   mvn clean install
   ```

## Exécution

Pour lancer l’application :
```
mvn exec:java -Dexec.mainClass="org.cartetresor.Main"
```

## Structure du projet

- `src/main/java` : Code source principal
- `src/test/java` : Tests unitaires
- `pom.xml` : Fichier de configuration Maven

## Auteur

Maxime Leviel

