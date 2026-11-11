package ar.edu.undec.adapter.service.empleado.beanconfig;

import empleado.input.BuscarEmpleadoPorLegajoUseCase;
import empleado.input.CrearEmpleadoUseCase;
import empleado.input.ListarEmpleadosUseCase;
import empleado.output.EmpleadoRepository;
import empleado.usecase.BuscarEmpleadoPorLegajoUseCaseImpl;
import empleado.usecase.CrearEmpleadoUseCaseImpl;
import empleado.usecase.ListarEmpleadosUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public CrearEmpleadoUseCase crearEmpleadoUseCase(EmpleadoRepository repositorio) {
        return new CrearEmpleadoUseCaseImpl(repositorio);
    }

    @Bean
    public BuscarEmpleadoPorLegajoUseCase buscarEmpleadoPorLegajoUseCase(EmpleadoRepository repositorio) {
        return new BuscarEmpleadoPorLegajoUseCaseImpl(repositorio);
    }

    @Bean
    public ListarEmpleadosUseCase listarEmpleadosUseCase(EmpleadoRepository repositorio) {
        return new ListarEmpleadosUseCaseImpl(repositorio);
    }
}
