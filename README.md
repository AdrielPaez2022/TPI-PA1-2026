# Trabajo Práctico Integrador
## Ing. y Lic. en Sistemas
### Universidad Nacional de Chilecito
#### Asignatura: Programación Avanzada I

# Módulo Recursos Humanos: Microservicio de Gestión de Empleados

## Objetivo

Diseñar e implementar un microservicio completo para la gestión de empleados aplicando **Clean Architecture (Hexagonal)**, **Principios SOLID** y **Test-Driven Development (TDD)** en todas sus capas. 

Se evaluará la separación estricta entre el dominio puro y los adaptadores de infraestructura (web y persistencia), la calidad del diseño, la cobertura de pruebas unitarias e integrales, y un historial de Git que evidencie que el desarrollo estuvo guiado por tests escritos con anterioridad a la implementación.

---

## Consigna

El sistema debe permitir dar de alta y consultar empleados en el departamento de Recursos Humanos a través de una API REST, persistiendo la información en una base de datos relacional.

El proyecto es multi-módulo Maven (`core` y `adapter`):
- **`core`**: contiene la lógica de negocio pura, entidades y casos de uso, completamente desacoplados de frameworks.
- **`adapter`**: contiene los adaptadores de entrada (API REST con Spring Boot) y de salida (persistencia relacional con Spring Data JPA).

---

### 1. Dominio y Reglas de Negocio (Módulo `core`)

La entidad `Empleado` modela a los colaboradores y contiene los siguientes datos:
- Identificador único (`UUID`).
- Nombre completo.
- Legajo.
- Fecha de ingreso.
- Puesto (categorías posibles: `ANALISTA`, `SUPERVISOR`, `GERENTE`).
- Salario.

La creación de instancias debe realizarse a través de un factory method de dominio. La entidad debe garantizar su validez aplicando las siguientes reglas:

- Todos los atributos son obligatorios.
- La fecha de ingreso no puede ser posterior a la fecha actual.
- El legajo debe responder al formato institucional: 3 letras mayúsculas seguidas de 4 números (ej. `ABC1234`).
- El salario debe ser mayor a cero y respetar los pisos salariales según la jerarquía:
  - Analista: mínimo $500.000
  - Supervisor: mínimo $800.000
  - Gerente: mínimo $1.200.000

Cualquier infracción a estas reglas debe ser informada mediante excepciones de dominio semánticas.

---

### 2. Casos de Uso (Módulo `core`)

El núcleo de la aplicación debe resolver dos operaciones:

1. **Crear Empleado**:
   - Coordina la creación válida del colaborador.
   - Verifica a través del puerto de salida que no exista otro empleado con el mismo legajo. De existir duplicado, debe interrumpir el flujo informando el conflicto.
   - Persiste el empleado a través del puerto correspondiente y lo retorna.

2. **Buscar Empleado por Legajo**:
   - Consulta al repositorio por el legajo solicitado.
   - Si no existe, debe lanzar una excepción de negocio que notifique la ausencia del recurso.
   - Si existe, retorna el empleado.

---

### 3. Adaptadores de Infraestructura (Módulo `adapter`)

#### Adaptador Web (Entrada)
Implementar los endpoints HTTP correspondientes:
- **`POST /empleados`**: recibe los datos del empleado, invoca al caso de uso de creación y responde con código HTTP 201 Created (o 200) y el recurso registrado.
- **`GET /empleados/{legajo}`**: busca por legajo. Si se encuentra retorna código 200 con los datos; si no existe, debe traducirse a un código **404 Not Found**.
- **`GET /empleados`**: retorna el listado de empleados registrados.
- **Manejo de Errores**: implementar un manejador global de excepciones (`@ControllerAdvice`) que capture las excepciones de dominio y casos de uso, respondiendo con los códigos de estado HTTP adecuados (400 para reglas violadas, 404 para búsquedas fallidas, 409 para duplicados).

#### Adaptador de Persistencia (Salida)
- Implementar el puerto de repositorio definido en el `core` utilizando **Spring Data JPA**.
- Modelar la entidad relacional de base de datos separada de la entidad de dominio, junto con los mappers bidireccionales pertinentes.
- La base de datos debe ser PostgreSQL (o H2 en memoria para pruebas locales).

---

## Arquitectura

El proyecto debe respetar la estructura de paquetes y la dirección de las dependencias:

```text
backend/
├── core/
│   └── src/main/java/empleado/
│       ├── modelo/          # Entidad de dominio, Enums
│       ├── exception/       # Excepciones de negocio y aplicación
│       ├── input/           # Puertos de entrada (interfaces de casos de uso / DTOs)
│       ├── output/          # Puertos de salida (contratos de persistencia)
│       └── usecase/         # Implementación de casos de uso
│
└── adapter/
    └── src/main/java/.../adapter/
        ├── handler/         # ControllerAdvice y manejo global HTTP
        ├── service/         # Adaptador Web (Controllers, DTOs REST, Mappers, BeanConfig)
        └── data/            # Adaptador Persistencia (Entidades JPA, Spring Data Crud, Mappers, Implementación de Puertos)
```

