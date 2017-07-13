# SYSCALL
Para procesar el script que hemos creado hemos implementado una especie de syscall que puede ser llamado desde los scripts.

#### jump
Permite saltar hacia una linea que este marcado con un label.

#### sleep
Genera una interrupcion por un tiempo que se especifica como parametro.
Ejemplos:
* `sleep 1s 10ms`
* `sleep 50ms`

#### runSysExe
Es usado por el proceso principal systemd y lo utiliza para levantar los servicios del sistema.

#### end
Indica que el proceso termina.

#### print

#### runExe
#### startUpService
#### memoryService
#### diskService
#### networkService
#### shellService
#### rsleep
#### discRead
#### discWrite
#### netUpload
#### netDownload
#### run
#### malloc
#### mouse
#### beep
#### keyboard
#### checkUsb
