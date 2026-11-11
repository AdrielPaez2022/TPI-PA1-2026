package empleado.usecase;

import empleado.exception.LegajoDuplicadoException;
import empleado.input.CrearEmpleadoUseCase;
import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import empleado.output.EmpleadoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public final class CrearEmpleadoUseCaseImpl implements CrearEmpleadoUseCase {
    private final EmpleadoRepository repositorio;

    public CrearEmpleadoUseCaseImpl(EmpleadoRepository repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio);
    }

    @Override
    public Empleado ejecutar(String nombreCompleto, String legajo, LocalDate fechaIngreso,
                             Puesto puesto, BigDecimal salario) {
        Empleado empleado = Empleado.crear(nombreCompleto, legajo, fechaIngreso, puesto, salario);
        if (repositorio.existePorLegajo(empleado.getLegajo())) {
            throw new LegajoDuplicadoException(empleado.getLegajo());
        }
        return repositorio.guardar(empleado);
    }
}
