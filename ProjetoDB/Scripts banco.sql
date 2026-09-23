create database BDAula01;
show databases;
use bdaula01;
CREATE TABLE pessoa(
id int auto_increment PRIMARY KEY, 
nome varchar(50) NOT NULL,
sexo varchar(1) NOT NULL,
idioma varchar(10) NOT NULL
);
show tables;
desc pessoa; -- descrição pessoa

INSERT INTO pessoa (nome, sexo, idioma)
VALUES
("Ricardo", "M", "Português"),
("Rafael", "M", "PortuguêS"),
("Antony", "M", "Francês"),
("Joe", "M", "Espanhol"),
("Gabriel", "M", "Espanish");

select * from pessoa;
 