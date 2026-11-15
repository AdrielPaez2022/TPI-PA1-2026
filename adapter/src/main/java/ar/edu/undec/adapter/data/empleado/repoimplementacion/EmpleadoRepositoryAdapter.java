package ar.edu.undec.adapter.data.empleado.repoimplementacion;

import ar.edu.undec.adapter.data.empleado.crud.SpringDataEmpleadoRepository;
import ar.edu.undec.adapter.data.empleado.mapper.EmpleadoPersistenceMapper;
import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmpleadoRepositoryAdapter implements EmpleadoRepository {
    private final SpringDataEmpleadoRepository repositorioJpa;
    private final EmpleadoPersistenceMapper mapper;

    public EmpleadoRepositoryAdapter(SpringDataEmpleadoRepository repositorioJpa,
                                     EmpleadoPersistenceMapper mapper) {
        this.repositorioJpa = repositorioJpa;
        this.mapper = mapper;
    }

    @Override
    public boolean existePorLegajo(String legajo) {
        return repositorioJpa.existsByLegajo(legajo);
    }

    @Override
    public Empleado guardar(Empleado empleado) {
        return mapper.aDominio(repositorioJpa.save(mapper.aEntidad(empleado)));
    }

    @Override
    public Optional<Empleado> buscarPorLegajo(String legajo) {
        return repositorioJpa.findByLegajo(legajo).map(mapper::aDominio);
    }

    @Override
    public List<Empleado> listarTodos() {
        return repositorioJpa.findAll().stream()
                .map(mapper::aDominio)
                .toList();
    }
}
