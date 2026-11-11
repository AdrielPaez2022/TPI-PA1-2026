package empleado.usecase;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarEmpleadosUseCaseTest {

    @Mock
    private EmpleadoRepository repositorio;

    @InjectMocks
    private ListarEmpleadosUseCaseImpl casoDeUso;

    @Test
    void retornaTodosLosEmpleados() {
        List<Empleado> empleados = List.of(Empleado.crear(
                "Ana Perez", "ABC1234", LocalDate.now(), Puesto.ANALISTA, new BigDecimal("500000")));
        when(repositorio.listarTodos()).thenReturn(empleados);

        assertEquals(empleados, casoDeUso.ejecutar());
    }
}
