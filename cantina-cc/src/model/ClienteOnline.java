package model;

public class ClienteOnline extends PessoaAutenticavel {

    public ClienteOnline(String nome, int id, String senha) {
        setNome(nome);
        setId(id);
        setSenha(senha);
    }
}