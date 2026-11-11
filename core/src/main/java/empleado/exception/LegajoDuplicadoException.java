package empleado.exception;

public class LegajoDuplicadoException extends RuntimeException {
    public LegajoDuplicadoException(String legajo) {
        super("Ya existe un empleado con el legajo " + legajo);
    }
}
