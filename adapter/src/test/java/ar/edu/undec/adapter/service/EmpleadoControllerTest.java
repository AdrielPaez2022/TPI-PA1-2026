package ar.edu.undec.adapter.service;

import ar.edu.undec.adapter.handler.GlobalExceptionHandler;
import ar.edu.undec.adapter.service.empleado.controller.EmpleadoController;
import ar.edu.undec.adapter.service.empleado.mapper.EmpleadoRestMapper;
import empleado.exception.DatosEmpleadoInvalidosException;
import empleado.exception.EmpleadoNoEncontradoException;
import empleado.exception.LegajoDuplicadoException;
import empleado.input.BuscarEmpleadoPorLegajoUseCase;
import empleado.input.CrearEmpleadoUseCase;
import empleado.input.ListarEmpleadosUseCase;
import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpleadoController.class)
@Import({GlobalExceptionHandler.class, EmpleadoRestMapper.class})
class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrearEmpleadoUseCase crearEmpleado;

    @MockBean
    private BuscarEmpleadoPorLegajoUseCase buscarEmpleado;

    @MockBean
    private ListarEmpleadosUseCase listarEmpleados;

    @Test
    void postCreaEmpleadoYResponde201ConElRecurso() throws Exception {
        Empleado empleado = empleado();
        when(crearEmpleado.ejecutar(
                "Ana Perez", "ABC1234", LocalDate.of(2024, 1, 15), Puesto.ANALISTA,
                new BigDecimal("500000"))).thenReturn(empleado);

        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreCompleto": "Ana Perez",
                                  "legajo": "ABC1234",
                                  "fechaIngreso": "2024-01-15",
                                  "puesto": "ANALISTA",
                                  "salario": 500000
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(empleado.getId().toString()))
                .andExpect(jsonPath("$.legajo").value("ABC1234"));
    }

    @Test
    void getPorLegajoRetornaEmpleado() throws Exception {
        when(buscarEmpleado.ejecutar("ABC1234")).thenReturn(empleado());

        mockMvc.perform(get("/empleados/ABC1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto").value("Ana Perez"))
                .andExpect(jsonPath("$.puesto").value("ANALISTA"));
    }

    @Test
    void getPorLegajoInexistenteResponde404() throws Exception {
        when(buscarEmpleado.ejecutar("ABC1234"))
                .thenThrow(new EmpleadoNoEncontradoException("No existe el empleado"));

        mockMvc.perform(get("/empleados/ABC1234"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getListadoRetornaEmpleados() throws Exception {
        when(listarEmpleados.ejecutar()).thenReturn(List.of(empleado()));

        mockMvc.perform(get("/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].legajo").value("ABC1234"));
    }

    @Test
    void postConReglaDeDominioVioladaResponde400() throws Exception {
        when(crearEmpleado.ejecutar(
                "Ana Perez", "ABC1234", LocalDate.of(2024, 1, 15), Puesto.ANALISTA,
                new BigDecimal("500000")))
                .thenThrow(new DatosEmpleadoInvalidosException("Salario insuficiente"));

        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreCompleto": "Ana Perez",
                                  "legajo": "ABC1234",
                                  "fechaIngreso": "2024-01-15",
                                  "puesto": "ANALISTA",
                                  "salario": 500000
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void postConLegajoDuplicadoResponde409() throws Exception {
        when(crearEmpleado.ejecutar(
                "Ana Perez", "ABC1234", LocalDate.of(2024, 1, 15), Puesto.ANALISTA,
                new BigDecimal("500000")))
                .thenThrow(new LegajoDuplicadoException("El legajo ya existe"));

        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreCompleto": "Ana Perez",
                                  "legajo": "ABC1234",
                                  "fechaIngreso": "2024-01-15",
                                  "puesto": "ANALISTA",
                                  "salario": 500000
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void postConJsonInvalidoResponde400() throws Exception {
        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"legajo\":"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    private Empleado empleado() {
        return Empleado.crear("Ana Perez", "ABC1234", LocalDate.of(2024, 1, 15),
                Puesto.ANALISTA, new BigDecimal("500000"));
    }
}
