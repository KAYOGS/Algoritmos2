package algoritmos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Prim {

    private static class Aresta {
        int destino;
        int peso;

        public Aresta(int destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    private static class NoCusto {
        int vertice;
        int custo; 

        public NoCusto(int vertice, int custo) {
            this.vertice = vertice;
            this.custo = custo;
        }
    }

    private Map<Integer, List<Aresta>> grafo;
    private Set<Integer> vertices;

    public Prim() {
        this.grafo = new HashMap<>();
        this.vertices = new HashSet<>();
    }

    public void adicionarAresta(int origem, int destino, int peso) {
        grafo.putIfAbsent(origem, new ArrayList<>());
        grafo.get(origem).add(new Aresta(destino, peso));

        grafo.putIfAbsent(destino, new ArrayList<>());
        grafo.get(destino).add(new Aresta(origem, peso));

        vertices.add(origem);
        vertices.add(destino);
    }

    public int executar(int inicio) {
        int custoTotalAGM = 0;

        Set<Integer> naAGM = new HashSet<>();

        Map<Integer, Integer> custoParaConectar = new HashMap<>();

        PriorityQueue<NoCusto> filaPrioridade = new PriorityQueue<>(
                (nc1, nc2) -> Integer.compare(nc1.custo, nc2.custo)
        );

        for (int vertice : vertices) {
            custoParaConectar.put(vertice, Integer.MAX_VALUE);
        }

        custoParaConectar.put(inicio, 0);
        filaPrioridade.add(new NoCusto(inicio, 0));

        while (!filaPrioridade.isEmpty()) {
            NoCusto atual = filaPrioridade.poll();
            int u = atual.vertice;
            int custoU = atual.custo;

            if (naAGM.contains(u)) {
                continue;
            }

            naAGM.add(u);
            custoTotalAGM += custoU;

            if (!grafo.containsKey(u)) {
                continue;
            }

            for (Aresta aresta : grafo.get(u)) {
                int v = aresta.destino;
                int pesoUV = aresta.peso;

                if (!naAGM.contains(v) && pesoUV < custoParaConectar.get(v)) {
                    custoParaConectar.put(v, pesoUV);
                    filaPrioridade.add(new NoCusto(v, pesoUV));
                }
            }
        }

        if (naAGM.size() != vertices.size()) {
             System.out.println("Aviso: O grafo pode ser desconexo. A AGM pode estar incompleta.");
        }

        return custoTotalAGM;
    }
}