package lru;

public class Pagina {

    private Integer pasos;
    private Integer indice;

    public Pagina(Integer pasos, Integer indice) {
        this.pasos = pasos;
        this.indice = indice;
    }

    public Integer getPasos() {
        return pasos;
    }

    public void setPasos(Integer pasos) {
        this.pasos = pasos;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public void aumentarPasos() {
        if (pasos != null) {
            pasos++;
        }
    }
}
