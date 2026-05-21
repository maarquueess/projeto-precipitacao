package repository;

import model.RegistroPrecipitacao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LeitorArquivoCsv {

    public List<RegistroPrecipitacao> lerRegistros(String caminhoArquivo) {
        List<RegistroPrecipitacao> registros = new ArrayList<>();

        // Lê o arquivo usando a API de NIO e Streams do Java
        try (Stream<String> linhas = Files.lines(Path.of(caminhoArquivo))) {
            linhas.skip(1) // Pula a primeira linha (cabeçalho do CSV)
                    .forEach(linha -> {
                        // O arquivo é separado por ponto e vírgula (;)
                        String[] campos = linha.split(";");

                        // Verifica se a linha tem exatamente 4 colunas (id;valor;data;posto)
                        if (campos.length == 4) {
                            Long id = Long.parseLong(campos[0].trim());
                            Double valor = Double.parseDouble(campos[1].trim());
                            LocalDate data = LocalDate.parse(campos[2].trim());
                            Integer posto = Integer.parseInt(campos[3].trim());

                            // Cria o objeto e adiciona na lista
                            registros.add(new RegistroPrecipitacao(id, valor, data, posto));
                        }
                    });
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        }

        return registros;
    }
}