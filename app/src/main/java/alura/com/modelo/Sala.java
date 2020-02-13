package alura.com.modelo;

import java.io.Serializable;

public class Sala implements Serializable {
    private int id;
    private String nome;
    private int quantidadePessoasSentadas;
    private boolean possuiArcon;
    private boolean possuiMultimidia;
    private double areaDaSala;

    public boolean isPossuiArcon() {
        return possuiArcon;
    }

    public boolean isPossuiMultimidia() {
        return possuiMultimidia;
    }

    public double getAreaDaSala() {
        return areaDaSala;
    }

    public void setAreaDaSala(double areaDaSala) {
        this.areaDaSala = areaDaSala;
    }

    public void setPossuiArcon(boolean possuiArcon) {
        this.possuiArcon = possuiArcon;
    }

    public void setPossuiMultimidia(boolean possuiMultimidia) {
        this.possuiMultimidia = possuiMultimidia; }

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



}