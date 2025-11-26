package model;

public abstract class PessoaAutenticavel extends Pessoa {
    private String senha;

    public void setSenha(String senha) { this.senha = senha; }

    public String getSenha(){ return senha;}

    public boolean autenticar(String senhaDigitada) {
        return senha != null && senha.equals(senhaDigitada);
    }
}