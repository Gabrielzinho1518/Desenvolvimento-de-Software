

CREATE DATABASE Escola;
USE Escola;

CREATE TABLE Alunos(
id INT AUTO_INCREMENT PRIMARY KEY,
nome VARCHAR(50) NOT NULL,
idade INT NOT NULL,
curso VARCHAR(50) NOT NULL
);

INSERT INTO Alunos( nome, idade, curso)
VALUES
("Joao", "20", "Matematica"),
("Maria", "22", "Historia"),
("Pedro", "21", "Ciencia da computacao"),
("Ana", "19", "Biologia"),
("Carlos", "23", "Economia");


CREATE TABLE Professores(
id INT AUTO_INCREMENT PRIMARY KEY,
nome VARCHAR(50) NOT NULL,
idade INT NOT NULL,
disciplina VARCHAR(50) NOT NULL
);


INSERT INTO Professores( nome, idade, disciplina)
VALUES
("Fabio", "25", "Matematica"),
("Fabricio", "22", "Historia"),
("Gabriel", "21", "Ciencia da computacao"),
("Gilberto", "19", "Biologia"),
("Arruda", "23", "Economia");


CREATE TABLE Matricula(
id INT AUTO_INCREMENT PRIMARY KEY,
id_alunos INT,
id_professores INT,
data_matriculas DATE,

FOREIGN KEY(id_alunos) REFERENCES alunos(id),
FOREIGN KEY(id_professores) REFERENCES professores(id)
);

INSERT INTO Matricula(id_alunos, id_professores, data_matriculas)
VALUES
(1, 1, "2023-01-15"),
(2, 2, "2023-02-20"),
(3, 3, "2023-03-10"),
(4, 1, "2023-04-05"),
(5, 2, "2023-05-12");


select nome, disciplina FROM professores;
select nome, idade, curso FROM alunos;
select * FROM Matricula;
SELECT *FROM Alunos;







SELECT * FROM escola; 
