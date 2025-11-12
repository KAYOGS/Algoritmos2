package algoritmos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Aresta implements Comparable<Aresta> {

    int origem, destino, peso;

    public Aresta(int origem, int destino, int peso) {
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
    }

    @Override
    public int compareTo(Aresta outra) {
        return this.peso - outra.peso;
    }
}

public class Kruskal {

    private final int numVertices;
    private final List<Aresta> listaArestas;

    public Kruskal(int numVertices) {
        this.numVertices = numVertices;
        this.listaArestas = new ArrayList<>();
    }

    public void adicionarAresta(int origem, int destino, int peso) {
        listaArestas.add(new Aresta(origem, destino, peso));
    }

    public int executar() {
        Collections.sort(listaArestas);

        int[] conjunto = new int[numVertices]; 

        for (int i = 0; i < numVertices; i++) {
            conjunto[i] = i;
        }

        int custoTotalAGM = 0;
        int arestasAdicionadas = 0;

        for (Aresta aresta : listaArestas) {
            int raizOrigem = encontrarRaiz(conjunto, aresta.origem);
            int raizDestino = encontrarRaiz(conjunto, aresta.destino);

            if (raizOrigem != raizDestino) {
                custoTotalAGM += aresta.peso;
                arestasAdicionadas++;
                unirConjuntos(conjunto, raizOrigem, raizDestino);
            }
            if (arestasAdicionadas == numVertices - 1) {
                break;
            }
        }

        return custoTotalAGM;
    }

    private int encontrarRaiz(int[] conjunto, int i) {
        if (conjunto[i] != i) {
            conjunto[i] = encontrarRaiz(conjunto, conjunto[i]);
        }

        return conjunto[i];
    }

    private void unirConjuntos(int[] conjunto, int x, int y) {
        int raizX = encontrarRaiz(conjunto, x);
        int raizY = encontrarRaiz(conjunto, y);
        conjunto[raizX] = raizY;
    }
}