import java.util.Locale;

final class Tabela {
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final String SEPARADOR = "+----------+----------------+--------------+--------------+-----------------+";
    private static final String FORMATO = "| %-8s | %14s | %12s | %12s | %15s |%n";

    private Tabela() {
    }

    static void cabecalho() {
        System.out.println(SEPARADOR);
        System.out.printf(FORMATO, "Execucao", "Valor Esperado", "Valor Obtido", "Perdidos", "Tempo Gasto");
        System.out.println(SEPARADOR);
    }

    static void linha(int execucao, int esperado, int obtido, double tempoMs) {
        System.out.printf(FORMATO,
                execucao,
                String.format(PT_BR, "%,d", esperado),
                String.format(PT_BR, "%,d", obtido),
                String.format(PT_BR, "%,d", esperado - obtido),
                String.format(PT_BR, "%,.2f ms", tempoMs));
    }

    static void rodape() {
        System.out.println(SEPARADOR);
    }
}
