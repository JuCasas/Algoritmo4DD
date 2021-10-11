package com.paqhoy.algoritmoAlgorutas.algoritmo;

import com.paqhoy.algoritmoAlgorutas.algoritmo.dijkstra.Dijkstra;
import com.paqhoy.algoritmoAlgorutas.algoritmo.kmeans.Kmeans;
import com.paqhoy.algoritmoAlgorutas.model.*;
// import com.paqhoy.algoritmoAlgorutas.repository.AlgoritmoRepository;
// import com.paqhoy.algoritmoAlgorutas.repository.UsuarioRepository;
import com.paqhoy.algoritmoAlgorutas.service.CallesBloqueadasService;
// import com.paqhoy.algoritmoAlgorutas.service.PedidoService;
// import com.paqhoy.algoritmoAlgorutas.service.UsuarioService;
// import com.paqhoy.algoritmoAlgorutas.service.VehiculoService;
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

    // POR BORRAR
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

    // POR BORRAR
    public Integer cantidadProductos = 0;
    public Integer cantClusterMotos = 0;
    public Integer cantClusterAutos = 0;
    public Integer cantAutos = 0;
    public Integer cantMotos = 0;

    public Integer cantClusterVehiculoTipo1 = 0;
    public Integer cantClusterVehiculoTipo2 = 0;
    public Integer cantClusterVehiculoTipo3 = 0;
    public Integer cantClusterVehiculoTipo4 = 0;
    public Integer cantVehiculoTipo1 = 0;
    public Integer cantVehiculoTipo2 = 0;
    public Integer cantVehiculoTipo3 = 0;
    public Integer cantVehiculoTipo4 = 0;

    @Autowired
    private CallesBloqueadasService callesBloqueadasService;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    /**
     * Inicializa las variables necesarias para ejecutar el algoritmo
     * 
     * @return una cadena que indica el resultado del intento de inicialización
     */
    public String inicializar() {
        // listaChoferesAuto = usuarioService.getConductoresAutoDisponibles();
        // listaChoferesMoto = usuarioService.getConductoresMotoDisponibles();

        // Inicializando listas de vehículos
        listaVehiculoTipo1 = initializeVehicleList(1);
        listaVehiculoTipo2 = initializeVehicleList(2);
        listaVehiculoTipo3 = initializeVehicleList(3);
        listaVehiculoTipo4 = initializeVehicleList(4);

        // Obteniendo la lista de pedidos
        obtenerListaPedidos();

        // Sin vehículos
        // if (listaVehiculoTipo1.size() == 0 && listaVehiculoTipo2.size() == 0 &&
        // listaVehiculoTipo3.size() == 0
        // && listaVehiculoTipo4.size() == 0) {
        // return "No hay vehículos disponibles para las rutas";
        // }

        // Sin pedidos
        if (listaPedidos.size() == 0) {
            return "No hay pedidos en cola";
        }

        // Obteniendo la cantidad de clusters
//        obtenerCantidadClusters();

        // Para agrupar en clusters
        kmeans = new Kmeans(cantVehiculoTipo1, cantVehiculoTipo2, cantVehiculoTipo3, cantVehiculoTipo4);

        // Obteniendo las calles bloqueadas
        obtenerCallesBloqueadas();

        // Obteniendo la lista de adyacencia
        obtenerListaAdyacente();

        return "correcto";
    }

    /**
     * Inicializa una lista de vehículos
     * 
     * @param typeId identificador del tipo de vehículo
     * @return lista con vehículos agregados
     */
    public List<AVehiculo> initializeVehicleList(int typeId) {
        List<AVehiculo> list = new ArrayList<>();
        int count;
        String type;
        int capacity;
        double weight;

        if (typeId == 1) {
            count = 2;
            type = "Tipo TA";
            capacity = 25;
            weight = 2.5;
        } else if (typeId == 2) {
            count = 4;
            type = "Tipo TB";
            capacity = 20;
            weight = 2.0;
        } else if (typeId == 3) {
            count = 4;
            type = "Tipo TC";
            capacity = 15;
            weight = 1.5;
        } else {
            count = 10;
            type = "Tipo TD";
            capacity = 10;
            weight = 1.0;
        }

        for (int i = 0; i < count; i++) {
            AVehiculo vehiculo = new AVehiculo();
            vehiculo.setTipo(type);
            vehiculo.setTipoId(typeId);
            vehiculo.setCapacidad(capacity);
            vehiculo.setPeso(weight);
            vehiculo.setVelocidad(50.0);
            list.add(vehiculo);
        }

        return list;
    };

    /**
     * Genera las rutas a partir de los datos obtenidos
     * 
     * @return cadena que indica que la generación de rutas fue exitosa
     */
    public String generarRutas() {
        LocalDateTime tiempo1, tiempo2;

        tiempo1 = LocalDateTime.now();

        obtenerPedidosClusterizados();
        obtenerRutas();
        asignarRutas();

        tiempo2 = LocalDateTime.now();

        System.out.print("Tiempo de ejecución del algoritmo: ");
        System.out.println((tiempo2.getSecond() - tiempo1.getSecond()) + " segundos");

        return "Rutas generadas exitosamente";
    }

    /**
     * Obtiene mes y año a partir del nombre del archivo de nodos bloqueados
     * 
     * @param fileName cadena con la ruta completa del archivo
     * @return cadena con el mes y el año correspondientes
     */
    public static String getLockedNodesDateFromName(String fileName) {
        File file = new File(fileName);
        String name = file.getName();
        String strYearMonth = name.substring(0, 4) + "-" + name.substring(4, 6);

        return strYearMonth;
    }

    /**
     * Obtiene mes y año a partir del nombre del archivo de pedidos
     * 
     * @param fileName cadena con la ruta completa del archivo
     * @return cadena con el mes y el año correspondientes
     */
    public static String getOrdersDateFromName(String fileName) {
        File file = new File(fileName);
        String name = file.getName();
        String strYearMonth = name.substring(6, 10) + "-" + name.substring(10, 12);

        return strYearMonth;
    }

    /**
     * Convierte una fecha del tipo LocalDateTime a minutos del tipo int
     * 
     * @param ldt fecha del tipo LocalDateTime
     * @return la fecha convertida a los minutos que pasaron desde el inicio del año
     */
    private Integer convertLocalDateTimeToMinutes(LocalDateTime ldt) {
        LocalDateTime d1 = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

        return (int) ChronoUnit.MINUTES.between(d1, ldt);
    }

    /**
     * Obtiene la lista de pedidos a partir de un archivo de texto
     */
    public void obtenerListaPedidos() {
        try {
            // Para lectura del archivo
            String fileName = "src/main/resources/ventas202212.txt";
            final BufferedReader br = new BufferedReader(new FileReader(fileName));
            String strYearMonth = getOrdersDateFromName(fileName); // datos del nombre del archivo
            String line; // línea del archivo
            int id = 1; // contador para identificador
            listaPedidos = new ArrayList<APedido>(); // para almacenar pedidos
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d H:m:s");

            // Leyendo datos del archivo

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
                String strDate = strYearMonth + "-" + day + " " + hour + ":" + min + ":0";
                LocalDateTime orderDate = LocalDateTime.parse(strDate, formatter);

                APedido pedido = new APedido(id++, x, y, demand, remaining, orderDate);
                listaPedidos.add(pedido);

                // System.out.println(day + " " + hour + " " + min + " " + x + " " + y + " " +
                // demand + " " + remaining);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // List<Pedido> pedidosList = pedidoService.getPedidosPorAtender();
        // List<APedido> respuesta = new ArrayList<APedido>();
        // for (Pedido pedido : pedidosList) {
        // int xP = (pedido.getDireccion_id() - 1) % 71;
        // int yP = (pedido.getDireccion_id() - 1) / 71;
        // // CAMBIAR
        // int minutos = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(),
        // pedido.getFecha_limite());
        // cantidadProductos += pedido.getCantidad();
        // APedido apedido = new APedido(pedido.getId(), xP, yP, pedido.getCantidad(),
        // minutos);
        // respuesta.add(apedido);
        // }
        // Collections.sort(respuesta);
        // return respuesta;
    }

    /**
     * Obtiene la lista de calles bloqueadas a partir de un archivo de texto
     */
    public void obtenerCallesBloqueadas() {
        try {
            String fileName = "src/main/resources/202209bloqueadas.txt";
            final BufferedReader br = new BufferedReader(new FileReader(fileName));
            String strYearMonth = getLockedNodesDateFromName(fileName);
            String line;
            int id = 1; // para el identificador de la calle bloqueada
            listaCallesBloqueadas = new ArrayList<CallesBloqueadas>(); // para calles bloqueadas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d H:m:s");

            while ((line = br.readLine()) != null) {
                final String[] tokens = line.trim().split(",");
                final String[] plazo = tokens[0].trim().split("-");
                final String[] inicio = plazo[0].trim().split(":");
                final String[] fin = plazo[1].trim().split(":");
                final int diaIni = Integer.parseInt(inicio[0]);
                final int horaIni = Integer.parseInt(inicio[1]);
                final int minIni = Integer.parseInt(inicio[2]);
                final int diaFin = Integer.parseInt(fin[0]);
                final int horaFin = Integer.parseInt(fin[1]);
                final int minFin = Integer.parseInt(fin[2]);
                String strDateIni = strYearMonth + "-" + diaIni + " " + horaIni + ":" + minIni + ":0";
                String strDateFin = strYearMonth + "-" + diaFin + " " + horaFin + ":" + minFin + ":0";
                LocalDateTime dateIni = LocalDateTime.parse(strDateIni, formatter);
                LocalDateTime dateFin = LocalDateTime.parse(strDateFin, formatter);

                final int len = tokens.length - 1;
                final String[] strCoords = Arrays.copyOfRange(tokens, 1, len + 1);
                final int[] coords = new int[len];

                // System.out.print(diaIni + " " + horaIni + " " + minIni + " " + diaFin + " " +
                // horaFin + " " + minFin + " ");

                for (int i = 0; i < len; i++) {
                    coords[i] = Integer.parseInt(strCoords[i]); // pasando a enteros
                    // System.out.print(strCoords[i] + " ");
                }
                // System.out.println();

                CallesBloqueadas calleBloqueada = new CallesBloqueadas(id++, convertLocalDateTimeToMinutes(dateIni),
                        convertLocalDateTimeToMinutes(dateFin));

                // Agregando el identificador del nodo a la calle bloqueada
                for (int i = 0; i < len; i += 2) {
                    int x = coords[i];
                    int y = coords[i + 1];
                    calleBloqueada.addNode(x + 71 * y + 1);
                }

                listaCallesBloqueadas.add(calleBloqueada);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la cantidad de clusters necesarios para el algoritmo
     */
    public void obtenerCantidadClusters() {
        // cantMotos = listaMotos.size();
        // cantAutos = listaAutos.size();

        cantVehiculoTipo1 = listaVehiculoTipo1.size();
        cantVehiculoTipo2 = listaVehiculoTipo2.size();
        cantVehiculoTipo3 = listaVehiculoTipo3.size();
        cantVehiculoTipo4 = listaVehiculoTipo4.size();

        // System.out.println(listaChoferesMoto.size() + " " +
        // listaChoferesAuto.size());
        System.out.println(listaVehiculoTipo1.size() + " " + listaVehiculoTipo2.size() + " " + listaVehiculoTipo3.size()
                + " " + listaVehiculoTipo4.size());

        // TODO A: 2.5Tn B: 2Tn C: 1.5Tn D: 1Tn
        // TODO ENTENDER
        int k = (int) (0.9 * (cantidadProductos / (cantMotos * 4 + cantAutos * 25)));
        if (k > 10)
            k = 10;
        if (k < 3)
            k = 3;

        // TODO ENTENDER
        cantClusterMotos = cantMotos * k;
        cantClusterAutos = cantAutos * k;
    }

    /**
     * Obtiene la lista de adyancencia a partir de un archivo de texto
     */
    public void obtenerListaAdyacente() {
        int origen, destino;
        InputStream grafo = getClass().getClassLoader().getResourceAsStream("grafo.txt");
        Scanner sc = new Scanner(grafo);

        dijkstraAlgorithm = new Dijkstra(Configuraciones.V, listaCallesBloqueadas);
        for (int i = 0; i < Configuraciones.E; i++) {
            origen = sc.nextInt() + 1;
            destino = sc.nextInt() + 1;
            dijkstraAlgorithm.addEdge(origen, destino);
        }

        sc.close();
    }

    /**
     * Distribuye los pedidos en clusters
     */
    public void obtenerPedidosClusterizados() {
        int cantClusters = cantClusterMotos + cantClusterAutos;
        List<AVehiculo> vehiculos = inicializarVehiculos();

        // inicializar clusters
        List<Cluster> clustersList = inicializarClusters(vehiculos);
        List<Cluster> clustersAns = inicializarClusters(vehiculos);

        // Clusterizacion
        clusterResult = kmeans.kmeans(listaPedidos, clustersList, cantClusters, clustersAns);
        Double SSE = kmeans.getOptimo(listaPedidos, clustersAns, cantClusters);
        System.out.println("------------------------------------------------------");
        System.out.println("Rutas calculadas con un SSE=" + SSE);
        System.out.println("------------------------------------------------------");
        System.out.println();
        System.out.println();
    }

    // FALTA ENTENDER
    // FALTA CAMBIAR A SOLO AUTOS
    public List<AVehiculo> inicializarVehiculos() {
        List<AVehiculo> lista = new ArrayList<>();
        for (int i = 0; i < cantClusterMotos; i++) {
            AVehiculo vehiculo = new AVehiculo();
            vehiculo.setTipo("Moto");
            vehiculo.setCapacidad(4);
            vehiculo.setPeso(3.0);
            vehiculo.setVelocidad(60.00);
            vehiculo.setTipoId(2);
            lista.add(vehiculo);
        }
        for (int i = 0; i < cantClusterAutos; i++) {
            AVehiculo vehiculo = new AVehiculo();
            vehiculo.setTipo("Auto");
            vehiculo.setCapacidad(25);
            vehiculo.setPeso(5.0);
            vehiculo.setVelocidad(30.00);
            vehiculo.setTipoId(1);
            lista.add(vehiculo);
        }
        return lista;
    }

    /**
     * Inicializa los clusters
     */
    public List<Cluster> inicializarClusters(List<AVehiculo> vehiculos) {
        List<Cluster> lista = new ArrayList<Cluster>();
        for (AVehiculo vehiculo : vehiculos) {
            Cluster cluster = new Cluster();
            // TODO ENTENDER
            cluster.pedidos = new PriorityQueue<APedido>(500, new Comparator<APedido>() {
                // override compare method
                public int compare(APedido i, APedido j) {
                    // if(i.minFaltantes > j.minFaltantes) return 1;
                    // else if (i.minFaltantes < j.minFaltantes) return -1;
                    // else return 0;
                    if (Math.abs(i.x - Configuraciones.almacenX) + Math.abs(i.y - Configuraciones.almacenY) > Math
                            .abs(j.x - Configuraciones.almacenX) + Math.abs(j.y - Configuraciones.almacenY))
                        return 1;
                    else if (Math.abs(i.x - Configuraciones.almacenX) + Math.abs(i.y - Configuraciones.almacenY) < Math
                            .abs(j.x - Configuraciones.almacenX) + Math.abs(j.y - Configuraciones.almacenY))
                        return -1;
                    else if (i.cantidad > j.cantidad)
                        return 1;
                    else if (i.cantidad < j.cantidad)
                        return -1;
                    else
                        return 1;
                }
            });
            cluster.centroideX = 0;
            cluster.centroideY = 0;
            cluster.vehiculo = vehiculo;
            lista.add(cluster);
        }
        return lista;
    }

    /**
     * Obtiene las rutas
     */
    public void obtenerRutas() {
        // calculamos el tiempo en minutos en que iniciamos a correr el algoritmo
        LocalDateTime tiempo = LocalDateTime.now();
        LocalDateTime d1 = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);
        int tiempoMinutosInicio = (int) ChronoUnit.MINUTES.between(d1, tiempo);

        // para calcular el tiempo máximo de entrega
        int maximoTiempo = -1;

        // inicializamos la lista de rutas
        listaRutas = new ArrayList<Ruta>();

        for (Cluster cluster : clusterResult) {
            // asignamos el tiempo en minutos en que iniciamos a correr el algoritmo
            int tiempoMinutos = tiempoMinutosInicio;
            if (cluster.firstPedido == null)
                continue;
            // imprimos en forma de reporte la información relacionada a la ruta
            System.out.println("------------------------------------------------------");
            System.out.println("Cluster: " + cluster.vehiculo.getTipo());
            System.out.println("Capacidad: " + cluster.capacidad + "/" + cluster.vehiculo.getCapacidad());
            // System.out.println("Tiempo inicial en minutos: " + tiempoMinutos);
            System.out.println("------------------------------------------------------");

            // incializamos la ruta
            Ruta ruta = new Ruta(cluster.vehiculo, cluster.capacidad);

            // seteamos el origen a nuestro almacén
            int origen = Configuraciones.almacen;

            // nos servirá para hallar un ruta si estamos en un nodo bloqueado
            int ultimoViable = Configuraciones.almacen;

            // para el firstPedido
            if (cluster.firstPedido != null) {
                APedido pedido = cluster.firstPedido;
                ruta.addPedido(pedido);

                System.out.println("x:  " + pedido.x + "   y: " + pedido.y + "   z: " + pedido.minFaltantes
                        + "   cant: " + pedido.cantidad + "   idNodo: " + pedido.getNodoId());

                boolean estaBloqueada = estaBloqueada(tiempoMinutos, origen);

                if (estaBloqueada) {
                    System.out.println("Bloqueado!");
                    origen = ultimoViable;
                }

                dijkstraAlgorithm.dijkstra(origen, tiempoMinutos, (int) Math.round(cluster.vehiculo.getVelocidad()));
                System.out.printf("Ruta: ");

                int tamanoIni = ruta.recorrido.size();

                dijkstraAlgorithm.printShortestPath(pedido.getNodoId(), ruta, 1);

                int tamanoFin = ruta.recorrido.size();

                if (tamanoFin - tamanoIni >= 2) {
                    ultimoViable = ruta.recorrido.get(ruta.recorrido.size() - 2);
                }

                int tiempoEnLlegar = (tamanoFin - tamanoIni - 1) * 60
                        / ((int) Math.round(cluster.vehiculo.getVelocidad()));
                System.out.println("Nodos recorridos: " + (tamanoFin - tamanoIni - 1) + "   Tiempo llegada en minutos: "
                        + tiempoEnLlegar + " minutos");

                tiempoMinutos += tiempoEnLlegar;

                origen = pedido.getNodoId();

                if (cluster.pedidos.size() != 0)
                    System.out.println();
            }

            // iteramos mientras sacamos pedidos de la cola de prioridad del cluster
            // ordenados por distancia manhattan al almacén
            while (!cluster.pedidos.isEmpty()) {

                // extraemos un pedido del cluster
                APedido pedido = cluster.pedidos.poll();
                ruta.addPedido(pedido);
                // imprimir información del pedido
                System.out.println("x:  " + pedido.x + "   y: " + pedido.y + "   z: " + pedido.minFaltantes
                        + "   cant: " + pedido.cantidad + "   idNodo: " + pedido.getNodoId());

                // verificamos si nos encontramos en un nodo bloqueado
                // esto puede ocurrir ya que hemos entregado un pedido en un nodo bloqueado
                // o si el almancén es un nodo bloqueado
                boolean estaBloqueada = estaBloqueada(tiempoMinutos, origen);

                if (estaBloqueada) {
                    System.out.println("Bloqueado!");
                    origen = ultimoViable;
                    ruta.addNodo(origen);
                }

                // corremos el algoritmo de dijkstra
                dijkstraAlgorithm.dijkstra(origen, tiempoMinutos, (int) Math.round(cluster.vehiculo.getVelocidad()));
                System.out.printf("Ruta: ");

                // tamano antes de la nueva parte de la ruta
                int tamanoIni = ruta.recorrido.size();

                // obtenemos la ruta en un array
                dijkstraAlgorithm.printShortestPath(pedido.getNodoId(), ruta, 1);

                // tamano luego de la nueva parte de la ruta
                int tamanoFin = ruta.recorrido.size();

                // para obtener el último nodo que no está bloqueado si es que acabamos de
                // entregar un pedido en un nodo bloqueado
                if (tamanoFin - tamanoIni >= 2) {
                    ultimoViable = ruta.recorrido.get(ruta.recorrido.size() - 2);
                }

                // calculamos el tiempo que tomó en llegar
                int tiempoEnLlegar = (tamanoFin - tamanoIni - 1) * 60
                        / ((int) Math.round(cluster.vehiculo.getVelocidad()));
                System.out.println("Nodos recorridos: " + (tamanoFin - tamanoIni - 1) + "   Tiempo llegada en minutos: "
                        + tiempoEnLlegar + " minutos");

                // calculamos el nuevo tiempo en el que nos encontramos
                tiempoMinutos += tiempoEnLlegar;

                // cambiamos el origen
                origen = pedido.getNodoId();

                // detalle estético, la última línea no imprime una nueva en el reporte
                if (cluster.pedidos.size() != 0)
                    System.out.println();
            }

            // tiempo que tomó realizar la entrega
            int diferenciaTiempo = tiempoMinutos - tiempoMinutosInicio;

            if (diferenciaTiempo > maximoTiempo) {
                maximoTiempo = diferenciaTiempo;
            }
            System.out.println("------------------------------------------------------");
            System.out.println("Tiempo de entrega: " + diferenciaTiempo + " minutos");
            System.out.println("------------------------------------------------------");

            if (cluster.firstPedido != null) {
                System.out.println("Camino de retorno al almacén:  ");

                origen = ruta.recorrido.get(ruta.recorrido.size() - 1);
                boolean estaBloqueada = estaBloqueada(tiempoMinutos, origen);

                if (estaBloqueada) {
                    System.out.println("Bloqueado!");
                    origen = ultimoViable;
                    ruta.addNodoRetorno(origen);
                }

                dijkstraAlgorithm.dijkstra(origen, tiempoMinutos, (int) Math.round(cluster.vehiculo.getVelocidad()));

                int tamanoIni = ruta.retorno.size(); // FALTA ENTENDER EL TAMANOINI AQUI

                dijkstraAlgorithm.printShortestPath(Configuraciones.almacen, ruta, 2);
            }

            System.out.println();
            System.out.println();
            listaRutas.add(ruta);
        }
        System.out.println("Máximo tiempo de entrega: " + maximoTiempo + " minutos");
    }

    /**
     * Asigna rutas a choferes
     */
    public void asignarRutas() {
        int contadorAutos = 0;
        System.out.println("Asignar rutas: ");
        System.out.println("Size Chof Auto: " + listaChoferesAuto.size());
        System.out.println("Size Chof Moto: " + listaChoferesMoto.size());
        System.out.println("cantAutos: " + cantAutos);
        System.out.println("cantMotos: " + cantMotos);
        for (Usuario chofer : listaChoferesAuto) {
            if (contadorAutos == cantAutos)
                break;
            int minimo = Integer.MAX_VALUE;
            int contador = 0;
            int minCont = -1;
            for (Ruta ruta : listaRutas) {
                if (ruta.vehiculo.getTipoId() == 1 && ruta.chofer == null && minimo > ruta.tiempoMin) {
                    minimo = ruta.tiempoMin;
                    minCont = contador;
                }
                contador++;
            }
            if (minCont == -1)
                break;
            listaRutas.get(minCont).chofer = chofer;
            listaRutas.get(minCont).vehiculo = listaVehiculoTipo2.get(contadorAutos);
            System.out.println("MinCont: " + minCont);
            System.out.println("Auto: " + listaRutas.get(minCont).chofer);
            contadorAutos++;
        }

        int contadorMotos = 0;
        for (Usuario chofer : listaChoferesMoto) {
            if (contadorMotos == cantMotos)
                break;
            int minimo = Integer.MAX_VALUE;
            int contador = 0;
            int minCont = -1;
            for (Ruta ruta : listaRutas) {
                if (ruta.vehiculo.getTipoId() == 2 && ruta.chofer == null && minimo > ruta.tiempoMin) {
                    minimo = ruta.tiempoMin;
                    minCont = contador;
                }
                contador++;
            }
            if (minCont == -1)
                break;
            listaRutas.get(minCont).chofer = chofer;
            listaRutas.get(minCont).vehiculo = listaVehiculoTipo1.get(contadorMotos);
            System.out.println("MinCont: " + minCont);
            System.out.println("Auto: " + listaRutas.get(minCont).chofer);
            contadorMotos++;
        }

        // for(int i=listaRutas.size()-1; i>=0; i--){
        // if(listaRutas.get(i).chofer == null) listaRutas.remove(i);
        // else {
        // Ruta ruta = listaRutas.get(i);
        // AlgoRuta algoRuta = new AlgoRuta();
        // algoRuta.setInicio(LocalDateTime.now());
        // algoRuta.setDistancia(0.0);
        // algoRuta.setCosto(0.0);
        // algoRuta.setUsuario_id(ruta.chofer.getId());
        // algoRuta.setVehiculo_id(ruta.vehiculo.getId());
        // algoRuta.setEstado_id(2);
        // algoritmoRepository.save(algoRuta);
        // usuarioRepository.cambiarEstadoUsuario(algoRuta.getUsuario_id());
        // algoritmoRepository.cambiarEstadoVehiculo(algoRuta.getVehiculo_id());
        // int orden = 1;
        // for(APedido pedido: ruta.pedidos){
        // algoritmoRepository.cambiarEstadoPedido(pedido.id);
        // algoritmoRepository.insertarPedidoRuta(algoRuta.getId(), pedido.id, orden);
        // orden++;
        // }
        // orden = 1;
        // for(int nodo: ruta.recorrido){
        // algoritmoRepository.insertarNodoRuta(algoRuta.getId(),nodo,orden);
        // orden++;
        // }
        // for(int nodo: ruta.retorno){
        // algoritmoRepository.insertarNodoRuta(algoRuta.getId(),nodo,orden);
        // orden++;
        // }
        // }
        // System.out.println(i);
        // }
    }

    /**
     * Verifica si un nodo está bloqueado
     * 
     * @param tiempoMinutos el tiempo que lleva ejecutándose el algoritmo
     * @param nodoId        identificador del nodo a verificar
     * 
     * @return true o false dependiendo de si el nodo está bloqueado o no
     */
    private boolean estaBloqueada(int tiempoMinutos, int nodoId) {
        for (CallesBloqueadas par : listaCallesBloqueadas) {
            if ((tiempoMinutos >= par.getMinutosInicio()) && (tiempoMinutos < par.getMinutosFin())) {
                return par.estaNodo(nodoId);
            }
        }
        return false;
    }
}
