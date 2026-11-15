package ar.edu.undec.adapter.data.empleado.mapper;

import ar.edu.undec.adapter.data.empleado.model.EmpleadoEntity;
import empleado.modelo.Empleado;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoPersistenceMapper {
    public EmpleadoEntity aEntidad(Empleado empleado) {
        return new EmpleadoEntity(
                empleado.getId(),
                empleado.getNombreCompleto(),
                empleado.getLegajo(),
                empleado.getFechaIngreso(),
                empleado.getPuesto(),
                empleado.getSalario()
        );
    }

    public Empleado aDominio(EmpleadoEntity entidad) {
        return Empleado.reconstruir(
                entidad.getId(),
                entidad.getNombreCompleto(),
                entidad.getLegajo(),
                entidad.getFechaIngreso(),
                entidad.getPuesto(),
                entidad.getSalario()
        );
    }
}
