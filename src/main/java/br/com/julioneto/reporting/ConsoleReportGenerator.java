package br.com.julioneto.reporting;

import br.com.julioneto.contract.ReportGenerator;
import br.com.julioneto.domain.Grafo;
import br.com.julioneto.domain.Link;

import java.util.List;

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

        List<Link> brokenLinks = grafo.getLinksQuebrados();
        if (!brokenLinks.isEmpty()) {
            System.out.println("\n--- Links Quebrados (" + brokenLinks.size() + ") ---");
            for (Link link : brokenLinks) {
                System.out.println("URL: " + link.getUrl() + ", Status: " + link.getStatusCode());
            }
        } else {
            System.out.println("\nNenhum link quebrado encontrado.");
        }
    }
}
