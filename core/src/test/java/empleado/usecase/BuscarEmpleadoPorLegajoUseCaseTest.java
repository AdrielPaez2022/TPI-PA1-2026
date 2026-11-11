package empleado.usecase;

import empleado.exception.EmpleadoNoEncontradoException;
import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import empleado.output.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarEmpleadoPorLegajoUseCaseTest {

    @Mock
    private EmpleadoRepository repositorio;

    @InjectMocks
    private BuscarEmpleadoPorLegajoUseCaseImpl casoDeUso;

    @Test
    void retornaEmpleadoEncontrado() {
        Empleado esperado = Empleado.crear(
                "Ana Perez", "ABC1234", LocalDate.now(), Puesto.ANALISTA, new BigDecimal("500000"));
        when(repositorio.buscarPorLegajo("ABC1234")).thenReturn(Optional.of(esperado));

        assertEquals(esperado, casoDeUso.ejecutar("ABC1234"));
    }

    @Test
    void informaCuandoElEmpleadoNoExiste() {
        when(repositorio.buscarPorLegajo("ABC1234")).thenReturn(Optional.empty());

        assertThrows(EmpleadoNoEncontradoException.class, () -> casoDeUso.ejecutar("ABC1234"));
    }
}
