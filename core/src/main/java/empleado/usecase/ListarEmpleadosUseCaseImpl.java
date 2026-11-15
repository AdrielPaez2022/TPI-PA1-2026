package empleado.usecase;

import empleado.input.ListarEmpleadosUseCase;
import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepository;

import java.util.List;
import java.util.Objects;

public final class ListarEmpleadosUseCaseImpl implements ListarEmpleadosUseCase {
    private final EmpleadoRepository repositorio;

    public ListarEmpleadosUseCaseImpl(EmpleadoRepository repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio);
    }

    @Override
    public List<Empleado> ejecutar() {
        return repositorio.listarTodos();
    }
}
