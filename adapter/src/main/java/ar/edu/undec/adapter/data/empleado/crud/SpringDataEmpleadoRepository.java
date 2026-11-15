package ar.edu.undec.adapter.data.empleado.crud;

import ar.edu.undec.adapter.data.empleado.model.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataEmpleadoRepository extends JpaRepository<EmpleadoEntity, UUID> {
    boolean existsByLegajo(String legajo);

    Optional<EmpleadoEntity> findByLegajo(String legajo);
}
