package algoritmos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class GrafoReader {

    private int numVertices;
    private int numArestas;
    private Dijkstra dijkstra;
    private Prim prim;
    private Kruskal kruskal;
    private int verticeInicial;
    
    private final Map<Integer, Integer> idMapping; 

    public GrafoReader(String caminhoArquivo) throws IOException, IllegalArgumentException {
        idMapping = new TreeMap<>(); 
        lerArquivo(caminhoArquivo);
    }

    private void lerArquivo(String caminhoArquivo) throws IOException, IllegalArgumentException {
        
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("c")) continue;

                if (linha.startsWith("p")) {
                    String[] partes = linha.split("\\s+");
                    
                    if (partes.length < 4) { 
                        throw new IllegalArgumentException("Linha 'p' incompleta. Esperado: p tipo N M");
                    }
                    
                    try {
                        numVertices = Integer.parseInt(partes[2]);
                        numArestas = Integer.parseInt(partes[3]);
                    } catch (NumberFormatException e) {
                        if (partes.length >= 5) {
                            numVertices = Integer.parseInt(partes[3]);
                            numArestas = Integer.parseInt(partes[4]);
                        } else {
                            throw new IllegalArgumentException("Erro ao parsear N e M na linha 'p'.");
                        }
                    }
                    
                    break;
                }
            }
        }
        
        if (numVertices <= 0 || numArestas <= 0) {
             throw new IllegalArgumentException("Não foi possível ler N e M no arquivo do grafo ou são inválidos.");
        }

        this.dijkstra = new Dijkstra();
        this.prim = new Prim();
        this.kruskal = new Kruskal(this.numVertices); 
        this.verticeInicial = 0;

        try (BufferedReader brArestas = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            
            while ((linha = brArestas.readLine()) != null) {
                linha = linha.trim();
                if (linha.startsWith("a")) {
                    String[] partes = linha.split("\\s+");
                    if (partes.length < 4) continue; 

                    int u = Integer.parseInt(partes[1]) - 1; 
                    int v = Integer.parseInt(partes[2]) - 1; 
                    int peso = Integer.parseInt(partes[3]);
                    
                    if (u < 0 || v < 0 || u >= numVertices || v >= numVertices) {
                         continue;
                    }

                    this.dijkstra.adicionarAresta(u, v, peso);
                    
                    this.kruskal.adicionarAresta(u, v, peso); 
                    this.prim.adicionarAresta(u, v, peso); 
                    
                    this.prim.adicionarAresta(v, u, peso); 
                } 
            }
        }
    }
    
    public int getNumVertices() {
        return numVertices;
    }

    public int getNumArestas() {
        return numArestas; 
    }

    public Dijkstra getDijkstra() {
        return dijkstra;
    }

    public Prim getPrim() {
        return prim;
    }

    public Kruskal getKruskal() {
        return kruskal;
    }

    public int getVerticeInicial() {
        return verticeInicial;
    }
}