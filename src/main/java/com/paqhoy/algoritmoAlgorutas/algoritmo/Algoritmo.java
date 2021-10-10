package com.paqhoy.algoritmoAlgorutas.algoritmo;

import com.paqhoy.algoritmoAlgorutas.algoritmo.dijkstra.Dijkstra;
import com.paqhoy.algoritmoAlgorutas.algoritmo.kmeans.Kmeans;
import com.paqhoy.algoritmoAlgorutas.model.*;
import com.paqhoy.algoritmoAlgorutas.repository.AlgoritmoRepository;
import com.paqhoy.algoritmoAlgorutas.repository.UsuarioRepository;
import com.paqhoy.algoritmoAlgorutas.service.CallesBloqueadasService;
import com.paqhoy.algoritmoAlgorutas.service.PedidoService;
import com.paqhoy.algoritmoAlgorutas.service.UsuarioService;
import com.paqhoy.algoritmoAlgorutas.service.VehiculoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.io.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
@Transactional
public class Algoritmo {

    public List<APedido> listaPedidos;
    public List<Usuario> listaChoferesMoto;
    public List<Usuario> listaChoferesAuto;
    public List<AVehiculo> listaVehiculoTipo1;
    public List<AVehiculo> listaVehiculoTipo2;
    public List<AVehiculo> listaVehiculoTipo3;
    public List<AVehiculo> listaVehiculoTipo4;
    public List<Cluster> clusterResult;
    public List<CallesBloqueadas> listaCallesBloqueadas;
    public List<Ruta> listaRutas;
    public Dijkstra dijkstraAlgorithm;
    public Kmeans kmeans;

    public Integer cantClusterMotos = 0;
    public Integer cantClusterAutos = 0;
    public Integer cantAutos = 0;
    public Integer cantMotos = 0;

    public Integer cantidadProductos = 0;

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private VehiculoService vehiculoService;
    @Autowired
    private CallesBloqueadasService callesBloqueadasService;
    @Autowired
    private AlgoritmoRepository algoritmoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public String inicializar(){
        //TODO POR BORRAR
//        listaChoferesAuto = usuarioService.getConductoresAutoDisponibles();
//        listaChoferesMoto = usuarioService.getConductoresMotoDisponibles();

        listaVehiculoTipo1 = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            AVehiculo vehiculo = new AVehiculo();
            vehiculo.setTipo("Tipo TA");
            vehiculo.setTipo_id(1);
            vehiculo.setCapacidad(25);
            vehiculo.setPeso(2.5);
            vehiculo.setVelocidad(50.00);
            listaVehiculoTipo1.add(vehiculo);
        }

        listaVehiculoTipo2 = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            AVehiculo vehiculo = new AVehiculo();
            vehiculo.setTipo("Tipo TB");
            vehiculo.setTipo_id(2);
            vehiculo.setCapacidad(20);
            vehiculo.setPeso(2.0);
            vehiculo.setVelocidad(50.00);
            listaVehiculoTipo2.add(vehiculo);
        }

        listaVehiculoTipo3 = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            AVehiculo vehiculo = new AVehiculo();
            vehiculo.setTipo("Tipo TC");
            vehiculo.setTipo_id(3);
            vehiculo.setCapacidad(15);
            vehiculo.setPeso(1.5);
            vehiculo.setVelocidad(50.00);
            listaVehiculoTipo3.add(vehiculo);
        }

        listaVehiculoTipo4 = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            AVehiculo vehiculo = new AVehiculo();
            vehiculo.setTipo("Tipo TD");
            vehiculo.setTipo_id(4);
            vehiculo.setCapacidad(10);
            vehiculo.setPeso(1.0);
            vehiculo.setVelocidad(50.00);
            listaVehiculoTipo4.add(vehiculo);
        }

        listaPedidos = obtenerListaPedidos();

