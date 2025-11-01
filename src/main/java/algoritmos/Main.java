package algoritmos;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Main {
    public static void main(String[] args) {
        int[] sizes = { 100000, 160000, 220000, 280000, 340000, 400000, 460000, 520000, 580000, 640000, 700000 };

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Relatorio_Sorts.pdf"));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            document.add(new Paragraph("Relatorio Comparativo de Algoritmos de Ordenacao", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Analise Teorica:", titleFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Bubble Sort:"));
            document.add(
                    new Paragraph(" - Complexidade de Tempo: O(n) no melhor caso. O(n2) no caso medio e pior caso."));
            document.add(new Paragraph(" - Complexidade de Espaco: O(1)."));
            document.add(new Paragraph(" - Notas: Extremamente ineficiente para grandes volumes de dados."));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Merge Sort:"));
            document.add(new Paragraph(" - Complexidade de Tempo: O(n log n) em todos os casos."));
            document.add(new Paragraph(" - Complexidade de Espaco: O(n)."));
            document.add(new Paragraph(" - Notas: Eficiente e estavel, mas consome mais memoria."));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Quick Sort:"));
            document.add(new Paragraph(" - Complexidade de Tempo: O(n log n) no caso medio. O(n2) no pior caso."));
            document.add(
                    new Paragraph(" - Complexidade de Espaco: O(log n) a O(n)."));
            document.add(
                    new Paragraph(" - Notas: Um dos mais rapidos na pratica, mas sensivel a entrada no pior caso."));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Heap Sort:"));
            document.add(new Paragraph(" - Complexidade de Tempo: O(n log n) em todos os casos."));
            document.add(new Paragraph(" - Complexidade de Espaco: O(1)."));
            document.add(new Paragraph(" - Notas: Eficiente, estavel e usa pouca memoria."));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Insertion Sort:"));
            document.add(
                    new Paragraph(" - Complexidade de Tempo: O(n) no melhor caso. O(n2) no caso medio e pior caso."));
            document.add(new Paragraph(" - Complexidade de Espaco: O(1)."));
            document.add(new Paragraph(" - Notas: Extremamente rapido para dados quase ordenados."));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Selection Sort:"));
            document.add(new Paragraph(" - Complexidade de Tempo: O(n2) em todos os casos."));
            document.add(new Paragraph(" - Complexidade de Espaco: O(1)."));
            document.add(new Paragraph(" - Notas: Sempre realiza o mesmo numero de comparacoes."));

            document.add(new Paragraph(" "));

            List<PdfPTable> tables = new ArrayList<>();
            List<Image> charts = new ArrayList<>();
            List<String> labels = new ArrayList<>();

            String[] tiposEntrada = {
                    "Crescente com repeticao", "Decrescente com repeticao", "Aleatorio com repeticao",
                    "Crescente sem repeticao", "Decrescente sem repeticao", "Aleatorio sem repeticao"
            };

            for (String tipo : tiposEntrada) {
                Map<String, XYSeries> seriesMap = new HashMap<>();
                seriesMap.put("BubbleSort", new XYSeries("BubbleSort"));
                seriesMap.put("MergeSort", new XYSeries("MergeSort"));
                seriesMap.put("QuickSort", new XYSeries("QuickSort"));
                seriesMap.put("HeapSort", new XYSeries("HeapSort"));
                seriesMap.put("InsertionSort", new XYSeries("InsertionSort"));
                seriesMap.put("SelectionSort", new XYSeries("SelectionSort"));

                Map<Integer, Map<String, Double>> tabelaTempos = new LinkedHashMap<>();

                /*for (int n : sizes) {
                    Map<String, int[]> todasAsEntradas = Entradas.gerarTodasEntradas(n);
                    int[] arrOriginal = todasAsEntradas.get(tipo);

                    tabelaTempos.put(n, new LinkedHashMap<>());

                    int[] arrBubble = arrOriginal.clone();
                    long startBubble = System.nanoTime();
                    BubbleSort.bubbleSort(arrBubble);
                    long endBubble = System.nanoTime();
                    double bubbleTime = (endBubble - startBubble) / 1_000_000.0;
                    seriesMap.get("BubbleSort").add(n, bubbleTime);
                    tabelaTempos.get(n).put("BubbleSort", bubbleTime);

                    int[] arrMerge = arrOriginal.clone();
                    long startMerge = System.nanoTime();
                    MergeSort.mergeSort(arrMerge);
                    long endMerge = System.nanoTime();
                    double mergeTime = (endMerge - startMerge) / 1_000_000.0;
                    seriesMap.get("MergeSort").add(n, mergeTime);
                    tabelaTempos.get(n).put("MergeSort", mergeTime);

                    int[] arrQuick = arrOriginal.clone();
                    long startQuick = System.nanoTime();
                    QuickSort.quickSort(arrQuick, 0, arrQuick.length - 1);
                    long endQuick = System.nanoTime();
                    double quickTime = (endQuick - startQuick) / 1_000_000.0;
                    seriesMap.get("QuickSort").add(n, quickTime);
                    tabelaTempos.get(n).put("QuickSort", quickTime);

                    int[] arrHeap = arrOriginal.clone();
                    long startHeap = System.nanoTime();
                    HeapSort.heapSort(arrHeap);
                    long endHeap = System.nanoTime();
                    double heapTime = (endHeap - startHeap) / 1_000_000.0;
                    seriesMap.get("HeapSort").add(n, heapTime);
                    tabelaTempos.get(n).put("HeapSort", heapTime);

                    int[] arrInsertion = arrOriginal.clone();
                    long startInsertion = System.nanoTime();
                    InsertionSort.insertionSort(arrInsertion);
                    long endInsertion = System.nanoTime();
                    double insertionTime = (endInsertion - startInsertion) / 1_000_000.0;
                    seriesMap.get("InsertionSort").add(n, insertionTime);
                    tabelaTempos.get(n).put("InsertionSort", insertionTime);

                    int[] arrSelect = arrOriginal.clone();
                    long startSelect = System.nanoTime();
                    SelectSort.selectSort(arrSelect);
                    long endSelect = System.nanoTime();
                    double selectTime = (endSelect - startSelect) / 1_000_000.0;
                    seriesMap.get("SelectionSort").add(n, selectTime);
                    tabelaTempos.get(n).put("SelectionSort", selectTime);
                }*/

                PdfPTable table = new PdfPTable(7);
                table.addCell("n");
                table.addCell("BubbleSort (ms)");
                table.addCell("InsertionSort (ms)");
                table.addCell("SelectionSort (ms)");
                table.addCell("MergeSort (ms)");
                table.addCell("QuickSort (ms)");
                table.addCell("HeapSort (ms)");

                for (Integer n : tabelaTempos.keySet()) {
                    table.addCell(String.valueOf(n));
                    table.addCell(String.format("%.2f", tabelaTempos.get(n).get("BubbleSort")));
                    table.addCell(String.format("%.2f", tabelaTempos.get(n).get("InsertionSort")));
                    table.addCell(String.format("%.2f", tabelaTempos.get(n).get("SelectionSort")));
                    table.addCell(String.format("%.2f", tabelaTempos.get(n).get("MergeSort")));
                    table.addCell(String.format("%.2f", tabelaTempos.get(n).get("QuickSort")));
                    table.addCell(String.format("%.2f", tabelaTempos.get(n).get("HeapSort")));
                }

                XYSeriesCollection dataset = new XYSeriesCollection();
                dataset.addSeries(seriesMap.get("BubbleSort"));
                dataset.addSeries(seriesMap.get("MergeSort"));
                dataset.addSeries(seriesMap.get("QuickSort"));
                dataset.addSeries(seriesMap.get("HeapSort"));
                dataset.addSeries(seriesMap.get("InsertionSort"));
                dataset.addSeries(seriesMap.get("SelectionSort"));

                JFreeChart chart = ChartFactory.createXYLineChart(
                        "Tempo de Execucao - " + tipo,
                        "Tamanho do Vetor (n)",
                        "Tempo (ms)",
                        dataset);

                ByteArrayOutputStream bas = new ByteArrayOutputStream();
                ChartUtils.writeChartAsPNG(bas, chart, 500, 300);
                Image chartImage = Image.getInstance(bas.toByteArray());

                tables.add(table);
                charts.add(chartImage);
                labels.add(tipo);
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Tabelas de Resultados", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD)));
            document.add(new Paragraph(" "));

            for (int i = 0; i < tables.size(); i++) {
                document.add(
                        new Paragraph("Tabela: " + labels.get(i), new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
                document.add(new Paragraph(" "));
                document.add(tables.get(i));
                document.add(new Paragraph(" "));
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Gráficos de Comparação", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD)));
            document.add(new Paragraph(" "));

            for (int i = 0; i < charts.size(); i++) {
                document.add(
                        new Paragraph("Gráfico: " + labels.get(i), new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
                document.add(new Paragraph(" "));
                document.add(charts.get(i));
                document.add(new Paragraph(" "));
            }

            document.close();
            System.out.println("Relatorio PDF criado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}