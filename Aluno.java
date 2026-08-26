package com.mycompany.cadastroalunos;

public class Aluno {

    private String nome;
    private String dataNascimento;
    private String sexo;
    private String matricula;
    private String curso;
    private String cpf;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String telefone;

    public Aluno(String nome, String dataNascimento, String sexo,
            String matricula, String curso, String cpf,String rua,
            String numero, String bairro,String cidade, String estado,
            String cep,String telefone) {

        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.matricula = matricula;
        this.curso = curso;
        this.cpf = cpf;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.telefone = telefone;
    }


    @Override
    public String toString() {
        return nome + ";" + dataNascimento + ";" + sexo + ";"+ matricula + ";" + curso + ";" + cpf + ";"+ rua + ";" + numero + ";" + bairro + ";"+ cidade + ";" + estado + ";" + cep + ";"+ telefone;
    }
}
