package empleado.exception;

public class EmpleadoNoEncontradoException extends RuntimeException {
    public EmpleadoNoEncontradoException(String legajo) {
        super("No se encontró un empleado con el legajo " + legajo);
    }
}
