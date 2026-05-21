package model;
import java.time.LocalDate;

public record RegistroPrecipitacao(Long id, Double valor, LocalDate data, Integer posto) {}