package com.lta.sistemapagos.repository;

import com.lta.sistemapagos.entities.Estudiante;
import com.lta.sistemapagos.entities.Pago;
import com.lta.sistemapagos.enums.TypePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Pago findByEstudianteCodigo(String codigo);

    List<Pago> findByStatus(String status);

    List<Pago> findByType(TypePago typePago);
}
