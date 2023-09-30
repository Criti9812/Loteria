package Loteria;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.*;
import java.net.InetAddress;

public class LoteriaS{
    static ServerSocket servidor=null;
    static Socket jugador=null;
    public static ArrayList<Jugadores> todosLosJugadores=new ArrayList<Jugadores>();//Creamos un arrayListe de los hilos Jugadores
    public static ArrayList<PrintWriter> writers=new ArrayList<PrintWriter>();//Creamos un arrayListe de printWriters
    public static ArrayList<String>usuarios=new ArrayList<String>();//creamos un arry list de string

  public static void main(String[]args){
    final int puertoServ=5021;//Definimos el pueto del servidor
    int numeroCl=1;//Definimos un contador

    try{
      servidor=new ServerSocket(puertoServ);//creamos un socket servidor con el puerto predefinido

      //obtenemos la ip del socket servidor
      InetAddress direccion = servidor.getInetAddress();
      String direccionIP = direccion.getHostAddress();

      System.out.println("SERVIDOR INICIADO");
      System.out.println("Direccion IP: "+direccionIP);
      System.out.println("Puerto: "+puertoServ);

      while(true){//creamos un while que estara esperando a los jugadores
        jugador=servidor.accept();//Cada que un jugador entre al servidor se recibe y se le asigna un nuevo puerto
        Jugadores jugadorActual= new Jugadores(jugador, todosLosJugadores, writers, usuarios);//Se construye un hilo de jugadores y se le da algunos atributos
        todosLosJugadores.add(jugadorActual);//Se agrega el jugador actual al arreglo que guarda a todos los jugadores
        jugadorActual.start();//Se inicia el hilo que dara inicio al jugador
        System.out.println("El cliente "  + numeroCl + " se ha conectado!");
        numeroCl++;//Se le suma al contador cada que se recibe un jugador
      }
    }catch(Exception e){
      System.out.println("Error al crear el socket del servidor");
    }
  }
}

class Jugadores extends Thread{
  Scanner leer=new Scanner(System.in);
  private Socket jugador=null;
  private String name=null;
  private final ArrayList<Jugadores> todosLosJugadores;
  private final ArrayList<PrintWriter> writers;
  private final ArrayList<String> usuarios;
  private PrintWriter escritura;
  private BufferedReader lectura;
  private static int contador=0;
  public static int con=0;
  public static boolean lot=true;

  public Jugadores(Socket jugador, ArrayList<Jugadores> todosLosJugadores, ArrayList<PrintWriter> writers, ArrayList<String> usuarios){
    //Todos los atributos dados se reciben y se le asigana al hilo
    this.jugador=jugador;
    this.todosLosJugadores=todosLosJugadores;
    this.writers=writers;
    this.usuarios=usuarios;
  }

  public void run(){
    //Una vez creado el hilo e iniciado se asigna una variable sincronizada para llevar el control de todos los jugadores que entran correctamente
    synchronized (Jugadores.class) {
      contador++;
    }

    ArrayList<Jugadores> todosLosJugadores = this.todosLosJugadores;
    try{

      //Se crean los objetos de lectura y escritura
      lectura=new BufferedReader(new InputStreamReader(jugador.getInputStream() ));
      escritura=new PrintWriter(jugador.getOutputStream(),true);

      name=lectura.readLine();//Se recibe el nombre del jugador y se guarda en 8una variable
      usuarios.add(name);//Se agraga el nombre al arreglo que contiene el nombre de todos los usuarios
      escritura.println("Nombre de usuario registrado: " + name);
      for (PrintWriter writer : writers) {
          writer.println(name+" se ha unido al chat");
      }
      writers.add(escritura);

      while (true) {//Se crea un while el cual estara escuchando todo lo que manden los usuarios
          String textoL = lectura.readLine();//Se lee lo que el usuario mando
          if(textoL.startsWith("inicio")){//Si el ususario mando la palabra inicio se realizara lo siguiente
            synchronized (Jugadores.class) {//Se crea otra variable serializada y se va sumando
              con++;
            }
              Cartas c = new Cartas(contador,con,writers,lot,name);//Se construye el hilo con los atributos dados
              c.start();//Se da ininico al hilo
            }

          else if(textoL.startsWith("loteria")){//Si el ususario mando la palabra loteria se realizara lo siguiente
            System.out.println("loteria");
            Cartas.lot=false;//Se mcambia el valor de la variable lot que se encuentra en el hilo Cartas, esto para poder frenar su while que esta dentro
            for (PrintWriter writer : writers) {//Se manda a todos los jugadores la palabra gano
                writer.println("gano");
            }
            for (PrintWriter writer : writers) {//Se manda a todos los juagdores el mensaje dado
                writer.println("El jugador: "+name+" a ganado");
            }
          }
      }
    }catch(Exception ex){
      System.out.println("Exception: "+ex);
    }finally{
      if(name!=null){
        usuarios.remove(name);
        writers.remove(escritura);
      }
    }//finally
  }
}

class Cartas extends Thread{
    private final ArrayList<PrintWriter> writers;
    private static int contador;
    private static int con;
    public static boolean lot=true;
    private static String name;

    public Cartas(int contador, int con, ArrayList<PrintWriter> writers,boolean lot,String name){
      //Todos los atributos dados se reciben y se le asigana al hilo
      this.writers=writers;
      this.contador=contador;
      this.con=con;
      this.lot=lot;
      this.name=name;
    }

    public void run() {
      /*
      Se crea un random y un HashSet que es una estructura de datos que
      representa un conjunto de elementos únicos, es decir, que no permite elementos duplicados
      */
      Random random = new Random();
      HashSet<Integer> usedNumbers = new HashSet<>();
      int contadores=0;

      if(con==contador){//Si las dos variables son iguales se manda el mensaje ya a todos los usuarios
        for (PrintWriter writer : writers) {
          writer.println("ya");
        }

        while (lot) {//mientras qeu lot sea true el while es infinito

          //Se generan numeros aleatorios y si no son repetidos se mandan a todos los usuarios
          int time=0;
          int number = random.nextInt(54) + 1;
          if (!usedNumbers.contains(number)) {
              System.out.println(number);
              for (PrintWriter writer : writers) {
                  writer.println(number);
              }
              usedNumbers.add(number);
              time=2000;
              contadores++;
          }
          if(contadores==54){
            break;
          }
          try {
              Thread.sleep(time);//El hilo se duerme dependiendo el tiempo que se le asigne
          }catch (InterruptedException e) {
              e.printStackTrace();
          }
        }//end while
        System.out.println("new");
        Jugadores.con=0;//una vez terminado el while el contador del hilo jugadores se vuelve cero
      }
    }
  }
