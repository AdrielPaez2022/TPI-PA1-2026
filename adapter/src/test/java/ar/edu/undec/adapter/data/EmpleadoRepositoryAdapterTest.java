package ar.edu.undec.adapter.data;

import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import empleado.output.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmpleadoRepositoryAdapterTest {

    @Autowired
    private EmpleadoRepository repositorio;

    @Test
    void guardaYRecuperaEmpleadoPorLegajo() {
        Empleado original = Empleado.crear("Ana Perez", "XYZ9876", LocalDate.of(2024, 1, 15),
                Puesto.SUPERVISOR, new BigDecimal("800000"));

        Empleado guardado = repositorio.guardar(original);
        Empleado recuperado = repositorio.buscarPorLegajo("XYZ9876").orElseThrow();

        assertEquals(original.getId(), guardado.getId());
        assertEquals(original.getId(), recuperado.getId());
        assertEquals(original.getNombreCompleto(), recuperado.getNombreCompleto());
        assertEquals(original.getFechaIngreso(), recuperado.getFechaIngreso());
        assertEquals(original.getPuesto(), recuperado.getPuesto());
        assertEquals(0, original.getSalario().compareTo(recuperado.getSalario()));
        assertTrue(repositorio.existePorLegajo("XYZ9876"));
        assertTrue(repositorio.listarTodos().stream()
                .anyMatch(empleado -> empleado.getLegajo().equals("XYZ9876")));
    }
}
