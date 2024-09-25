# Spring Boot Application
 ## Description
Ce projet est une application basée sur Spring Boot . Elle utilise Maven comme outil de gestion de projet .

 ## Prérequis
Avant de commencer, assurez-vous d'avoir installé les éléments suivants sur votre machine:

1. JDK 17+
  2. Maven 3.8.1+

  ## Installation

### Compiler le projet
Utilisez Maven pour compiler le projet et télécharger les dépendances nécessaires :
- `mvn clean install`.

## Creation de la base de donnees

- Intaller MySQL : https://dev.mysql.com/downloads/installer/

- Aller sur MySQL command line client et
Copier cela :
-  CREATE TABLE `USERS` (
`id` integer PRIMARY KEY AUTO_INCREMENT,
`email` varchar(255),
`name` varchar(255),
`password` varchar(255),
`created_at` timestamp,
`updated_at` timestamp
);

- CREATE TABLE `RENTALS` (
`id` integer PRIMARY KEY AUTO_INCREMENT,
`name` varchar(255),
`surface` numeric,
`price` numeric,
`picture` varchar(255),
`description` varchar(2000),
`owner_id` integer NOT NULL,
`created_at` timestamp,
`updated_at` timestamp
);

- CREATE TABLE `MESSAGES` (
`id` integer PRIMARY KEY AUTO_INCREMENT,
`rental_id` integer,
`user_id` integer,
`message` varchar(2000),
`created_at` timestamp,
`updated_at` timestamp
);

## Configurer la base de données
Dans le fichier application.properties , configurez la connexion à votre base de données :

- spring.datasource.url=jdbc:mysql://localhost:3306/locataire
- spring.datasource.username=nom_utilisateur
- spring.datasource.password=mot_de_passe

## Démarrer l'application
Utilisez la commande suivante pour démarrer l'application :

- `mvn spring-boot:run`
