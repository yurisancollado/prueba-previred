# Prueba Yurisan Collado

Este proyecto  invoque al servicio REST GDD y escriba como salida un archivo con las fechas, los periodos recibidos y la lista de periodos faltantes.

Una versión del GDD se encuentra en este repositorio en GitHub: https://github.com/previred/Generador_Datos_Desafio_Uno

El Json de entrada tiene la siguiente estructura:
```
*id*: identificador
*fechaCreacion*: Fecha de inicio de la secuencia
*fechaFin*: Fecha de fin de la secuencia
*fechas*: Lista de fechas que están en el rango de la fecha que se encuentra en “fechaCreacion” hasta la fecha “fechaFin”
```
Ejemplo.
```json
{
    "id": 6,
    "fechaCreacion": "1968-08-01",
    "fechaFin": "1971-06-01",
    "fechas": [
      "1969-03-01",
      "1969-05-01",
      "1969-09-01",
      "1971-05-01"]
}
```
***Nota***:
- El formato de las fechas es yyyy-MM-dd
- El servicio entrega 1 periodos, el periodo contiene una fecha inicial una fecha final y una lista fechas.

El archivo de salida, tiene el siguiente formato y se crea en la directorio ***miPrueba\target***:

```
	fecha creación: 2018-10-01
    fecha fin: 2019-04-01
    fechas recibidas: 2018-10-01, 2018-12-01, 2019-01-01, 2019-04-01
	fechas faltantes: 2018-11-01, 2019-02-01, 2019-03-01
```


# Detalle del  sistemas

Java 8
Maven 3

# Configuración del archivo properties
En el archivo **config.properties **se encuentra las siguientes propiedades
```
	url.api = http://127.0.0.1:8080/periodos/api 
	archivo.salida=salidaNivel2.txt
```


# Compilar y ejecutar el proyecto

Para copilar el proyecto se requiere Java y Maven instalado.

Ingresar al directorio *miPrueba* ejecutar el siguiente comando *maven*

```bash
mvn package
```

Luego de compilar el proyecto ingresar al directorio *target* ejecutar el siguiente comando *java*

```bash
java -jar miPrueba-1.0-SNAPSHOT-jar-with-dependencies.jar
```

En la consola mostrara un mensaje como el siguiente:
```
Lectura de Properties: config.properties
Llamada a api: http://127.0.0.1:8080/periodos/api
Fin de Calculo fecha
Creado archivo de salida en la ruta: \miPrueba\target\salidaNivel2.txt
```