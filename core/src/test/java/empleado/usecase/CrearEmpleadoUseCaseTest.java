package empleado.usecase;

import empleado.exception.LegajoDuplicadoException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearEmpleadoUseCaseTest {

    @Mock
    private EmpleadoRepository repositorio;

    @InjectMocks
    private CrearEmpleadoUseCaseImpl casoDeUso;

    @Test
    void creaYGuardaEmpleadoCuandoElLegajoEstaDisponible() {
        Empleado empleadoGuardado = Empleado.crear(
                "Ana Perez", "ABC1234", LocalDate.now(), Puesto.ANALISTA, new BigDecimal("500000"));
        when(repositorio.existePorLegajo("ABC1234")).thenReturn(false);
        when(repositorio.guardar(any(Empleado.class))).thenReturn(empleadoGuardado);

        Empleado resultado = casoDeUso.ejecutar(
                "Ana Perez", "ABC1234", LocalDate.now(), Puesto.ANALISTA, new BigDecimal("500000"));

        assertEquals(empleadoGuardado, resultado);
        verify(repositorio).guardar(any(Empleado.class));
    }

    @Test
    void rechazaLegajoDuplicadoSinGuardar() {
        when(repositorio.existePorLegajo("ABC1234")).thenReturn(true);

        assertThrows(LegajoDuplicadoException.class, () -> casoDeUso.ejecutar(
                "Ana Perez", "ABC1234", LocalDate.now(), Puesto.ANALISTA, new BigDecimal("500000")));

        verify(repositorio, never()).guardar(any());
    }
}
