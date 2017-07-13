# Events
El simulador emite eventos que pueden ser escuchados por los listeners.
De esta manera el monitor puede representar lo que pasa en el simulador.
Los eventos estan definidos en el `package pe.edu.uni.fiis.so.simulation.events`.

## Lista de eventos
Codigo | Descripcion
 --- | ---
process.new | Este evento se emite cuando un nuevo proceso es creado.
process.changeStatus | Este evento se emite cuando un proceso cambia de estado.
cpu.changeStatus | Este evento se emite cuando el cpu cambia de estado.
cpu.updateStats | Este evento se emite regularmente y contiene los datos estadisticos de la cpu.
memory.update | Este evento se emite cuando hay un cambio en la memoria.
clock.update | Este evento se emite regularmente y contiene el tiempo transcurrido desde el inicio de la simulacion.
log.update | Este evento se emite para registrar diverson eventos que pasan dentro del kernel.
io.update | Este evento se emite para registrar un evento de I/O.
