package empleado.modelo;

import empleado.exception.DatosEmpleadoInvalidosException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmpleadoTest {

    @Test
    void creaEmpleadoValidoConIdentificador() {
        Empleado empleado = Empleado.crear(
                "Ana Perez", "ABC1234", LocalDate.now(), Puesto.ANALISTA,
                new BigDecimal("500000"));

        assertNotNull(empleado.getId());
        assertEquals("Ana Perez", empleado.getNombreCompleto());
        assertEquals("ABC1234", empleado.getLegajo());
    }

    @ParameterizedTest
    @CsvSource({
            "ANALISTA, 500000",
            "SUPERVISOR, 800000",
            "GERENTE, 1200000"
    })
    void aceptaElSalarioMinimoDelPuesto(Puesto puesto, String salario) {
        assertDoesNotThrow(() -> Empleado.crear(
                "Ana Perez", "ABC1234", LocalDate.now(), puesto, new BigDecimal(salario)));
    }

    @ParameterizedTest
    @CsvSource({
            "ANALISTA, 499999",
            "SUPERVISOR, 799999",
            "GERENTE, 1199999"
    })
    void rechazaSalarioInferiorAlMinimo(Puesto puesto, String salario) {
        assertThrows(DatosEmpleadoInvalidosException.class, () -> Empleado.crear(
                "Ana Perez", "ABC1234", LocalDate.now(), puesto, new BigDecimal(salario)));
    }

    @ParameterizedTest
    @CsvSource({"abc1234", "AB12345", "ABCD123", "ABC123", "ABC12345"})
    void rechazaLegajoConFormatoInvalido(String legajo) {
        assertThrows(DatosEmpleadoInvalidosException.class, () -> Empleado.crear(
                "Ana Perez", legajo, LocalDate.now(), Puesto.ANALISTA, new BigDecimal("500000")));
    }

    @Test
    void rechazaFechaDeIngresoFutura() {
        assertThrows(DatosEmpleadoInvalidosException.class, () -> Empleado.crear(
                "Ana Perez", "ABC1234", LocalDate.now().plusDays(1), Puesto.ANALISTA,
                new BigDecimal("500000")));
    }

    @Test
    void rechazaAtributosObligatoriosNulosYNombreVacio() {
        assertAll(
                () -> assertThrows(DatosEmpleadoInvalidosException.class, () -> Empleado.crear(
                        null, "ABC1234", LocalDate.now(), Puesto.ANALISTA, new BigDecimal("500000"))),
                () -> assertThrows(DatosEmpleadoInvalidosException.class, () -> Empleado.crear(
                        " ", "ABC1234", LocalDate.now(), Puesto.ANALISTA, new BigDecimal("500000"))),
                () -> assertThrows(DatosEmpleadoInvalidosException.class, () -> Empleado.crear(
                        "Ana Perez", null, LocalDate.now(), Puesto.ANALISTA, new BigDecimal("500000"))),
                () -> assertThrows(DatosEmpleadoInvalidosException.class, () -> Empleado.crear(
                        "Ana Perez", "ABC1234", null, Puesto.ANALISTA, new BigDecimal("500000"))),
                () -> assertThrows(DatosEmpleadoInvalidosException.class, () -> Empleado.crear(
                        "Ana Perez", "ABC1234", LocalDate.now(), null, new BigDecimal("500000"))),
                () -> assertThrows(DatosEmpleadoInvalidosException.class, () -> Empleado.crear(
                        "Ana Perez", "ABC1234", LocalDate.now(), Puesto.ANALISTA, null))
        );
    }

    @Test
    void rechazaSalarioCeroONegativo() {
        assertAll(
                () -> assertThrows(DatosEmpleadoInvalidosException.class, () -> Empleado.crear(
                        "Ana Perez", "ABC1234", LocalDate.now(), Puesto.ANALISTA, BigDecimal.ZERO)),
                () -> assertThrows(DatosEmpleadoInvalidosException.class, () -> Empleado.crear(
                        "Ana Perez", "ABC1234", LocalDate.now(), Puesto.ANALISTA, new BigDecimal("-1")))
        );
    }
}
