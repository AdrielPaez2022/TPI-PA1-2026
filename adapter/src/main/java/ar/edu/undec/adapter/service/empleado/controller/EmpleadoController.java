package ar.edu.undec.adapter.service.empleado.controller;

import ar.edu.undec.adapter.service.empleado.mapper.EmpleadoRestMapper;
import ar.edu.undec.adapter.service.empleado.model.CrearEmpleadoRequest;
import ar.edu.undec.adapter.service.empleado.model.EmpleadoResponse;
import empleado.input.BuscarEmpleadoPorLegajoUseCase;
import empleado.input.CrearEmpleadoUseCase;
import empleado.input.ListarEmpleadosUseCase;
import empleado.modelo.Empleado;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {
    private final CrearEmpleadoUseCase crearEmpleado;
    private final BuscarEmpleadoPorLegajoUseCase buscarEmpleado;
    private final ListarEmpleadosUseCase listarEmpleados;
    private final EmpleadoRestMapper mapper;

    public EmpleadoController(CrearEmpleadoUseCase crearEmpleado,
                              BuscarEmpleadoPorLegajoUseCase buscarEmpleado,
                              ListarEmpleadosUseCase listarEmpleados,
                              EmpleadoRestMapper mapper) {
        this.crearEmpleado = crearEmpleado;
        this.buscarEmpleado = buscarEmpleado;
        this.listarEmpleados = listarEmpleados;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@RequestBody CrearEmpleadoRequest request) {
        Empleado empleado = crearEmpleado.ejecutar(
                request.nombreCompleto(),
                request.legajo(),
                request.fechaIngreso(),
                request.puesto(),
                request.salario()
        );
        EmpleadoResponse response = mapper.aRespuesta(empleado);
        return ResponseEntity.created(URI.create("/empleados/" + response.legajo())).body(response);
    }

    @GetMapping("/{legajo}")
    public EmpleadoResponse buscarPorLegajo(@PathVariable("legajo") String legajo) {
        return mapper.aRespuesta(buscarEmpleado.ejecutar(legajo));
    }

    @GetMapping
    public List<EmpleadoResponse> listar() {
        return listarEmpleados.ejecutar().stream()
                .map(mapper::aRespuesta)
                .toList();
    }
}
