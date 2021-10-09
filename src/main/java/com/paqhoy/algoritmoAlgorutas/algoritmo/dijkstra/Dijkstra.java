package com.paqhoy.algoritmoAlgorutas.algoritmo.dijkstra;

import com.paqhoy.algoritmoAlgorutas.model.Bloqueado;
import com.paqhoy.algoritmoAlgorutas.model.CallesBloqueadas;
import com.paqhoy.algoritmoAlgorutas.model.Ruta;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra{

    //similar a los defines de C++
    private final int MAX = 3622;  //maximo numero de vértices
    private final int INF = 1<<30;  //definimos un valor grande que represente la distancia infinita inicial, basta conque sea superior al maximo valor del peso en alguna de las aristas

    private List<List< Node >> ady = new ArrayList< List< Node > >(); //lista de adyacencia
    private int distancia[ ] = new int[ MAX ];          //distancia[ u ] distancia de vértice inicial a vértice con ID = u
    private boolean visitado[ ] = new boolean[ MAX ];   //para vértices visitados
    private PriorityQueue< Node > Q = new PriorityQueue<Node>(); //priority queue propia de Java, usamos el comparador definido para que el de menor valor este en el tope
    private int V;                                      //numero de vertices
    private int previo[] = new int[ MAX ];              //para la impresion de caminos
    private boolean dijkstraEjecutado;
    private List<CallesBloqueadas> listaCallesBloqueadas;

    public Dijkstra(int V, List<CallesBloqueadas> listaCallesBloqueadas){
        this.V = V;
        for( int i = 0 ; i <= V ; ++i )
            ady.add(new ArrayList<Node>()) ; //inicializamos lista de adyacencia
        dijkstraEjecutado = false;
        this.listaCallesBloqueadas = listaCallesBloqueadas;
    }

    //función de inicialización
    private void init(){
        for( int i = 0 ; i <= V ; ++i ){
            distancia[ i ] = INF;  //inicializamos todas las distancias con valor infinito
            visitado[ i ] = false; //inicializamos todos los vértices como no visitados
            previo[ i ] = -1;      //inicializamos el previo del vertice i con -1
        }
    }

    //Paso de relajacion
    private void relajacion( int actual , int adyacente , int peso ){
        //Si la distancia del origen al vertice actual + peso de su arista es menor a la distancia del origen al vertice adyacente
        if( distancia[ actual ] + peso < distancia[ adyacente ] ){
            distancia[ adyacente ] = distancia[ actual ] + peso;  //relajamos el vertice actualizando la distancia
            previo[ adyacente ] = actual;                         //a su vez actualizamos el vertice previo
            Q.add( new Node( adyacente , distancia[ adyacente ] ) ); //agregamos adyacente a la cola de prioridad
        }
    }

    public void dijkstra( int inicial, int tiempoMinutos, int velocidad ){
        init(); //inicializamos nuestros arreglos
        Q.add( new Node( inicial , 0 ) ); //Insertamos el vértice inicial en la Cola de Prioridad
        distancia[ inicial ] = 0;      //Este paso es importante, inicializamos la distancia del inicial como 0
        int actual , adyacente , peso, tiempoTranscurrido;
        while( !Q.isEmpty() ){                   //Mientras cola no este vacia
            actual = Q.element().first;            //Obtengo de la cola el nodo con menor peso, en un comienzo será el inicial
            Q.remove();                           //Sacamos el elemento de la cola
            if( visitado[ actual ] ) continue; //Si el vértice actual ya fue visitado entonces sigo sacando elementos de la cola
            visitado[ actual ] = true;         //Marco como visitado el vértice actual
            tiempoTranscurrido = distancia[ actual ] * 60 / ( velocidad );
            tiempoTranscurrido = tiempoMinutos + tiempoTranscurrido;
            boolean estaBloqueada = estaBloqueada(tiempoTranscurrido, actual);
            //   System.out.println("bloqueado: " + estaBloqueada);
            for( int i = 0 ; i < ady.get( actual ).size() ; ++i ){ //reviso sus adyacentes del vertice actual
                adyacente = ady.get( actual ).get( i ).first;   //id del vertice adyacente
                if(estaBloqueada) peso = INF;
                else peso = ady.get( actual ).get( i ).second;        //peso de la arista que une actual con adyacente ( actual , adyacente )
                if( !visitado[ adyacente ] ){        //si el vertice adyacente no fue visitado
                    relajacion( actual , adyacente , peso ); //realizamos el paso de relajacion
                }
            }
        }
        dijkstraEjecutado = true;
    }

    public void addEdge( int origen , int destino){
        //llenando lista de adyacencia
        ady.get( origen ).add( new Node( destino , 1 ) );
    }

    public void printShortestPath(int destino, Ruta ruta, int tipo){
        if( !dijkstraEjecutado ){
            System.out.println("Es necesario ejecutar el algorithmo de Dijkstra antes de poder imprimir el camino mas corto");
            return;
        }
        print( destino, ruta, tipo );
        //   System.out.printf("\n");
    }

    //Impresion del camino mas corto desde el vertice inicial y final ingresados
    private void print(int destino, Ruta ruta, int tipo ){
        if( previo[ destino ] != -1 ){    //si aun poseo un vertice previo
            print( previo[ destino ], ruta, tipo );  //recursivamente sigo explorando
        }
        if( previo[ destino ] != -1 ){
            if(tipo == 1) ruta.addNodo(destino);
            else ruta.addNodoRetorno(destino);
        }
        //   System.out.printf("%d " , destino );        //terminada la recursion imprimo los vertices recorridos
    }

    public int getNumberOfVertices() {
        return V;
    }

    public void setNumberOfVertices(int numeroDeVertices) {
        V = numeroDeVertices;
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
