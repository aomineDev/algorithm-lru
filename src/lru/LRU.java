package lru;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class LRU {

    private Integer memoria[][];
    private int marcos;
    private int paginasProceso;
    private Integer paginasParaAcceder[];
    private int fallos;
    private int mayorNumeroPasos;
    private Integer paginaConMasPasos;
    private int cantidadDePaginasAAcceder;
    private HashSet<Integer> paginas;
    private HashMap<Integer, Pagina> paginasMap;
    private String datos;

    public LRU(int marcos, int paginasProceso) {
        this.marcos = marcos;
        this.paginasProceso = paginasProceso;
        mayorNumeroPasos = 0;
        paginas = new HashSet<>();
        paginasMap = new HashMap<>();
    }

    public void setPaginasParaUsar(Integer[] paginasParaAcceder) throws Exception {
        for (Integer pagina : paginasParaAcceder) {
            if (pagina > paginasProceso) {
                throw new Exception("Alguna pagina supera el limite permitido");
            }
        }

        this.paginasParaAcceder = paginasParaAcceder;
        cantidadDePaginasAAcceder = paginasParaAcceder.length;

        iniciarMemoria(marcos, cantidadDePaginasAAcceder);
        iniciarPaginas(paginasParaAcceder);
        iniciarPaginasMap(paginas);
    }

    public void iniciarMemoria(int filas, int columnas) {
        memoria = new Integer[filas][columnas];
    }

    public void iniciarPaginas(Integer[] paginasArray) {
        paginas.addAll(Arrays.asList(paginasArray));
    }

    public void iniciarPaginasMap(HashSet<Integer> paginas) {
        paginas.forEach(pagina -> paginasMap.put(pagina, new Pagina(null, -1)));
    }

    public void accesoAPagina(int columna) {
        Integer pagina = paginasParaAcceder[columna];

        if (columna != 0) {
            for (int fila = 0; fila < marcos; fila++) {
                memoria[fila][columna] = memoria[fila][columna - 1];
            }
        }

        for (int fila = 0; fila < marcos; fila++) {
            if (memoria[fila][columna] == pagina) {
                paginasMap.get(pagina).setPasos(0);
                break;
            }

            if (memoria[fila][columna] == null) {
                memoria[fila][columna] = pagina;
                paginasMap.get(pagina).setPasos(0);
                paginasMap.get(pagina).setIndice(fila);

                fallos++;
                break;
            }

            if (fila != marcos - 1) {
                continue;
            }

            setPaginaConMasPasos();

            Integer indiceMarco = paginasMap.get(paginaConMasPasos).getIndice();
            memoria[indiceMarco][columna] = pagina;
            paginasMap.get(paginaConMasPasos).setPasos(null);
            paginasMap.get(pagina).setPasos(0);
            paginasMap.get(pagina).setIndice(indiceMarco);
            fallos++;
        }

        paginasMap.forEach((key, value) -> value.aumentarPasos());
    }

    public void setPaginaConMasPasos() {
        mayorNumeroPasos = 0;
        paginaConMasPasos = null;

        paginasMap.forEach((key, value) -> {
            Integer pasos = value.getPasos();
            if (pasos != null && pasos >= mayorNumeroPasos) {
                mayorNumeroPasos = pasos;
                paginaConMasPasos = key;
            }
        });
    }

    public String getDatos() {
        datos = "";

        paginasMap.forEach((key, value) -> {
            if (value.getPasos() != null) {
                datos += key + " - pasos: " + value.getPasos() + " - marco: " + (value.getIndice() + 1) + "\n";
            }
        });

        setPaginaConMasPasos();
        datos += "\npagina con mas pasos: " + (paginaConMasPasos == null ? "--" : paginaConMasPasos);
        datos += "\n\nfallos: " + fallos;

        return datos;
    }

    public Integer[][] getMemoria() {
        return memoria;
    }

    public int getMarcos() {
        return marcos;
    }

    public int getPaginasProceso() {
        return paginasProceso;
    }

    public Integer[] getPaginasParaAcceder() {
        return paginasParaAcceder;
    }

    public int getFallos() {
        return fallos;
    }

    public int getCantidadDePaginasAAcceder() {
        return cantidadDePaginasAAcceder;
    }

    @Override
    public String toString() {
        int numeroPaginas = cantidadDePaginasAAcceder;
        return String.format("paginas referenciadas: %d%nfallos: %d%naciertos: %d", numeroPaginas, fallos, numeroPaginas - fallos);
    }
}
