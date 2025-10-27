package algoritmos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Dijkstra {
    private static class Aresta {
        int destino;
        int peso;

        public Aresta() {}
        public Aresta(int destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    private static class NoDistancia {
        int vertice;
        int distancia;

        public NoDistancia(int vertice, int distancia) {
            this.vertice = vertice;
            this.distancia = distancia;
        }
    }

    private Map<Integer, List<Aresta>> grafo;

    public Dijkstra() {
        this.grafo = new HashMap<>();
    }

    public void adicionarAresta(int origem, int destino, int peso) {
        grafo.putIfAbsent(origem, new ArrayList<>());
        grafo.get(origem).add(new Aresta(destino, peso));
    }

    public Map<Integer, Integer> encontrarMenorCaminho(int origem) {
        Map<Integer, Integer> distancias = new HashMap<>();

        PriorityQueue<NoDistancia> filaPrioridade = new PriorityQueue<>(
            (nd1, nd2) -> Integer.compare(nd1.distancia, nd2.distancia)
        );

        for (int vertice : grafo.keySet()) {
            distancias.put(vertice, Integer.MAX_VALUE);
        }

        distancias.put(origem, 0);
        filaPrioridade.add(new NoDistancia(origem, 0));
        return distancias;

        /*while (!filaPrioridade.isEmpty()) {
            NoDistancia atual = filaPrioridade.poll();
            int u = atual.vertice;
            int distanciaU = atual.distancia;

            if (distanciaU > distancias.get(u)) {
                continue;
            }

            List<Aresta> vizinhos = grafo.getOrDefault(u, new ArrayList<>());


        }*/
    }
}