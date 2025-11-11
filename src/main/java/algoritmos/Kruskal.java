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

   public int compareTo(Aresta outra) {
      return this.peso - outra.peso;
   }
}

public class Kruskal {
   private int numVertices;
   private List<Aresta> listaArestas;

   public Kruskal(int numVertices) {
      this.numVertices = numVertices;
      this.listaArestas = new ArrayList<>();
   }

   public void adicionarAresta(int origem, int destino, int peso) {
      listaArestas.add(new Aresta(origem, destino, peso));
   }

   public void executar() {
      Collections.sort(listaArestas);

      int[] conjunto = new int[numVertices];

      for (int i = 0; i < numVertices; i++) conjunto[i] = i;

      List<Aresta> agm = new ArrayList<>();

      for (Aresta aresta : listaArestas) {
         int raizOrigem = encontrarRaiz(conjunto, aresta.origem);
         int raizDestino = encontrarRaiz(conjunto, aresta.destino);

         if (raizOrigem != raizDestino) {
            agm.add(aresta);
            unirConjuntos(conjunto, raizOrigem, raizDestino);
         }
         if (agm.size() == numVertices - 1) break;
      }

      System.out.println("Arvore Geradora Minima:");
      for (Aresta aresta : agm) {
         System.out.println(aresta.origem + " - " + aresta.destino + " : " + aresta.peso);
      }
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
