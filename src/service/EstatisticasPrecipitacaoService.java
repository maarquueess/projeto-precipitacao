package service;

import model.RegistroPrecipitacao;

import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EstatisticasPrecipitacaoService {

    private final List<RegistroPrecipitacao> dados;

    // Recebe a lista de dados lida do CSV
    public EstatisticasPrecipitacaoService(List<RegistroPrecipitacao> dados) {
        this.dados = dados;
    }

    // 1. Total de precipitação para cada mês do ano
    public double calcularTotalPrecipitacaoPorMes(int ano, Month mes) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano && r.data().getMonth() == mes)
                .mapToDouble(RegistroPrecipitacao::valor)
                .sum();
    }

    // 2. Dia de maior precipitação no ano
    public Optional<RegistroPrecipitacao> obterDiaMaiorPrecipitacao(int ano) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano)
                .max(Comparator.comparing(RegistroPrecipitacao::valor));
    }

    // 3. Dia de menor precipitação no ano
    public Optional<RegistroPrecipitacao> obterDiaMenorPrecipitacao(int ano) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano)
                .min(Comparator.comparing(RegistroPrecipitacao::valor));
    }

    // 4. Mês de maior precipitação no ano
    public Optional<Month> obterMesMaiorPrecipitacao(int ano) {
        Map<Month, Double> somaPorMes = agruparESomarPorMes(ano);
        return somaPorMes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    // 5. Mês de menor precipitação no ano
    public Optional<Month> obterMesMenorPrecipitacao(int ano) {
        Map<Month, Double> somaPorMes = agruparESomarPorMes(ano);
        return somaPorMes.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    // 6. Média de precipitação do ano
    public double calcularMediaPrecipitacaoAnual(int ano) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano)
                .mapToDouble(RegistroPrecipitacao::valor)
                .average()
                .orElse(0.0);
    }

    // 7. Média da precipitação de cada mês do ano
    public double calcularMediaPrecipitacaoMensal(int ano, Month mes) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano && r.data().getMonth() == mes)
                .mapToDouble(RegistroPrecipitacao::valor)
                .average()
                .orElse(0.0);
    }

    // 8. Os 10 Dias de maior precipitação no ano
    public List<RegistroPrecipitacao> obterTop10DiasMaiorPrecipitacao(int ano) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano)
                .sorted(Comparator.comparing(RegistroPrecipitacao::valor).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    // Método auxiliar privado para agrupar as somas por mês (reúso de código)
    private Map<Month, Double> agruparESomarPorMes(int ano) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano)
                .collect(Collectors.groupingBy(
                        r -> r.data().getMonth(),
                        Collectors.summingDouble(RegistroPrecipitacao::valor)
                ));
    }
}