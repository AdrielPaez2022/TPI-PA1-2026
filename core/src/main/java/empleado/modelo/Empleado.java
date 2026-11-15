package empleado.modelo;

import empleado.exception.DatosEmpleadoInvalidosException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Pattern;

public final class Empleado {
    private static final Pattern FORMATO_LEGAJO = Pattern.compile("[A-Z]{3}[0-9]{4}");

    private final UUID id;
    private final String nombreCompleto;
    private final String legajo;
    private final LocalDate fechaIngreso;
    private final Puesto puesto;
    private final BigDecimal salario;

    private Empleado(UUID id, String nombreCompleto, String legajo, LocalDate fechaIngreso,
                     Puesto puesto, BigDecimal salario) {
        this.id = validarId(id);
        this.nombreCompleto = validarNombre(nombreCompleto);
        this.legajo = validarLegajo(legajo);
        this.fechaIngreso = validarFechaIngreso(fechaIngreso);
        if (puesto == null) {
            throw new DatosEmpleadoInvalidosException("El puesto es obligatorio");
        }
        this.puesto = puesto;
        this.salario = validarSalario(salario, puesto);
    }

    public static Empleado crear(String nombreCompleto, String legajo, LocalDate fechaIngreso,
                                 Puesto puesto, BigDecimal salario) {
        return reconstruir(UUID.randomUUID(), nombreCompleto, legajo, fechaIngreso, puesto, salario);
    }

    public static Empleado reconstruir(UUID id, String nombreCompleto, String legajo,
                                       LocalDate fechaIngreso, Puesto puesto, BigDecimal salario) {
        return new Empleado(id, nombreCompleto, legajo, fechaIngreso, puesto, salario);
    }

    private static UUID validarId(UUID id) {
        if (id == null) {
            throw new DatosEmpleadoInvalidosException("El identificador es obligatorio");
        }
        return id;
    }

    private static String validarNombre(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new DatosEmpleadoInvalidosException("El nombre completo es obligatorio");
        }
        return nombreCompleto.trim();
    }

    private static String validarLegajo(String legajo) {
        if (legajo == null || !FORMATO_LEGAJO.matcher(legajo).matches()) {
            throw new DatosEmpleadoInvalidosException(
                    "El legajo debe tener 3 letras mayúsculas seguidas de 4 números");
        }
        return legajo;
    }

    private static LocalDate validarFechaIngreso(LocalDate fechaIngreso) {
        if (fechaIngreso == null) {
            throw new DatosEmpleadoInvalidosException("La fecha de ingreso es obligatoria");
        }
        if (fechaIngreso.isAfter(LocalDate.now())) {
            throw new DatosEmpleadoInvalidosException("La fecha de ingreso no puede ser futura");
        }
        return fechaIngreso;
    }

    private static BigDecimal validarSalario(BigDecimal salario, Puesto puesto) {
        if (salario == null || salario.signum() <= 0) {
            throw new DatosEmpleadoInvalidosException("El salario debe ser mayor a cero");
        }
        if (salario.compareTo(puesto.getSalarioMinimo()) < 0) {
            throw new DatosEmpleadoInvalidosException(
                    "El salario mínimo para el puesto " + puesto + " es " + puesto.getSalarioMinimo());
        }
        return salario;
    }

    public UUID getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getLegajo() {
        return legajo;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public BigDecimal getSalario() {
        return salario;
    }
}
