package com.paqhoy.algoritmoAlgorutas.service;

import com.paqhoy.algoritmoAlgorutas.model.Configuraciones;
import com.paqhoy.algoritmoAlgorutas.model.Pedido;
import com.paqhoy.algoritmoAlgorutas.repository.PedidoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Slf4j
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public String uploadFile(MultipartFile file) {
        try {
            File fileObj = convertMultiPartFileToFile(file);
            getAllPedidos(fileObj);
            fileObj.delete();
            return "Done!";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }

    private void getAllPedidos(File fileObj) throws FileNotFoundException {
        Scanner sc = new Scanner(fileObj);
        List<Pedido> pedidosList = new ArrayList<>();
        String strDate = getDateFromFileName(fileObj.getName());
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Pedido pedido = getPedidoFromLine(line, strDate);
            pedidosList.add(pedido);
        }
        sc.close();
        pedidoRepository.saveAll(pedidosList);
    }

    private Pedido getPedidoFromLine(String line, String strDate) {
        Pedido pedido = new Pedido();

        // set Fecha del pedido
        int hh = getIntFromLine(line, ":");
        line = line.substring(line.indexOf(':') + 1);
        int mm = getIntFromLine(line, ",");
        line = line.substring(line.indexOf(',') + 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
        LocalDateTime fechaPedido = LocalDateTime.parse(strDate + " " + hh + ":" + mm + ":0", formatter);
        pedido.setFecha_pedido(fechaPedido);

        // set nodo con formula
        int y = getIntFromLine(line, ",");
        line = line.substring(line.indexOf(',') + 1);
        int x = getIntFromLine(line, ",");
        line = line.substring(line.indexOf(',') + 1);
        pedido.setDireccion_id(71 * y + x + 1);

        // set precio y cantidad de paquetes
        int numPaq = getIntFromLine(line, ",");
        line = line.substring(line.indexOf(',') + 1);
        pedido.setCantidad(numPaq);
        pedido.setPrecio(numPaq * Configuraciones.precio);

        // set cliente
        int idCliente = getIntFromLine(line, ",");
        line = line.substring(line.indexOf(',') + 1);
        pedido.setCliente_id(idCliente);

        // set tipo de pedido y hora máxima de entrega
        int hLimite = Integer.parseInt(line);
        if (hLimite <= 4)
            pedido.setTipo_id(hLimite - 1);
        else if (hLimite == 8)
            pedido.setTipo_id(4);
        else
            pedido.setTipo_id(5);
        pedido.setFecha_limite(fechaPedido.plusHours(hLimite));

        // valores por defecto iniciales
        pedido.setFecha_entrega(null);
        pedido.setCreated_at(LocalDateTime.now());
        pedido.setUpdated_at(LocalDateTime.now());
        pedido.setDeleted_at(null);
        pedido.setEstado_id(1);
        return pedido;
    }

    private String getDateFromFileName(String fileName) {
        String strDate = fileName.substring(0, 4) + "-" + fileName.substring(4, 6) + "-" + fileName.substring(6, 8);
        return strDate;
    }

    private Integer getIntFromLine(String line, String c) {
        int indexChar = line.indexOf(c);
        return Integer.parseInt(line.substring(0, indexChar));
    }

    public List<Pedido> getPedidosPorAtender() {
        return pedidoRepository.findPedidosPorAtender();
    }
}
