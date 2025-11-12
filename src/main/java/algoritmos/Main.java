package algoritmos;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Main {

    private static class ResultadoExecucao {
        long custo;
        double tempoSegundos;

        public ResultadoExecucao(long custo, double tempoSegundos) {
            this.custo = custo;
            this.tempoSegundos = tempoSegundos;
        }
    }

    public static void main(String[] args) {
        Map<String, String> instancias = new LinkedHashMap<>();
        instancias.put("NY_dist", "NY_dist.gr");
        instancias.put("BAY_dist", "BAY_dist.gr");
        instancias.put("COL_dist", "COL_dist.gr");
        
        Map<String, Map<String, ResultadoExecucao>> resultadosTabela = new LinkedHashMap<>();

        try {
            for (Map.Entry<String, String> entrada : instancias.entrySet()) {
                String nomeInstancia = entrada.getKey();
                String caminhoArquivo = entrada.getValue();

                GrafoReader reader;
                try {
                    reader = new GrafoReader(caminhoArquivo);
                } catch (IOException | IllegalArgumentException e) {
                    System.err.println("ERRO ao carregar o arquivo " + caminhoArquivo + ": " + e.getMessage());
                    Map<String, ResultadoExecucao> erroResult = new HashMap<>();
                    erroResult.put("CM", new ResultadoExecucao(-1, 0.0));
                    erroResult.put("AGM_Prim", new ResultadoExecucao(-1, 0.0));
                    erroResult.put("AGM_Kruskal", new ResultadoExecucao(-1, 0.0));
                    erroResult.put("FM", new ResultadoExecucao(-1, 0.0));
                    resultadosTabela.put(nomeInstancia + " (ERRO)", erroResult);
                    continue;
                }
                
                int n = reader.getNumVertices();
                int m = reader.getNumArestas();
                int verticeInicial = reader.getVerticeInicial();
                int verticeFinalCM = n > 0 ? n - 1 : verticeInicial; 

                Map<String, ResultadoExecucao> resultadosInstancia = new LinkedHashMap<>();

                long custoCM = 0;
                long startCM = System.nanoTime();
                Map<Integer, Integer> distancias = reader.getDijkstra().encontrarMenorCaminho(verticeInicial);
                long endCM = System.nanoTime();
                double tempoCM = (endCM - startCM) / 1_000_000_000.0; 

                Integer distanciaDestino = distancias.get(verticeFinalCM);
                if (distanciaDestino != null && distanciaDestino != Integer.MAX_VALUE) {
                    custoCM = distanciaDestino;
                } else {
                    custoCM = -1;
                }
                resultadosInstancia.put("CM", new ResultadoExecucao(custoCM, tempoCM));

                long startPrim = System.nanoTime();
                long custoPrim = reader.getPrim().executar(verticeInicial);
                long endPrim = System.nanoTime();
                double tempoPrim = (endPrim - startPrim) / 1_000_000_000.0;
                resultadosInstancia.put("AGM_Prim", new ResultadoExecucao(custoPrim, tempoPrim));
                
                long startKruskal = System.nanoTime();
                long custoKruskal = reader.getKruskal().executar();
                long endKruskal = System.nanoTime();
                double tempoKruskal = (endKruskal - startKruskal) / 1_000_000_000.0;
                resultadosInstancia.put("AGM_Kruskal", new ResultadoExecucao(custoKruskal, tempoKruskal));
                
                resultadosInstancia.put("FM", new ResultadoExecucao(0, 0.0)); 
                
                resultadosTabela.put(nomeInstancia + "|" + n + "|" + m, resultadosInstancia);
            }
            
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Relatorio_Grafos.pdf"));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

            document.add(new Paragraph("Relatório Comparativo de Algoritmos de Grafos", titleFont));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("Análise dos Algoritmos (CM e AGM)", headerFont));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("Dijkstra (Caminho Mínimo - CM):"));
            document.add(new Paragraph(" - Complexidade de Tempo: O(E + V log V) com Fila de Prioridade."));
            document.add(new Paragraph(" - Notas: Encontra o caminho de menor custo de uma origem a todos os outros vértices em grafos com pesos não negativos."));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Prim (Árvore Geradora Mínima - AGM):"));
            document.add(new Paragraph(" - Complexidade de Tempo: O(E log V) ou O((V+E) log V)."));
            document.add(new Paragraph(" - Notas: Algoritmo guloso que constrói a AGM a partir de um vértice inicial."));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Kruskal (Árvore Geradora Mínima - AGM):"));
            document.add(new Paragraph(" - Complexidade de Tempo: O(E log E) ou O(E log V) se V < E."));
            document.add(new Paragraph(" - Notas: Algoritmo guloso que ordena as arestas e usa a estrutura Disjoint Set (Union-Find)."));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Tabelas de Resultados", titleFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            
            table.addCell(new Paragraph("n", headerFont));
            table.addCell(new Paragraph("m", headerFont));
            table.addCell(new Paragraph("CM Custo (Dijkstra)", headerFont));
            table.addCell(new Paragraph("CM Tempo (s)", headerFont));
            table.addCell(new Paragraph("AGM Custo (Kruskal)", headerFont));
            table.addCell(new Paragraph("AGM Tempo (s)", headerFont));
            table.addCell(new Paragraph("FM Custo", headerFont));
            table.addCell(new Paragraph("FM Tempo (s)", headerFont));

            for (Map.Entry<String, Map<String, ResultadoExecucao>> entry : resultadosTabela.entrySet()) {
                String[] info = entry.getKey().split("\\|");
                int n = Integer.parseInt(info[1]);
                int m = Integer.parseInt(info[2]);
                
                Map<String, ResultadoExecucao> res = entry.getValue();

                ResultadoExecucao resCM = res.get("CM");
                ResultadoExecucao resAGM = res.get("AGM_Kruskal"); 
                ResultadoExecucao resFM = res.get("FM"); 

                table.addCell(String.valueOf(n));
                table.addCell(String.valueOf(m));
                table.addCell(String.valueOf(resCM.custo));
                table.addCell(String.format("%.6f", resCM.tempoSegundos));
                table.addCell(String.valueOf(resAGM.custo));
                table.addCell(String.format("%.6f", resAGM.tempoSegundos));
                table.addCell(String.valueOf(resFM.custo));
                table.addCell(String.format("%.6f", resFM.tempoSegundos));
            }
            
            document.add(new Paragraph("Resultados da Execução (Instâncias: NY_dist, BAY_dist, COL_dist)"));
            document.add(new Paragraph(" "));
            document.add(table);
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("Sua Conclusão (Preencher esta seção com a análise dos resultados!)", headerFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Com base nos resultados obtidos para as instâncias de grafos (NY, BAY, COL), observamos que..."));
            document.add(new Paragraph(" "));
            
            document.close();
            System.out.println("Relatorio PDF 'Relatorio_Grafos.pdf' criado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}