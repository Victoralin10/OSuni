# Configuration

## Machine

### Number of cpus
`machine.cpus`
Con esta opcion podemos configurar el nro de cpus que se quiere simular.

### Politica de colas de procesos.
Las politicas para las colas de procesos se especifican para cada cpu ya que cada una cuenta con su propia cola.

Para especificar la politica para una cpu lo hacemos de la siguiente manera.
`kernel.policy.cpu<cpuId>`. En `<cpuId>` se debe ingresar el id de la cpu (empieza en 0).

Las opciones disponibles son:
* fifo (First in First Out)
* lifo (Last in Last Out)
* sjf(Shortest Job First)

### Memory
`memory.size`
Con esta opcion especificamos el tamano de la memoria RAM.
Ejemplos:
* 8Gb
* 16Gb
* 500Mb

`memory.pageSize`
Con esta opcion especificamos el tamano de los frames.
Ejmplos:
* 8Mb
* 4Mb
* 4Kb

`memory.policy`
Con esta opcion especificamos la politia a usar en la asignacion de memoria.
Opciones disponibles:
* firstFit
* bestFit
* worstFit

### Disco
`disc.readSpeed`
Con esta opcion especificamos la velocidad de lectura del disco. Se debe ingresar en bytes por segundo.

`disc.writeSpeed`
Con esta opcion especificamos la velocidad de escritura del disco. Se debe ingresar en bytes por segundo.

### RED
`net.uploadSpeed`
Con esta opcion especificamos la velocidad de subida en la red.
Las unidades son en bytes por segundo.

`net.downloadSpeed`
Con esta opcion especificamos la velocidad de descarda en la red.
Las unidades son en bytes por segundo.
