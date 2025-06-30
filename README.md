# PM2E1390415 - Examen Primer Parcial de Programación Móvil I

Este proyecto corresponde al examen del primer parcial de la asignatura Programación Móvil I (PM01), desarrollado en UCENM bajo la dirección del Ing. Ricardo Enrique Lagos Mendoza.


## Integrantes

| Nombre del integrante | Número de cuenta | Función principal                            |
|-----------------------|------------------|----------------------------------------------|
| Said Ortiz          | 123020004        | Base de datos y modelos                      |
| Enrique Romero          | 219090015        | Interfaz gráfica y diseño de layouts         |
| Carlos Espinoza     | 122590039          | Lógica general, integración, validaciones    |
## Descripción

Una aplicación Android que permite:

- Guardar contactos en una base de datos SQLite
- Ver, editar, eliminar y compartir contactos
- Llamar a los contactos desde la app
- Ver imagen y nota guardada en un contacto
- Seleccionar imagen desde la galería
- Importar contactos del dispositivo
- Aplicar validación con expresiones regulares
- Buscar contactos por nombre en la lista

## Tecnologías

- Java
- Android SDK
- SQLite
- RecyclerView
- AlertDialog
- Expresiones regulares
- ContentProvider

## Funcionalidades Implementadas

### Pantalla Inicial
- Guardar contactos (país, nombre, teléfono, nota, imagen) en SQLite
- Validaciones de campos vacíos y regex en teléfono
- Selección de imagen y copia persistente en almacenamiento interno
- Importar contacto desde la app de contactos del sistema

### Pantalla de Lista
- Mostrar lista de contactos con RecyclerView
- Borrar contacto con confirmación
- Compartir contacto por intent
- Ver nota y ver imagen (AlertDialog personalizado)
- Llamar contacto con `ACTION_CALL`
- Buscar por nombre (EditText buscador)

### Extras
- Orden alfabético de contactos
- Iconos personalizados para acciones
- Selección automática de país al importar (según prefijo telefónico)

## Requisitos del Examen

| Requisito                                                        | Cumplido |
|------------------------------------------------------------------|----------|
| Guardar contactos en SQLite                                      | Sí       |
| Clase Contactos con los campos requeridos                        | Sí       |
| Alertas si faltan campos                                         | Sí       |
| Regex en campo teléfono                                          | Sí       |
| Mostrar lista desde SQLite                                       | Sí       |
| Eliminar contacto                                                | Sí       |
| Editar/Actualizar contacto                                       | Sí       |
| Compartir contacto                                               | Sí       |
| Ver imagen guardada                                              | Sí       |
| Realizar llamada con `Intent.ACTION_CALL`                        | Sí       |
| Uso de `AlertDialog.Builder`                                     | Sí       |
| Buscador funcional en la lista (punto extra)                     | Sí       |

## Estructura del Proyecto

- `MainActivity.java` - Registro e importación de contacto
- `ActivityContactos.java` - Lista de contactos
- `ContactoAdapter.java` - Adaptador RecyclerView
- `DatabaseHelper.java` - Acceso a base de datos SQLite
- `Modelos/Contacto.java` - Modelo de contacto
- `Modelos/Pais.java` - Modelo de país y validación regex
- `res/layout/` - Diseños de las pantallas y diálogos
- `res/drawable/` - Iconos de acciones
