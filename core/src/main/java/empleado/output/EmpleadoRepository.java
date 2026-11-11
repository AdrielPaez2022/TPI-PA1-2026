package empleado.output;

import empleado.modelo.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository {
    boolean existePorLegajo(String legajo);

    Empleado guardar(Empleado empleado);

    Optional<Empleado> buscarPorLegajo(String legajo);

    List<Empleado> listarTodos();
}
