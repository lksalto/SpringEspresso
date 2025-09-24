package br.ufscar.dc.dsw.dtos;

import java.util.UUID;

public class ExemploDto {
    private UUID id;
    private String texto;
    private String urlImagem;
    private int ordem;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }
    public int getOrdem(){return ordem;}
    public void setOrdem(int ordem) {this.ordem = ordem;}
}