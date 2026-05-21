package application;

import model.RegistroPrecipitacao;
import repository.LeitorArquivoCsv;
import service.EstatisticasPrecipitacaoService;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando a leitura do arquivo CSV...\n");

        LeitorArquivoCsv leitor = new LeitorArquivoCsv();

        String caminhoArquivo = "PluviometriaFuncemeNormalizada_2026-05-19T21_02_25.csv";

        List<RegistroPrecipitacao> registros = leitor.lerRegistros(caminhoArquivo);

        if (registros.isEmpty()) {
            System.out.println("Nenhum registro encontrado ou erro ao ler o arquivo.");
            return;
        }

        EstatisticasPrecipitacaoService service = new EstatisticasPrecipitacaoService(registros);
        int anoBusca = 2025;

        Locale ptBR = new Locale("pt", "BR");

        System.out.println("=========================================================");
        System.out.println("       ESTATÍSTICAS PLUVIOMÉTRICAS - ACARAÚ (" + anoBusca + ")");
        System.out.println("=========================================================\n");

        String nomeMesJaneiro = Month.JANUARY.getDisplayName(TextStyle.FULL, ptBR);
        nomeMesJaneiro = nomeMesJaneiro.substring(0, 1).toUpperCase() + nomeMesJaneiro.substring(1);

        System.out.printf("1. Total precipitação em %s/%d: %.2f mm\n", nomeMesJaneiro, anoBusca,
                service.calcularTotalPrecipitacaoPorMes(anoBusca, Month.JANUARY));

        service.obterDiaMaiorPrecipitacao(anoBusca).ifPresent(r ->
                System.out.printf("2. Dia de MAIOR precipitação: %s (%.2f mm)\n", r.data(), r.valor()));

        service.obterDiaMenorPrecipitacao(anoBusca).ifPresent(r ->
                System.out.printf("3. Dia de MENOR precipitação: %s (%.2f mm)\n", r.data(), r.valor()));

        service.obterMesMaiorPrecipitacao(anoBusca).ifPresent(m -> {
            String mesTraduzido = m.getDisplayName(TextStyle.FULL, ptBR).toUpperCase();
            System.out.println("4. Mês de MAIOR precipitação: " + mesTraduzido);
        });

        service.obterMesMenorPrecipitacao(anoBusca).ifPresent(m -> {
            String mesTraduzido = m.getDisplayName(TextStyle.FULL, ptBR).toUpperCase();
            System.out.println("5. Mês de MENOR precipitação: " + mesTraduzido);
        });

        System.out.printf("6. Média de precipitação Anual: %.2f mm\n",
                service.calcularMediaPrecipitacaoAnual(anoBusca));

        System.out.printf("7. Média de precipitação em %s/%d: %.2f mm\n", nomeMesJaneiro, anoBusca,
                service.calcularMediaPrecipitacaoMensal(anoBusca, Month.JANUARY));

        System.out.println("\n=========================================================");
        System.out.println("   RANKING: OS 10 DIAS DE MAIOR PRECIPITAÇÃO NO ANO");
        System.out.println("=========================================================\n");

        List<RegistroPrecipitacao> top10 = service.obterTop10DiasMaiorPrecipitacao(anoBusca);
        for (int i = 0; i < top10.size(); i++) {
            RegistroPrecipitacao r = top10.get(i);
            System.out.printf("   [%02d] Data: %s | Valor: %.2f mm\n", (i + 1), r.data(), r.valor());
        }

        System.out.println("\n=========================================================");
    }
}