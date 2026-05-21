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

        try (Stream<String> linhas = Files.lines(Path.of(caminhoArquivo))) {
            linhas.skip(1) // Pula a primeira linha (cabeçalho do CSV)
                    .forEach(linha -> {
                        String[] campos = linha.split(";");

                        if (campos.length == 4) {
                            Long id = Long.parseLong(campos[0].trim());
                            Double valor = Double.parseDouble(campos[1].trim());
                            LocalDate data = LocalDate.parse(campos[2].trim());
                            Integer posto = Integer.parseInt(campos[3].trim());

                            registros.add(new RegistroPrecipitacao(id, valor, data, posto));
                        }
                    });
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        }

        return registros;
    }
}