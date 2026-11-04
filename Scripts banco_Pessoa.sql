create database DBAula01;
show databases;

use dbaula01;

CREATE TABLE pessoa(
id int auto_increment PRIMARY KEY,
nome varchar(50) NOT NULL,
sexo varchar(1) NOT NULL,
idioma varchar(10) NOT NULL
);

SHOW tables;
desc pessoa; 

INSERT INTO pessoa( nome, sexo, idioma)
VALUES
("Ricardo", "M", "Portugues"),
("Vinicius", "M", "ingles"),
("Ricardo", "M", "Frances"),
("Mario", "M", "Espanhol"),
("Mary", "F", "Italiano");

select * from pessoa;
 

