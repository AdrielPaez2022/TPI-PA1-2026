package empleado.usecase;

import empleado.exception.EmpleadoNoEncontradoException;
import empleado.input.BuscarEmpleadoPorLegajoUseCase;
import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepository;

import java.util.Objects;

public final class BuscarEmpleadoPorLegajoUseCaseImpl implements BuscarEmpleadoPorLegajoUseCase {
    private final EmpleadoRepository repositorio;

    public BuscarEmpleadoPorLegajoUseCaseImpl(EmpleadoRepository repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio);
    }

    @Override
    public Empleado ejecutar(String legajo) {
        return repositorio.buscarPorLegajo(legajo)
                .orElseThrow(() -> new EmpleadoNoEncontradoException(legajo));
    }
}
