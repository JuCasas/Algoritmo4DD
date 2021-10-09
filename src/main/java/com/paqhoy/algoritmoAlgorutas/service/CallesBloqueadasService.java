package com.paqhoy.algoritmoAlgorutas.service;

import com.paqhoy.algoritmoAlgorutas.model.Bloqueado;
import com.paqhoy.algoritmoAlgorutas.model.CallesBloqueadas;
import com.paqhoy.algoritmoAlgorutas.model.Intervalo;
import com.paqhoy.algoritmoAlgorutas.model.IntervaloNodo;
import com.paqhoy.algoritmoAlgorutas.repository.BloqueadoRepository;
import com.paqhoy.algoritmoAlgorutas.repository.IntervaloNodoRepository;
import com.paqhoy.algoritmoAlgorutas.repository.IntervaloRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Slf4j
public class CallesBloqueadasService {

    @Autowired
    private IntervaloRepository intervaloRepository;
    @Autowired
    private IntervaloNodoRepository intervaloNodoRepository;
    @Autowired
    private BloqueadoRepository bloqueadoRepository;

    public List<CallesBloqueadas> obtenerCallesBloqueadasActuales(){
        List<Bloqueado> lista = bloqueadoRepository.obtenerCallesBloqueadasActuales();
        List<CallesBloqueadas> cbLista = new ArrayList<>();
        for(Bloqueado bloqueado: lista){
            CallesBloqueadas cb = new CallesBloqueadas(bloqueado.getId(), convertLocalDateToMinutes(bloqueado.getInicio()), convertLocalDateToMinutes(bloqueado.getFin()));
            String line = bloqueado.getNodos();
            while(line.length() != 0){
                int indexChar = line.indexOf(',');
                int idNodo;
                if (indexChar == -1){
                    idNodo = Integer.parseInt( line );
                    line = "";
                }
                else{
                    idNodo = Integer.parseInt( line.substring( 0, indexChar ) );
                    line = line.substring( indexChar + 1 );
                }
                cb.addNode(idNodo);
            }
            cbLista.add(cb);
        }
        return cbLista;
    }

    public List<CallesBloqueadas> obtenerCallesBloqueadas(){
        List<Bloqueado> lista = bloqueadoRepository.obtenerCallesBloqueadas();
        List<CallesBloqueadas> cbLista = new ArrayList<>();
        for(Bloqueado bloqueado: lista){
            CallesBloqueadas cb = new CallesBloqueadas(bloqueado.getId(), convertLocalDateToMinutes(bloqueado.getInicio()), convertLocalDateToMinutes(bloqueado.getFin()));
            String line = bloqueado.getNodos();
            while(line.length() != 0){
                int indexChar = line.indexOf(',');
                int idNodo;
                if (indexChar == -1){
                    idNodo = Integer.parseInt( line );
                    line = "";
                }
                else{
                    idNodo = Integer.parseInt( line.substring( 0, indexChar ) );
                    line = line.substring( indexChar + 1 );
                }
                cb.addNode(idNodo);
            }
            cbLista.add(cb);
        }
        return cbLista;
    }

    private Integer convertLocalDateToMinutes(LocalDateTime ldt){
        LocalDateTime d1 = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);
        return (int) ChronoUnit.MINUTES.between(d1, ldt);
    }

    public String uploadFile (MultipartFile file) {
        try {
            File fileObj = convertMultiPartFileToFile(file);
            getCallesBloqueadas(fileObj);
            fileObj.delete();
            return "Done!";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to File", e);
        }
        return convertedFile;
    }

    private void getCallesBloqueadas(File fileObj) throws FileNotFoundException {
        Scanner sc = new Scanner(fileObj);
        List<Intervalo> intervaloList = new ArrayList<>();
        String strDate = getDateFromFileName(fileObj.getName());
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Intervalo intervalo = getIntervaloFromLine(line, strDate);
            intervaloRepository.save(intervalo);
            intervaloList.add(intervalo);
            line = line.substring( line.indexOf(',') + 1 );
            List<IntervaloNodo> intervaloNodoList = new ArrayList<>();
            while(line.length() != 0){
                IntervaloNodo intervaloNodo = new IntervaloNodo();
                int indexChar = line.indexOf(',');
                int x,y;
                x = Integer.parseInt( line.substring( 0, indexChar ) );
                line = line.substring( indexChar + 1 );
                indexChar = line.indexOf(',');
                if (indexChar == -1){
                    y = Integer.parseInt( line );
                    line = "";
                }
                else{
                    y = Integer.parseInt( line.substring( 0, indexChar ) );
                    line = line.substring( indexChar + 1 );
                }
                intervaloNodo.setNodo_id(x + 71 * y + 1);
                intervaloNodo.setIntervalo_id(intervalo.getId());
                intervaloNodoList.add( intervaloNodo );
            }
            intervaloNodoRepository.saveAll(intervaloNodoList);
        }
        sc.close();
//        intervaloRepository.saveAll(intervaloList);
    }

    private String getDateFromFileName(String fileName){
        String strDate = fileName.substring(0, 4) + "-" + fileName.substring(4, 6);
        return strDate;
    }

    private Intervalo getIntervaloFromLine(String line, String strDate){
        Intervalo intervalo = new Intervalo();

        int dia = getIntFromLine(line,":");
        line = line.substring( line.indexOf(':') + 1 );
        int hh = getIntFromLine(line,":");
        line = line.substring( line.indexOf(':') + 1 );
        int mm = getIntFromLine(line,"-");
        line = line.substring( line.indexOf('-') + 1 );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d H:m:s");
        LocalDateTime inicio = LocalDateTime.parse(strDate + "-" + dia + " " + hh + ":" + mm + ":0", formatter);
        intervalo.setInicio(inicio);

        dia = getIntFromLine(line,":");
        line = line.substring( line.indexOf(':') + 1 );
        hh = getIntFromLine(line,":");
        line = line.substring( line.indexOf(':') + 1 );
        mm = getIntFromLine(line,",");
        line = line.substring( line.indexOf(',') + 1 );
        LocalDateTime fin = LocalDateTime.parse(strDate + "-" + dia + " " + hh + ":" + mm + ":0", formatter);
        intervalo.setFin(fin);

        return intervalo;
    }

    private Integer getIntFromLine(String line, String c){
        int indexChar = line.indexOf(c);
        return Integer.parseInt( line.substring( 0, indexChar ) );
    }
}
