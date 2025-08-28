package br.com.julioneto.reporting;

import br.com.julioneto.contract.ReportGenerator;
import br.com.julioneto.domain.Grafo;
import br.com.julioneto.domain.Link;

import java.util.List;
import java.util.Map;

/**
 * Implementação de ReportGenerator que exibe o relatório no console.
 *
 * @author Julio Neto
 * @version 0.0.1
 * @since 0.0.1
 */
public class ConsoleReportGenerator implements ReportGenerator {
    @Override
    public void generate(Grafo grafo) {
        System.out.println("\n--- Relatório Final ---");
        System.out.println("Total de páginas processadas: " + grafo.getLinks().size());

        Map<Integer, List<Link>> detailedBrokenLinks = grafo.getLinksQuebradosPorStatusCode();
        int totalLinks = 0;

        if (!detailedBrokenLinks.isEmpty()) {
            System.out.println("\n--- Links Quebrados por Status ---");
            for (Map.Entry<Integer, List<Link>> entry : detailedBrokenLinks.entrySet()) {
                System.out.println("\nStatus: " + entry.getKey() + " | Total: " + entry.getValue().size() + "\n");
                totalLinks += entry.getValue().size();
                for (Link link : entry.getValue()) {
                    System.out.println("URL: " + link.getUrl());
                }
            }
            System.out.println("\nTotal de links quebrados: " + totalLinks);
        } else {
            System.out.println("\nNenhum link quebrado encontrado.");
        }

        System.out.println("Depth máximo: " + Grafo.getMaxDepth());
    }
}
