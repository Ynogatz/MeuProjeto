package alura.com.telalogin.modelo;

public class Sala {
    private int id;
    private String nome;
    private int quantidadePessoasSentadas;
    private boolean possuiProjetor;
    private boolean possuiArcon;
    private double areaDaSala;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadePessoasSentadas() {
        return quantidadePessoasSentadas;
    }

    public void setQuantidadePessoasSentadas(int quantidadePessoasSentadas) {
        this.quantidadePessoasSentadas = quantidadePessoasSentadas;
    }

    public boolean isPossuiProjetor() {
        return possuiProjetor;
    }

    public void setPossuiProjetor(boolean possuiProjetor) {
        this.possuiProjetor = possuiProjetor;
    }

    public boolean isPossuiArcon() {
        return possuiArcon;
    }

    public void setPossuiArcon(boolean possuiArcon) {
        this.possuiArcon = possuiArcon;
    }

    public double getAreaDaSala() {
        return areaDaSala;
    }

    public void setAreaDaSala(double areaDaSala) {
        this.areaDaSala = areaDaSala;
    }
}
