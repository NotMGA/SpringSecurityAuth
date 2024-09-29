# Spring Boot Application

## Description

Ce projet est une application basée sur Spring Boot . Elle utilise Maven comme outil de gestion de projet .

## Prérequis

Avant de commencer, assurez-vous d'avoir installé les éléments suivants sur votre machine:

- JDK 17+
- Maven 3.8.1+
- Front end : https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring

## Installation

### Compiler le projet

Utilisez Maven pour compiler le projet et télécharger les dépendances nécessaires :

- `mvn clean install`.

## Creation de la base de donnees

- Intaller MySQL : https://dev.mysql.com/downloads/installer/

- Aller sur MySQL command line client et
  Copier cela :
- CREATE DATABASE nom_data_base
- USE nom_data_base

- CREATE TABLE `USERS` (
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
- CREATE UNIQUE INDEX `USERS_index` ON `USERS` (`email`);

## Configurer la base de données et les données sensible

Dans le fichier application.properties , configurez la connexion à votre base de données :

- spring.datasource.url=jdbc:mysql://localhost:3306/locataire

dans l inviter de commande power shell -$env:DB_USERNAME="ypur_db_username"
 -$env:DB_PASSWORD="your_db_password"
-$env:JWT_SECRET="your_JWTsecret"

## Démarrer l'application

Utilisez la commande suivante pour démarrer l'application :

- `mvn spring-boot:run`

## SWAGGER

http://localhost:3001/swagger-ui/index.html
