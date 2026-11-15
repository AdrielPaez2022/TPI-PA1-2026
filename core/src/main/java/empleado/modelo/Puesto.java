package empleado.modelo;

import java.math.BigDecimal;

public enum Puesto {
    ANALISTA(new BigDecimal("500000")),
    SUPERVISOR(new BigDecimal("800000")),
    GERENTE(new BigDecimal("1200000"));

    private final BigDecimal salarioMinimo;

    Puesto(BigDecimal salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }

    public BigDecimal getSalarioMinimo() {
        return salarioMinimo;
    }
}
