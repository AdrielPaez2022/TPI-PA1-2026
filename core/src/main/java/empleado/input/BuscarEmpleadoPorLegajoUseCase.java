package empleado.input;

import empleado.modelo.Empleado;

public interface BuscarEmpleadoPorLegajoUseCase {
    Empleado ejecutar(String legajo);
}
