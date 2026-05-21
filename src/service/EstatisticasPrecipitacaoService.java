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

    public EstatisticasPrecipitacaoService(List<RegistroPrecipitacao> dados) {
        this.dados = dados;
    }

    public double calcularTotalPrecipitacaoPorMes(int ano, Month mes) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano && r.data().getMonth() == mes)
                .mapToDouble(RegistroPrecipitacao::valor)
                .sum();
    }

    public Optional<RegistroPrecipitacao> obterDiaMaiorPrecipitacao(int ano) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano)
                .max(Comparator.comparing(RegistroPrecipitacao::valor));
    }

    public Optional<RegistroPrecipitacao> obterDiaMenorPrecipitacao(int ano) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano)
                .min(Comparator.comparing(RegistroPrecipitacao::valor));
    }

    public Optional<Month> obterMesMaiorPrecipitacao(int ano) {
        Map<Month, Double> somaPorMes = agruparESomarPorMes(ano);
        return somaPorMes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public Optional<Month> obterMesMenorPrecipitacao(int ano) {
        Map<Month, Double> somaPorMes = agruparESomarPorMes(ano);
        return somaPorMes.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public double calcularMediaPrecipitacaoAnual(int ano) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano)
                .mapToDouble(RegistroPrecipitacao::valor)
                .average()
                .orElse(0.0);
    }

    public double calcularMediaPrecipitacaoMensal(int ano, Month mes) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano && r.data().getMonth() == mes)
                .mapToDouble(RegistroPrecipitacao::valor)
                .average()
                .orElse(0.0);
    }

    public List<RegistroPrecipitacao> obterTop10DiasMaiorPrecipitacao(int ano) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano)
                .sorted(Comparator.comparing(RegistroPrecipitacao::valor).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    private Map<Month, Double> agruparESomarPorMes(int ano) {
        return dados.stream()
                .filter(r -> r.data().getYear() == ano)
                .collect(Collectors.groupingBy(
                        r -> r.data().getMonth(),
                        Collectors.summingDouble(RegistroPrecipitacao::valor)
                ));
    }
}