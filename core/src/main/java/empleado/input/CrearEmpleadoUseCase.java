package empleado.input;

import empleado.modelo.Empleado;
import empleado.modelo.Puesto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CrearEmpleadoUseCase {
    Empleado ejecutar(String nombreCompleto, String legajo, LocalDate fechaIngreso,
                      Puesto puesto, BigDecimal salario);
}
