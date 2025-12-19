package com.lta.sistemapagos.services;

import com.lta.sistemapagos.entities.Estudiante;
import com.lta.sistemapagos.entities.Pago;
import com.lta.sistemapagos.enums.PagoStatus;
import com.lta.sistemapagos.enums.TypePago;
import com.lta.sistemapagos.repository.EstudianteRepository;
import com.lta.sistemapagos.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    public Pago savePago(MultipartFile file, double cantidad, TypePago typePago, LocalDate localDate, String codidoEstudiante) throws IOException {
        /*
        - Creamos una ruta donde se guarda el archivo
        - System.getProperty("user.home"): Obtiene la ruta del directorio personal del usuario S.O
        - Paths.get(): Crea un objeto Path apuntando a una carpeta llamada enset/pagos dentro del disco
         */
        Path folderPath = Paths.get(System.getProperty("user.home"),"enset-data","pagos");

        if(!Files.exists(folderPath)){
            Files.createDirectories(folderPath);
        }

        String fileName = UUID.randomUUID().toString();

        //creamos un path para el archivo pdf que se guardara en enset/data
        Path filePath = Paths.get(System.getProperty("user.home"),"enset-data","pagos",fileName+".pdf");

        //files.getInputStream(): Obtiene el flujo de datos del archivo recibido desde la solicitud HTTP
        //Files.copy(...): Copia los datos del archivo al destino filePath
        Files.copy(file.getInputStream(),filePath);

        Estudiante estudiante = estudianteRepository.findByCodigo(codidoEstudiante);

        Pago pago = Pago.builder()
                .type(typePago)
                .status(PagoStatus.CREADO)
                .fecha(localDate)
                .estudiante(estudiante)
                .cantidad(cantidad)
                .file(filePath.toUri().toString())
                .build();

        return pagoRepository.save(pago);
    }

    public byte[] getArchivoPorId(Long pagoId) throws IOException {
        Pago pago = pagoRepository.findById(pagoId).get();
        /*
         - pago.getFile(): Obtiene la URI del archivo guardado
         - URI.create(...): Convierte la cadena en un objeto URI
         - Path.of(...): Convierte el URI en un Path
         - Files.readAllBytes(): Lee el contenido del archivo y lo devuelve como un array de bytes
        */
        return Files.readAllBytes(Path.of(URI.create(pago.getFile())));
    }

    public Pago actualizarPagoPorStatus(PagoStatus status,Long id){
        Pago pago = pagoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pago no encontrado"));;
        pago.setStatus(status);
        return pagoRepository.save(pago);
    }
}
