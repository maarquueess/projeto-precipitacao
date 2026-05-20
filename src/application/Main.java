package application;

import model.RegistroPrecipitacao;
import repository.LeitorArquivoCsv;
import service.EstatisticasPrecipitacaoService;

import java.time.Month;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando a leitura do arquivo CSV...\n");

        // 1. Instanciar o leitor
        LeitorArquivoCsv leitor = new LeitorArquivoCsv();

        // ATENÇÃO: O nome do arquivo deve ser EXATAMENTE igual ao que está na raiz do seu projeto.
        String caminhoArquivo = "PluviometriaFuncemeNormalizada_2026-05-19T21_02_25.csv";

        // 2. Carregar os dados na Collection (Lista)
        List<RegistroPrecipitacao> registros = leitor.lerRegistros(caminhoArquivo);

        if (registros.isEmpty()) {
            System.out.println("Nenhum registro encontrado ou erro ao ler o arquivo.");
            return;
        }

        // 3. Instanciar o serviço com os dados carregados em memória
        EstatisticasPrecipitacaoService service = new EstatisticasPrecipitacaoService(registros);

        // 4. Testando os cálculos pedidos no enunciado (Ano de 2025)
        int anoBusca = 2025;

        System.out.println("=================================================");
        System.out.println("   ESTATÍSTICAS PLUVIOMÉTRICAS - ACARAÚ (" + anoBusca + ")");
        System.out.println("=================================================\n");

        System.out.printf("1. Total precipitação em Janeiro/%d: %.2f mm\n", anoBusca,
                service.calcularTotalPrecipitacaoPorMes(anoBusca, Month.JANUARY));

        service.obterDiaMaiorPrecipitacao(anoBusca).ifPresent(r ->
                System.out.printf("2. Dia de MAIOR precipitação: %s (%.2f mm)\n", r.data(), r.valor()));

        service.obterDiaMenorPrecipitacao(anoBusca).ifPresent(r ->
                System.out.printf("3. Dia de MENOR precipitação: %s (%.2f mm)\n", r.data(), r.valor()));

        service.obterMesMaiorPrecipitacao(anoBusca).ifPresent(m ->
                System.out.println("4. Mês de MAIOR precipitação: " + m.name()));

        service.obterMesMenorPrecipitacao(anoBusca).ifPresent(m ->
                System.out.println("5. Mês de MENOR precipitação: " + m.name()));

        System.out.printf("6. Média de precipitação Anual: %.2f mm\n",
                service.calcularMediaPrecipitacaoAnual(anoBusca));

        System.out.printf("7. Média de precipitação em Janeiro/%d: %.2f mm\n", anoBusca,
                service.calcularMediaPrecipitacaoMensal(anoBusca, Month.JANUARY));

        System.out.println("\n8. Top 10 dias com MAIOR precipitação:");
        service.obterTop10DiasMaiorPrecipitacao(anoBusca).forEach(r ->
                System.out.printf("   - Data: %s | Valor: %.2f mm\n", r.data(), r.valor()));

        System.out.println("\n=================================================");
    }
}