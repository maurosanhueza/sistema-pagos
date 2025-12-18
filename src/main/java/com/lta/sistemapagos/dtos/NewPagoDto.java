package com.lta.sistemapagos.dtos;

import com.lta.sistemapagos.enums.TypePago;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPagoDto {

    private double cantidad;
    private TypePago typePago;
    private LocalDate date;
    private String codigoEstudiante;
}
