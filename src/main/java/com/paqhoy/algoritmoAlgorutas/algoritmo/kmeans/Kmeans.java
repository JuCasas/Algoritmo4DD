package com.paqhoy.algoritmoAlgorutas.algoritmo.kmeans;

import com.paqhoy.algoritmoAlgorutas.model.Cluster;
import com.paqhoy.algoritmoAlgorutas.model.APedido;
import com.paqhoy.algoritmoAlgorutas.model.Configuraciones;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class Kmeans {
    // Higher precision means earlier termination
    // and higher error
    static final Double PRECISION = 0.0;
    static final Double FACTORTIEMPO = 1.5;

    public Integer cantVehiculosTipo1;
    public Integer cantVehiculosTipo2;
    public Integer cantVehiculosTipo3;
    public Integer cantVehiculosTipo4;

    // TODO QUITAR ESTO
    public Integer cantMotos;
    public Integer cantAutos;

    public Kmeans(Integer cant1, Integer cant2, Integer cant3, Integer cant4) {
        this.cantVehiculosTipo1 = cant1;
        this.cantVehiculosTipo2 = cant2;
        this.cantVehiculosTipo3 = cant3;
        this.cantVehiculosTipo4 = cant4;

        // TODO QUITAR ESTO
//        this.cantAutos = cantAutos;
//        this.cantMotos = cantMotos;
    }

    /* K-Means++ implementation, initializes K centroids from data */
    private List<Cluster> kmeanspp(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            cluster.centroideX = (int) (Math.random() * 51);
            cluster.centroideY = (int) (Math.random() * 71);
            cluster.centroideZ = (int) (Math.random() * 1440);
        }

        return clusters;
    }

    private double distancia(List<Cluster> clusters, APedido pedido, int i) {

        int y2 = clusters.get(i).centroideY;
        int x2 = clusters.get(i).centroideX;
        int z2 = clusters.get(i).centroideZ;

        int y1 = pedido.y;
        int x1 = pedido.x;
        int z1 = pedido.minFaltantes;

        double distance;
        distance = Math.abs(y2 - y1) + Math.abs(x2 - x1);
        return distance;
    }

    private double euclideanDistanceXCluster(Cluster cluster, APedido pedido) {

        int y2 = cluster.centroideY;
        int x2 = cluster.centroideX;
        int z2 = cluster.centroideZ;

        int y1 = pedido.y;
        int x1 = pedido.x;
        int z1 = pedido.minFaltantes;

        double distance;
        distance = Math.abs(y2 - y1) + Math.abs(x2 - x1);
        return distance;
    }

    private double calculateTotalSSE(List<Cluster> clusters) {

        // TODO REVISAR Y MODIFICAR
        double totalsse = 0;
        int totalVehiculosTipo1 = 0;
        int totalVehiculosTipo2 = 0;
        int totalVehiculosTipo3 = 0;
        int totalVehiculosTipo4 = 0;
        int totalMotos = 0;
        int totalAutos = 0;

        List<APedido> listaVehiculosTipo1 = new ArrayList<>();
        List<APedido> listaVehiculosTipo2 = new ArrayList<>();
        List<APedido> listaVehiculosTipo3 = new ArrayList<>();
        List<APedido> listaVehiculosTipo4 = new ArrayList<>();
        List<APedido> listaMotos = new ArrayList<>();
        List<APedido> listaAuto = new ArrayList<>();
        int cont = 0;
        // log.info( "Cluster: " + clusters.get(0).firstPedido);
        for (Cluster cluster : clusters) {
            if (cluster.firstPedido == null) {
                cont++;
                continue;
            }
            cluster.firstPedido.idCluster = cont;

            if (cluster.vehiculo.getTipoId() == 1) {
                listaVehiculosTipo1.add(cluster.firstPedido);
            }
            if (cluster.vehiculo.getTipoId() == 2) {
                listaVehiculosTipo2.add(cluster.firstPedido);
            }
            if (cluster.vehiculo.getTipoId() == 3) {
                listaVehiculosTipo3.add(cluster.firstPedido);
            }
            if (cluster.vehiculo.getTipoId() == 4) {
                listaVehiculosTipo4.add(cluster.firstPedido);
            }

//            if (cluster.vehiculo.getTipoId() == 2)
//                listaMotos.add(cluster.firstPedido);
//            if (cluster.vehiculo.getTipoId() == 1)
//                listaAuto.add(cluster.firstPedido);

            cont++;
        }

        Collections.sort(listaVehiculosTipo1);
        Collections.sort(listaVehiculosTipo2);
        Collections.sort(listaVehiculosTipo3);
        Collections.sort(listaVehiculosTipo4);

//        Collections.sort(listaMotos);
//        Collections.sort(listaAuto);

        for (APedido pedido : listaVehiculosTipo1) {
            if (totalVehiculosTipo1 < cantVehiculosTipo1) {
                totalsse = totalsse + SSE(clusters.get(pedido.idCluster));
                totalVehiculosTipo1++;
            }
        }
        for (APedido pedido : listaVehiculosTipo2) {
            if (totalVehiculosTipo2 < cantVehiculosTipo2) {
                totalsse = totalsse + SSE(clusters.get(pedido.idCluster));
                totalVehiculosTipo2++;
            }
        }
        for (APedido pedido : listaVehiculosTipo3) {
            if (totalVehiculosTipo3 < cantVehiculosTipo3) {
                totalsse = totalsse + SSE(clusters.get(pedido.idCluster));
                totalVehiculosTipo3++;
            }
        }
        for (APedido pedido : listaVehiculosTipo4) {
            if (totalVehiculosTipo4 < cantVehiculosTipo4) {
                totalsse = totalsse + SSE(clusters.get(pedido.idCluster));
                totalVehiculosTipo4++;
            }
        }

        // TODO QUITAR ESTO
//        for (APedido pedido : listaMotos) {
//            if (totalMotos < cantMotos) {
//                totalsse = totalsse + SSE(clusters.get(pedido.idCluster));
//                totalMotos++;
//            }
//        }
//        for (APedido pedido : listaAuto) {
//            if (totalAutos < cantAutos) {
//                totalsse = totalsse + SSE(clusters.get(pedido.idCluster));
//                totalAutos++;
//            }
//        }
        return totalsse;
    }

    private double SSE(Cluster cluster) {

        double sumX = cluster.firstPedido.x;
        double sumY = cluster.firstPedido.y;
        double sum = 0;
        double media = euclideanDistanceXCluster(cluster, cluster.firstPedido);
        int cant = 1;

        // media
        for (APedido pedido : cluster.pedidos) {
            cant++;
            media = media + euclideanDistanceXCluster(cluster, pedido);
        }
        if (cant != 0)
            media = media / cant;
        else
            media = 0;

        for (APedido pedido : cluster.pedidos) {
            sum = sum + Math.pow((euclideanDistanceXCluster(cluster, pedido) - media), 2);
        }

        return sum;
    }

    private void cleanPedidos(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            cluster.firstPedido = null;
            cluster.capacidad = 0;
            cluster.pedidos = new PriorityQueue<APedido>(40, new Comparator<APedido>() {
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
        }
    }

    private void recomputeCentroids(List<Cluster> clusters) {
        int promX, promY, promZ, cant;
        for (Cluster cluster : clusters) {
            if (cluster.firstPedido == null) {
                cluster.centroideX = (int) (Math.random() * 51);
                cluster.centroideY = (int) (Math.random() * 71);
                cluster.centroideZ = (int) (Math.random() * 1440);
                continue;
            }
            promX = cluster.firstPedido.x;
            promY = cluster.firstPedido.y;
            promZ = cluster.firstPedido.minFaltantes;
            cant = 1;

            for (APedido pedido : cluster.pedidos) {
                cant = cant + 1;
                promX = promX + pedido.x;
                promY = promY + pedido.y;
                promZ = promZ + pedido.minFaltantes;
            }
            if (cant != 0) {
                cluster.centroideY = promY / cant;
                cluster.centroideX = promX / cant;
                cluster.centroideZ = promZ / cant;
            }
        }
    }

    /*
     * K-Means itself, it takes a dataset and a number K and adds class numbers to
     * records in the dataset
     */
    public List<Cluster> kmeans(List<APedido> pedidos, List<Cluster> clusters, int K, List<Cluster> clusterAns) {

        int runs = 100;
        double SSE = Double.MAX_VALUE; // Double.MAX_VALUE;
        while (runs-- != 0) {
            // Initialize Sum of Squared Errors to max, we'll lower it at each iteration
            cleanPedidos(clusters);
            // Select K initial centroids
            kmeanspp(clusters);
            int iterations = 30;
            while (iterations-- != 0) {
                // Assign observations to centroids
                // var records = data.getRecords();
                cleanPedidos(clusters);
                // For each record
                for (APedido pedido : pedidos) {
                    double minDist = Double.MAX_VALUE;
                    // Find the centroid at a minimum distance from it and add the record to its
                    // cluster
                    int asigned = -1;
                    boolean first = false;
                    for (int i = 0; i < K; i++) {
                        double dist = distancia(clusters, pedido, i);
                        if (dist < minDist) {
                            if (pedido.cantidad + clusters.get(i).capacidad <= clusters.get(i).vehiculo
                                    .getCapacidad()) {
                                minDist = dist;
                                if (asigned > -1) {
                                    if (first)
                                        clusters.get(asigned).firstPedido = null;
                                    else
                                        clusters.get(asigned).pedidos.remove(pedido);
                                    clusters.get(asigned).capacidad -= pedido.cantidad;
                                }
                                if (clusters.get(i).firstPedido == null) {
                                    clusters.get(i).firstPedido = pedido;
                                    clusters.get(i).capacidad += pedido.cantidad;
                                    asigned = i;
                                    first = true;
                                } else {
                                    clusters.get(i).setClusterNo(pedido);
                                    asigned = i;
                                    first = false;
                                }
                            }
                        }
                    }
                    // log.info( first ? "true" : "false");
                }
                // log.info( "Cluster: " + clusters.get(0).firstPedido);
                // Exit condition, SSE changed less than PRECISION parameter
                Double newSSE = calculateTotalSSE(clusters);

                if (newSSE < SSE) {
                    SSE = newSSE;
                    for (int i = 0; i < clusters.size(); i++) {

                        clusterAns.get(i).centroideX = clusters.get(i).centroideX;
                        clusterAns.get(i).centroideY = clusters.get(i).centroideY;
                        clusterAns.get(i).centroideZ = clusters.get(i).centroideZ;
                    }
                    System.out.println("newSSE: " + newSSE);
                }

                // Recompute centroids according to new cluster assignments
                recomputeCentroids(clusters);
            }
        }
        System.out.println("Rutas calculadas con un SSE=" + SSE);
        return clusterAns;
    }

    public Double getOptimo(List<APedido> pedidos, List<Cluster> clusters, int K) {
        for (APedido pedido : pedidos) {
            Double minDist = Double.MAX_VALUE;
            // Find the centroid at a minimum distance from it and add the record to its
            // cluster
            int asigned = -1;
            boolean first = false;
            for (int i = 0; i < K; i++) {
                Double dist = distancia(clusters, pedido, i);
                if (dist < minDist) {
                    if (pedido.cantidad + clusters.get(i).capacidad <= clusters.get(i).vehiculo.getCapacidad()) {
                        minDist = dist;
                        if (asigned > -1) {
                            if (first)
                                clusters.get(asigned).firstPedido = null;
                            else
                                clusters.get(asigned).pedidos.remove(pedido);
                            clusters.get(asigned).capacidad -= pedido.cantidad;
                        }
                        if (clusters.get(i).firstPedido == null) {
                            clusters.get(i).firstPedido = pedido;
                            clusters.get(i).capacidad += pedido.cantidad;
                            asigned = i;
                            first = true;
                        } else {
                            clusters.get(i).setClusterNo(pedido);
                            asigned = i;
                            first = false;
                        }
                    }
                }
            }
        }
        return calculateTotalSSE(clusters);
    }
}