//        if(listaChoferesMoto.size() == 0 && listaChoferesAuto.size() == 0) return "No hay conductores disponibles para las rutas";
        if(listaVehiculoTipo2.size() == 0 && listaVehiculoTipo1.size() == 0) return "No hay vehículos disponibles para las rutas";
        if(listaPedidos.size() == 0) return "No hay pedidos en cola";
        obtenerCantidadClusters();
        kmeans = new Kmeans(cantMotos, cantAutos);
        listaCallesBloqueadas = callesBloqueadasService.obtenerCallesBloqueadas();
        obtenerListaAdyacente();
        return "correcto";
    }

    public String generarRutas(){
        LocalDateTime tiempo1,tiempo2;
        tiempo1 = LocalDateTime.now();
        obtenerPedidosClusterizados();
        obtenerRutas();
        asignarRutas();
        tiempo2 = LocalDateTime.now();
        log.info("Tiempo de ejecución del algortimo: " + (tiempo2.getSecond()-tiempo1.getSecond()) + " segundos");
        return "Rutas generadas exitosamente";
    }

    public List<APedido> obtenerListaPedidos(){
        try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String strYearMonth = getOrdersDateFromName(fileName);
            String line;
            int id = 1;

            while ((line = br.readLine()) != null) {
                final String[] tokens = line.trim().split(",");
                final String[] date = tokens[0].trim().split(":");
                final int day = Integer.parseInt(date[0]);
                final int hour = Integer.parseInt(date[1]);
                final int min = Integer.parseInt(date[2]);
                final int x = Integer.parseInt(tokens[1]);
                final int y = Integer.parseInt(tokens[2]);
                final int demand = Integer.parseInt(tokens[3]);
                final int remaining = Integer.parseInt(tokens[4]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d H:m:s");
                String strDate = strYearMonth + "-" + day + " " + hour + ":" + min + ":0";
                LocalDateTime orderDate = LocalDateTime.parse(strDate, formatter);

                Node node = new Node(
                        id++,
                        x,
                        y,
                        demand,
                        remaining,
                        orderDate
                );
                nodes.add(node);

                System.out.println(day + " " + hour + " " + min + " " + x + " " + y + " " + demand + " " + remaining);
            }
        }
        //List<Pedido> pedidosList = pedidoService.getPedidosPorAtender();
        List<APedido> respuesta = new ArrayList< APedido >();
        for(Pedido pedido : pedidosList){
            int xP = ( pedido.getDireccion_id() - 1 ) % 71;
            int yP = ( pedido.getDireccion_id() - 1 ) / 71;
            //CAMBIAR CAMBIAR CAMBIAR CAMBIARCAMBIAR CAMBIAR CAMBIAR CAMBIAR CAMBIAR CAMBIARCAMBIARCAMBIAR CAMBIAR CAMBIAR CAMBIAR CAMBIAR
            int minutos = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), pedido.getFecha_limite());
            cantidadProductos += pedido.getCantidad();
            APedido apedido = new APedido(pedido.getId(), xP, yP, pedido.getCantidad(), minutos);
            respuesta.add(apedido);
        }
        Collections.sort(respuesta);
        return respuesta;
    }

    public void obtenerCantidadClusters(){
        cantMotos = listaVehiculoTipo1.size();
        cantAutos = listaVehiculoTipo2.size();
        System.out.println(listaVehiculoTipo1.size() + " " + listaVehiculoTipo2.size());
        System.out.println(listaChoferesMoto.size() + " " + listaChoferesAuto.size());
        //TODO  A: 2.5Tn B: 2Tn C: 1.5Tn  D: 1Tn
        int k = (int) (0.9 * (cantidadProductos / ( cantMotos * 4 + cantAutos * 25 )));
        if(k > 10) k = 10;
        if(k < 3) k = 3;
        cantClusterMotos = cantMotos * k;
        cantClusterAutos = cantAutos * k;
    }

    public void obtenerListaAdyacente(){
        int origen, destino;
        InputStream grafo = getClass().getClassLoader().getResourceAsStream("grafo.txt");
        Scanner sc = new Scanner( grafo );
        dijkstraAlgorithm = new Dijkstra(Configuraciones.V, listaCallesBloqueadas);
        for( int i = 0 ; i < Configuraciones.E ; ++i ){
            origen = sc.nextInt() +1;
            destino = sc.nextInt() +1;
            dijkstraAlgorithm.addEdge(origen, destino);
        }
    }

    public void obtenerPedidosClusterizados(){
        int cantClusters = cantClusterMotos + cantClusterAutos;
        List<AVehiculo> vehiculos = inicializarVehiculos();

        //inicializar clusters
        List<Cluster> clustersList = inicializarClusters(vehiculos);
        List<Cluster> clustersAns = inicializarClusters(vehiculos);

        //Clusterizacion
        clusterResult = kmeans.kmeans(listaPedidos,clustersList,cantClusters,clustersAns);
        Double SSE = kmeans.getOptimo(listaPedidos,clustersAns,cantClusters);
        System.out.println("------------------------------------------------------");
        System.out.println("Rutas calculadas con un SSE=" + SSE);
        System.out.println("------------------------------------------------------");
        System.out.println();
        System.out.println();
    }

    public List<AVehiculo> inicializarVehiculos() {
        List<AVehiculo> lista = new ArrayList<>();
        for(int i=0; i<cantClusterMotos;i++){
            AVehiculo vehiculo = new AVehiculo();
            vehiculo.setTipo("Moto");
            vehiculo.setCapacidad(4);
            vehiculo.setPeso(3.0);
            vehiculo.setVelocidad(60.00);
            vehiculo.setTipo_id(2);
            lista.add(vehiculo);
        }
        for(int i=0; i<cantClusterAutos;i++){
            AVehiculo vehiculo = new AVehiculo();
            vehiculo.setTipo("Auto");
            vehiculo.setCapacidad(25);
            vehiculo.setPeso(5.0);
            vehiculo.setVelocidad(30.00);
            vehiculo.setTipo_id(1);
            lista.add(vehiculo);
        }
        return lista;
    }

    public List<Cluster> inicializarClusters(List<AVehiculo>  vehiculos){
        List<Cluster> lista = new ArrayList<Cluster>();
        for(AVehiculo vehiculo: vehiculos){
            Cluster cluster =  new Cluster();
            //TODO ENTENDER ESTA COSA
            cluster.pedidos = new PriorityQueue<APedido>(500,
                    new Comparator<APedido>(){
                        //override compare method
                        public int compare(APedido i, APedido j){
                            // if(i.minFaltantes > j.minFaltantes) return 1;
                            // else if (i.minFaltantes < j.minFaltantes) return -1;
                            // else return 0;
                            if(Math.abs(i.x - Configuraciones.almacenX) + Math.abs(i.y - Configuraciones.almacenY) > Math.abs(j.x - Configuraciones.almacenX) + Math.abs(j.y - Configuraciones.almacenY)) return 1;
                            else if (Math.abs(i.x - Configuraciones.almacenX) + Math.abs(i.y - Configuraciones.almacenY) < Math.abs(j.x - Configuraciones.almacenX) + Math.abs(j.y - Configuraciones.almacenY)) return -1;
                            else if (i.cantidad > j.cantidad) return 1;
                            else if (i.cantidad < j.cantidad) return -1;
                            else return 1;
                        }
                    }
            );
            cluster.centroideX = 0;
            cluster.centroideY = 0;
            cluster.vehiculo = vehiculo;
            lista.add(cluster);
        }
        return lista;
    }

    public void obtenerRutas(){

        //calculamos el tiempo en minutos en que iniciamos a correr el algoritmo
        LocalDateTime tiempo = LocalDateTime.now();
        LocalDateTime d1 = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);
        int tiempoMinutosInicio = (int) ChronoUnit.MINUTES.between(d1, tiempo);

        //para calcular el tiempo máximo de entrega
        int maximoTiempo = -1;

        //inicializamos la lista de rutas
        listaRutas = new ArrayList< Ruta >();

        for(Cluster cluster:clusterResult){
            //asignamos el tiempo en minutos en que iniciamos a correr el algoritmo
            int tiempoMinutos = tiempoMinutosInicio;
            if(cluster.firstPedido == null) continue;
            //imprimos en forma de reporte la información relacionada a la ruta
            System.out.println("------------------------------------------------------");
            System.out.println("Cluster: " + cluster.vehiculo.getTipo());
            System.out.println("Capacidad: " + cluster.capacidad + "/" + cluster.vehiculo.getCapacidad());
            // System.out.println("Tiempo inicial en minutos: " + tiempoMinutos);
            System.out.println("------------------------------------------------------");

            //incializamos la ruta
            Ruta ruta = new Ruta(cluster.vehiculo, cluster.capacidad);

            //seteamos el origen a nuestro almacén
            int origen = Configuraciones.almacen;

            //nos servirá para hallar un ruta si estamos en un nodo bloqueado
            int ultimoViable = Configuraciones.almacen;

            //para el firstPedido
            if(cluster.firstPedido != null){
                APedido pedido = cluster.firstPedido;
                ruta.addPedido(pedido);

                System.out.println("x:  " + pedido.x + "   y: " + pedido.y + "   z: " + pedido.minFaltantes + "   cant: " + pedido.cantidad + "   idNodo: " + pedido.getNodoId());

                boolean estaBloqueada = estaBloqueada(tiempoMinutos, origen);

                if(estaBloqueada){
                    System.out.println("Bloqueado!");
                    origen = ultimoViable;
                }

                dijkstraAlgorithm.dijkstra( origen, tiempoMinutos, (int) Math.round(cluster.vehiculo.getVelocidad()));
                System.out.printf("Ruta: ");

                int tamanoIni = ruta.recorrido.size();

                dijkstraAlgorithm.printShortestPath(pedido.getNodoId(), ruta, 1);

                int tamanoFin = ruta.recorrido.size();

                if(tamanoFin - tamanoIni >= 2) {
                    ultimoViable = ruta.recorrido.get(ruta.recorrido.size() - 2);
                }

                int tiempoEnLlegar = (tamanoFin - tamanoIni-1) * 60 / ((int) Math.round(cluster.vehiculo.getVelocidad()));
                System.out.println("Nodos recorridos: " + (tamanoFin - tamanoIni - 1) + "   Tiempo llegada en minutos: " + tiempoEnLlegar + " minutos");

                tiempoMinutos += tiempoEnLlegar;

                origen = pedido.getNodoId();

                if(cluster.pedidos.size() != 0) System.out.println();
            }

            //iteramos mientras sacamos pedidos de la cola de prioridad del cluster
            //ordenados por distancia manhattan al almacén
            while(!cluster.pedidos.isEmpty()){

                //extraemos un pedido del cluster
                APedido pedido = cluster.pedidos.poll();
                ruta.addPedido(pedido);
                //imprimir información del pedido
                System.out.println("x:  " + pedido.x + "   y: " + pedido.y + "   z: " + pedido.minFaltantes + "   cant: " + pedido.cantidad + "   idNodo: " + pedido.getNodoId());

                //verificamos si nos encontramos en un nodo bloqueado
                //esto puede ocurrir ya que hemos entregado un pedido en un nodo bloqueado
                //o si el almancén es un nodo bloqueado
                boolean estaBloqueada = estaBloqueada(tiempoMinutos, origen);

                if(estaBloqueada){
                    System.out.println("Bloqueado!");
                    origen = ultimoViable;
                    ruta.addNodo(origen);
                }

                //corremos el algoritmo de dijkstra
                dijkstraAlgorithm.dijkstra( origen, tiempoMinutos, (int) Math.round(cluster.vehiculo.getVelocidad()) );
                System.out.printf("Ruta: ");

                //tamano antes de la nueva parte de la ruta
                int tamanoIni = ruta.recorrido.size();

                //obtenemos la ruta en un array
                dijkstraAlgorithm.printShortestPath(pedido.getNodoId(), ruta, 1);

                //tamano luego de la nueva parte de la ruta
                int tamanoFin = ruta.recorrido.size();

                // para obtener el último nodo que no está bloqueado si es que acabamos de entregar un pedido en un nodo bloqueado
                if(tamanoFin - tamanoIni >= 2) {
                    ultimoViable = ruta.recorrido.get(ruta.recorrido.size() - 2);
                }

                //calculamos el tiempo que tomó en llegar
                int tiempoEnLlegar = (tamanoFin - tamanoIni-1) * 60 / ((int) Math.round(cluster.vehiculo.getVelocidad()));
                System.out.println("Nodos recorridos: " + (tamanoFin - tamanoIni - 1) + "   Tiempo llegada en minutos: " + tiempoEnLlegar + " minutos");


                // calculamos el nuevo tiempo en el que nos encontramos
                tiempoMinutos += tiempoEnLlegar;

                //cambiamos el origen
                origen = pedido.getNodoId();

                //detalle estético, la última línea no imprime una nueva en el reporte
                if(cluster.pedidos.size() != 0) System.out.println();
            }

            //tiempo que tomó realizar la entrega
            int diferenciaTiempo = tiempoMinutos - tiempoMinutosInicio;

            if(diferenciaTiempo > maximoTiempo){
                maximoTiempo = diferenciaTiempo;
            }
            System.out.println("------------------------------------------------------");
            System.out.println("Tiempo de entrega: " + diferenciaTiempo + " minutos");
            System.out.println("------------------------------------------------------");

            if(cluster.firstPedido != null){
                System.out.println("Camino de retorno al almacén:  ");

                origen = ruta.recorrido.get(ruta.recorrido.size() - 1);
                boolean estaBloqueada = estaBloqueada(tiempoMinutos, origen);

                if(estaBloqueada){
                    System.out.println("Bloqueado!");
                    origen = ultimoViable;
                    ruta.addNodoRetorno(origen);
                }

                dijkstraAlgorithm.dijkstra( origen, tiempoMinutos, (int) Math.round(cluster.vehiculo.getVelocidad()) );

                int tamanoIni = ruta.retorno.size();

                dijkstraAlgorithm.printShortestPath(Configuraciones.almacen, ruta, 2);
            }

            System.out.println();
            System.out.println();
            listaRutas.add(ruta);
        }
        System.out.println("Máximo tiempo de entrega: " + maximoTiempo + " minutos");
    }

    public void asignarRutas(){
        int contadorAutos = 0;
        log.info("Asignar rutas: ");
        log.info("Size Chof Auto: " + listaChoferesAuto.size());
        log.info("Size Chof Moto: " + listaChoferesMoto.size());
        log.info("cantAutos: " + cantAutos);
        log.info("cantMotos: " + cantMotos);
        for(Usuario chofer: listaChoferesAuto){
            if(contadorAutos == cantAutos) break;
            int minimo = Integer.MAX_VALUE;
            int contador = 0;
            int minCont = -1;
            for(Ruta ruta: listaRutas){
                if(ruta.vehiculo.getTipo_id() == 1 && ruta.chofer == null && minimo > ruta.tiempoMin){
                    minimo = ruta.tiempoMin;
                    minCont = contador;
                }
                contador++;
            }
            if(minCont == -1) break;
            listaRutas.get(minCont).chofer = chofer;
            listaRutas.get(minCont).vehiculo = listaVehiculoTipo2.get(contadorAutos);
            log.info("MinCont: " + minCont);
            log.info("Auto: " + listaRutas.get(minCont).chofer);
            contadorAutos++;
        }

        int contadorMotos = 0;
        for(Usuario chofer: listaChoferesMoto){
            if(contadorMotos == cantMotos) break;
            int minimo = Integer.MAX_VALUE;
            int contador = 0;
            int minCont = -1;
            for(Ruta ruta: listaRutas){
                if(ruta.vehiculo.getTipo_id() == 2 && ruta.chofer == null && minimo > ruta.tiempoMin){
                    minimo = ruta.tiempoMin;
                    minCont = contador;
                }
                contador++;
            }
            if(minCont == -1) break;
            listaRutas.get(minCont).chofer = chofer;
            listaRutas.get(minCont).vehiculo = listaVehiculoTipo1.get(contadorMotos);
            log.info("MinCont: " + minCont);
            log.info("Auto: " + listaRutas.get(minCont).chofer);
            contadorMotos++;
        }

//        for(int i=listaRutas.size()-1; i>=0; i--){
//            if(listaRutas.get(i).chofer == null) listaRutas.remove(i);
//            else {
//                Ruta ruta = listaRutas.get(i);
//                AlgoRuta algoRuta = new AlgoRuta();
//                algoRuta.setInicio(LocalDateTime.now());
//                algoRuta.setDistancia(0.0);
//                algoRuta.setCosto(0.0);
//                algoRuta.setUsuario_id(ruta.chofer.getId());
//                algoRuta.setVehiculo_id(ruta.vehiculo.getId());
//                algoRuta.setEstado_id(2);
//                algoritmoRepository.save(algoRuta);
//                usuarioRepository.cambiarEstadoUsuario(algoRuta.getUsuario_id());
//                algoritmoRepository.cambiarEstadoVehiculo(algoRuta.getVehiculo_id());
//                int orden = 1;
//                for(APedido pedido: ruta.pedidos){
//                    algoritmoRepository.cambiarEstadoPedido(pedido.id);
//                    algoritmoRepository.insertarPedidoRuta(algoRuta.getId(), pedido.id, orden);
//                    orden++;
//                }
//                orden = 1;
//                for(int nodo: ruta.recorrido){
//                    algoritmoRepository.insertarNodoRuta(algoRuta.getId(),nodo,orden);
//                    orden++;
//                }
//                for(int nodo: ruta.retorno){
//                    algoritmoRepository.insertarNodoRuta(algoRuta.getId(),nodo,orden);
//                    orden++;
//                }
//            }
//            System.out.println(i);
//        }
    }

    private boolean estaBloqueada(int tiempoMinutos, int nodoId){
        for( CallesBloqueadas par : listaCallesBloqueadas ){
            if( ( tiempoMinutos >= par.getMinutosInicio() ) && ( tiempoMinutos < par.getMinutosFin() ) ){
                return par.estaNodo(nodoId);
            }
        }
        return false;
    }
}
