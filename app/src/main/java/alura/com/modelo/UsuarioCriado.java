package alura.com.modelo;

public class UsuarioCriado {
    private final String nome;
    private final String email;
    private final String senha;

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public UsuarioCriado(String nome, String email, String senha) {

        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}