**Regla de oro de la arquitectura**: El módulo `core` no conoce a Spring ni a ninguna librería de infraestructura; `adapter` depende de `core` y nunca al revés.

---

## Proceso de TDD y Pruebas Requeridas

Se exige la aplicación estricta del ciclo Red-Green-Refactor, demostrable en el árbol de Git:

1. **Pruebas en `core`**:
   - Tests unitarios de la entidad `Empleado`: casos válidos, límites salariales, formatos y fechas inválidas.
   - Tests unitarios de casos de uso: utilizando dobles de prueba (`Mockito`) para aislar los puertos de persistencia (éxito, legajo repetido, entidad no encontrada).
2. **Pruebas en `adapter`**:
   - Tests de controladores (`MockMvc`): verificar rutas, deserialización de requests, respuestas JSON y códigos de estado HTTP ante éxitos y fallas.
   - Tests de persistencia: verificar el correcto guardado y recuperación de entidades contra la base de datos a través de la implementación del repositorio.

El historial de commits debe evidenciar la alternancia: primero commits de pruebas (`test: ...`), seguidos por commits de código de producción (`feat: ...`).

---

## Condiciones de Entrega

- **Fecha límite improrrogable**: **Viernes 13/11/2026 a las 18:00 hs**.
- Trabajar en una rama `feature/tpi-integrador`.
- Abrir un único **Pull Request** desde `feature/tpi-integrador` hacia `main`.
- La descripción del PR debe incluir:
  - Justificación de decisiones de diseño adoptadas en dominio y adaptadores.
  - Mapeo de excepciones de dominio a códigos de respuesta HTTP.
  - Breve reseña de cómo el historial de commits demuestra la práctica de TDD.
- **Higiene del Repositorio y `.gitignore`**: Está terminantemente prohibido versionar y subir archivos de configuración de entornos IDE (`.idea/`, `.vscode/`, `*.iml`), directorios de compilación (`target/`), binarios (`*.class`, `*.jar`) o archivos del sistema operativo (`.DS_Store`). Todo debe gestionarse a través del archivo `.gitignore`.

> ⚠️ **ATENCIÓN - REGLA OBLIGATORIA SOBRE EL PULL REQUEST:**
> **BAJO NINGÚN CONCEPTO DEBEN MERGEAR NI CERRAR EL PULL REQUEST HACIA `main`.**
> El Pull Request debe permanecer **ABIERTO** hasta que el equipo docente realice y finalice la corrección formal del trabajo. 
> **El cierre o merge del PR previo a la evaluación implicará descuento directo de puntos sobre la nota final del examen.**

---

## Instancia de Defensa Oral

- **Modalidad**: Oral e individual.
- **Fecha**: **Martes 17/11/2026 en horario de clases**.
- **Condición**: Los turnos de defensa serán concedidos exclusivamente a quienes hayan presentado el integrador en tiempo y forma.
- **Cronograma**: El orden y la distribución horaria de los turnos se anunciarán oportunamente a través de los canales habituales de la asignatura.

---

## Criterios de Evaluación

1. **Metodología TDD (Eliminatorio)**: presencia verificable de pruebas unitarias e integrales escritas previamente a las implementaciones correspondientes.
2. **Clean Architecture y SOLID**: aislamiento absoluto del módulo `core`, inversión de dependencias efectiva mediante contratos y encapsulamiento de reglas en la entidad.
3. **Robustez y Cobertura de Tests**: pruebas unitarias de dominio, pruebas de casos de uso con mocks, pruebas de controladores REST y pruebas de persistencia.
4. **Manejo Semántico de Errores**: diseño limpio de excepciones y su correcta traducción a la capa web.
5. **Cumplimiento del Protocolo de Entrega**: PR abierto en tiempo y forma respetando la prohibición de merge previo.
6. **Higiene y Limpieza del Repositorio (`.gitignore`)**: ausencia absoluta de artefactos compilados (`target/`), configuraciones de IDEs (`.idea/`, `.vscode/`, `*.iml`), archivos de clase (`.class`) o temporales. El repositorio debe contener únicamente código fuente y recursos necesarios. La inclusión de estos archivos restará puntaje de evaluación.
7. **Defensa Oral**: solidez técnica, capacidad de fundamentación de decisiones de diseño tomadas y justificación del código frente al docente.
8. **Compilación y Ejecución**: el proyecto debe compilar y superar el 100% de los tests mediante `mvn test` o `mvn verify`.
