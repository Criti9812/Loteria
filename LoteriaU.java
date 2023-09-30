package Loteria;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

public class LoteriaU implements ActionListener{

  JFrame principal, inicioFondo;
  JButton im1, im2, im3, im4, im5, im6, im7, im8, im9, imG;
  JButton registrar, carta, iniciar, loteria;
  JLabel titulo, usuario, label,label2;
  JPanel panel,panel2;
  JTextField us;
  ImageIcon icon1,icon2,icon3,icon4,icon5,icon6,icon7,icon8,icon9,iconG,fondo;
  String cart1,cart2,cart3,cart4,cart5,cart6,cart7,cart8,cart9;
  int[] numerosAleatorios = new int[9];
  String[] numerosComoString;
  int comparador=0;
  int contadorr = 0;
  boolean lote=false;
  javax.swing.Timer timer = null;
  private Clip clip, clip2;

  private static BufferedReader recibir = null;
  private static PrintWriter enviar;

  int puerto = 5021;
  String host;
  Socket jugador=null;

  public void Empezar(){

    //Creamos la ventana de inicio y le damos sus parametros
    inicioFondo = new JFrame();
    inicioFondo.setSize(800,700);
    inicioFondo.setTitle("LOTERIA");
    inicioFondo.setDefaultCloseOperation(3);
    inicioFondo.setLocationRelativeTo(null);
    inicioFondo.setResizable(false);

    panel2 = new JPanel();
    panel2.setLayout(null);
    panel2.setBackground(Color.BLACK);
    inicioFondo.getContentPane().add(panel2);
    label2 = new JLabel();
    label2.setBounds(0,0,800,700);
    fondo= new ImageIcon("Loteria/Loteria/fondo4.jpeg");
    label2.setIcon(fondo);

    // Configurar el JLabel
    label = new JLabel("LOTERÍA");
    label.setForeground(Color.WHITE);
    label.setBackground(Color.BLACK);
    label.setOpaque(true); // Establecemos la opacidad del fondo del JLabel en true
    label.setFont(new Font("Arial", Font.BOLD, 50));//Se crea un estilo para el label y se le asigna
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setBounds(0,500,800,60);
    panel2.add(label);
    panel2.add(label2);
    inicioFondo.setVisible(true);

    // Configurar el Timer para la animación
    try {
      //Se crea un timer para crear una animacion donde aparece y desaparece un label cada segundo y medio
           timer = new javax.swing.Timer(1500, e -> {
              if (label.isVisible()) {
                  label.setVisible(false);
              } else {
                  label.setVisible(true);
              }
              contadorr++;

              if (contadorr == 6) { // detener después de 3 repeticiones (6 cambios)
                  timer.stop();//Se detiene el temporizador y se manda a llamar al metodo inicio
                  inicioFondo.dispose();
                  Inicio();
              }
          });
          timer.setRepeats(true);//indica que el temporizador debe repetirse después de cada ciclo completo,
          timer.setCoalesce(true);//indica que si el temporizador está configurado para dispararse varias veces durante un período de tiempo
          timer.start();//inicia el temporizador para que comience a ejecutarse en el momento
      } catch (Exception ex) {
          ex.printStackTrace();
      }
  }

  public void IP(){
    //Pedimos la ip del servidor y se guarda en una variable
    host = JOptionPane.showInputDialog(null, "IP del servidor:");
    if(host!=null){//Si el host ya se ingreso mandamos a llamar al metodo Empezar
      Empezar();
    }
  }


  public void Inicio(){

    try{
      clip=AudioSystem.getClip();//Construimos un objeto Clip
      clip.open(AudioSystem.getAudioInputStream(new File("Loteria//Loteria//sonidito.wav")));//Le damos al objeto clip un archivo wav
      clip.start();//iniciamos el objeto Clip
   }catch(Exception ev){
    System.out.println(ev.getMessage());
  }

  //Construimos la ventana principal y le damos sus parametros
  principal = new JFrame();
  principal.setSize(800,700);
  principal.setTitle("LOTERIA");
  principal.setDefaultCloseOperation(3);
  principal.setLocationRelativeTo(null);
  principal.setResizable(false);
  Componentes();
  principal.setVisible(true);
  }

  public void Componentes(){
    Panel();
    Label();
    Textos();
    Botones();
  }

  public void Panel(){
    panel = new JPanel();
    panel.setLayout(null);
    panel.setBackground(Color.BLACK);
    principal.getContentPane().add(panel);//le damos a la ventana principal un panel
  }

  public void Label(){
    titulo = new JLabel();
    titulo.setBounds(300,40,200,40);
    Font font = titulo.getFont();
    titulo.setFont(new Font("Algerian", Font.BOLD, 40));
    titulo.setForeground(Color.WHITE);
    titulo.setText("LOTERIA");
    panel.add(titulo);
  }

  public void Textos(){
    us = new JTextField();
    us.setBounds(550,35,150,30);
    panel.add(us);
  }

  public void Botones(){

      //Generamos numeros aleatorios y si no se repiten los guardamos en un arreglo
      Random random = new Random();
      for (int i = 0; i < numerosAleatorios.length; i++) {
          int numeroAleatorio;
          boolean numeroRepetido;

          do {
              numeroAleatorio = random.nextInt(54) + 1;
              numeroRepetido = false;

              for (int j = 0; j < i; j++) {
                  if (numeroAleatorio == numerosAleatorios[j]) {
                      numeroRepetido = true;
                      break;
                  }
              }
          } while (numeroRepetido);

          numerosAleatorios[i] = numeroAleatorio;
      }

    //Le asignamos a cada boton una imagen
    im1 = new JButton();
    icon1= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[0]+".png");
    im1.setIcon(icon1);
    im1.setBounds(40,100,100,160);
    im1.addActionListener(this);
    panel.add(im1);

    im2 = new JButton();
    icon2= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[1]+".png");
    im2.setIcon(icon2);
    im2.setBounds(160,100,100,160);
    im2.addActionListener(this);
    panel.add(im2);

    im3 = new JButton();
    icon3= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[2]+".png");
    im3.setIcon(icon3);
    im3.setBounds(280,100,100,160);
    im3.addActionListener(this);
    panel.add(im3);

    im4 = new JButton();
    icon4= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[3]+".png");
    im4.setIcon(icon4);
    im4.setBounds(40,280,100,160);
    im4.addActionListener(this);
    panel.add(im4);

    im5 = new JButton();
    icon5= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[4]+".png");
    im5.setIcon(icon5);
    im5.setBounds(160,280,100,160);
    im5.addActionListener(this);
    panel.add(im5);

    im6 = new JButton();
    icon6= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[5]+".png");
    im6.setIcon(icon6);
    im6.setBounds(280,280,100,160);
    im6.addActionListener(this);
    panel.add(im6);

    im7 = new JButton();
    icon7= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[6]+".png");
    im7.setIcon(icon7);
    im7.setBounds(40,460,100,160);
    im7.addActionListener(this);
    panel.add(im7);

    im8 = new JButton();
    icon8= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[7]+".png");
    im8.setIcon(icon8);
    im8.setBounds(160,460,100,160);
    im8.addActionListener(this);
    panel.add(im8);

    im9 = new JButton();
    icon9= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[8]+".png");
    im9.setIcon(icon9);
    im9.setBounds(280,460,100,160);
    im9.addActionListener(this);
    panel.add(im9);

    imG = new JButton();
    iconG= new ImageIcon("Loteria//Loteria2//1.png");
    imG.setIcon(iconG);
    imG.setBounds(480,100,200,320);
    panel.add(imG);

    carta = new JButton();
    carta.setText("CARTA");
    carta.addActionListener(this);
    carta.setBounds(480,440,200,40);
    panel.add(carta);

    iniciar = new JButton();
    iniciar.setText("INICIAR");
    iniciar.setBounds(480,490,200,40);
    iniciar.addActionListener(this);
    panel.add(iniciar);

    loteria = new JButton();
    loteria.setText("LOTERIA");
    loteria.setBounds(480,540,200,40);
    loteria.addActionListener(this);
    panel.add(loteria);

    registrar = new JButton();
    registrar.setBounds(701,35,30,30);
    registrar.addActionListener(this);
    panel.add(registrar);
  }

  public void actionPerformed(ActionEvent e){

    //Si el clip no esta reproduciendo nada se vuelve a reproducir al oprimir un boton
    if (!clip.isRunning()) {
        clip.start();
    }

    //le asignamos a cada evento de cada uno de los botones un clip, para que cuando se de click en uno se reproduzca
    try{
      clip2=AudioSystem.getClip();
      clip2.open(AudioSystem.getAudioInputStream(new File("Loteria//Loteria//sonidito2.wav")));
      clip2.start();
    }catch(Exception evv){
     System.out.println(evv.getMessage());
    }

    if(e.getSource()==carta){//Si se presiona el boton carta se va a realizar todo esto
      panel.remove(im1); // Elimina el botón del panel
      panel.remove(im2);
      panel.remove(im3);
      panel.remove(im4);
      panel.remove(im5);
      panel.remove(im6);
      panel.remove(im7);
      panel.remove(im8);
      panel.remove(im9);
      panel.revalidate(); // Revalida el panel para actualizar la interfaz
      panel.repaint(); // Repinta el panel para actualizar la interfaz

  //Generamos numeros aleatorios y si no se repiten los guardamos en un arreglo
        Random random = new Random();
        for (int i = 0; i < numerosAleatorios.length; i++) {
            int numeroAleatorio;
            boolean numeroRepetido;

            do {
                numeroAleatorio = random.nextInt(54) + 1;
                numeroRepetido = false;

                for (int j = 0; j < i; j++) {
                    if (numeroAleatorio == numerosAleatorios[j]) {
                        numeroRepetido = true;
                        break;
                    }
                }
            } while (numeroRepetido);

            numerosAleatorios[i] = numeroAleatorio;
        }

    //Le asignamos a cada boton una imagen
      im1 = new JButton();
      icon1= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[0]+".png");
      im1.setIcon(icon1);
      im1.setBounds(40,100,100,160);
      im1.addActionListener(this);
      panel.add(im1);

      im2 = new JButton();
      icon2= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[1]+".png");
      im2.setIcon(icon2);
      im2.setBounds(160,100,100,160);
      im2.addActionListener(this);
      panel.add(im2);

      im3 = new JButton();
      icon3= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[2]+".png");
      im3.setIcon(icon3);
      im3.setBounds(280,100,100,160);
      im3.addActionListener(this);
      panel.add(im3);

      im4 = new JButton();
      icon4= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[3]+".png");
      im4.setIcon(icon4);
      im4.setBounds(40,280,100,160);
      im4.addActionListener(this);
      panel.add(im4);

      im5 = new JButton();
      icon5= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[4]+".png");
      im5.setIcon(icon5);
      im5.setBounds(160,280,100,160);
      im5.addActionListener(this);
      panel.add(im5);

      im6 = new JButton();
      icon6= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[5]+".png");
      im6.setIcon(icon6);
      im6.setBounds(280,280,100,160);
      im6.addActionListener(this);
      panel.add(im6);

      im7 = new JButton();
      icon7= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[6]+".png");
      im7.setIcon(icon7);
      im7.setBounds(40,460,100,160);
      im7.addActionListener(this);
      panel.add(im7);

      im8 = new JButton();
      icon8= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[7]+".png");
      im8.setIcon(icon8);
      im8.setBounds(160,460,100,160);
      im8.addActionListener(this);
      panel.add(im8);

      im9 = new JButton();
      icon9= new ImageIcon("Loteria//Loteria//"+numerosAleatorios[8]+".png");
      im9.setIcon(icon9);
      im9.setBounds(280,460,100,160);
      im9.addActionListener(this);
      panel.add(im9);

      panel.revalidate(); // Revalida el panel para actualizar la interfaz
      panel.repaint(); // Repinta el panel para actualizar la interfaz
    }//end if

    if(e.getSource()==im1 ){
      comparador++;//Cada que se presione un boton se sumara el contador
      im1.setEnabled(false); // deshabilitar el boton 1
    }//end if

    if(e.getSource()==im2 ){
      comparador++;
      im2.setEnabled(false); // deshabilitar el boton 1
    }//end if

    if(e.getSource()==im3 ){
      comparador++;
      im3.setEnabled(false); // deshabilitar el boton 1
    }//end if

    if(e.getSource()==im4 ){
      comparador++;
      im4.setEnabled(false); // deshabilitar el boton 1
    }//end if

    if(e.getSource()==im5 ){
      comparador++;
      im5.setEnabled(false); // deshabilitar el boton 1
    }//end if

    if(e.getSource()==im6 ){
      comparador++;
      im6.setEnabled(false); // deshabilitar el boton 1
    }//end if

    if(e.getSource()==im7 ){
      comparador++;
      im7.setEnabled(false); // deshabilitar el boton 1
    }//end if

    if(e.getSource()==im8 ){
      comparador++;
      im8.setEnabled(false); // deshabilitar el boton 1
    }//end if

    if(e.getSource()==im9 ){
      comparador++;
      im9.setEnabled(false); // deshabilitar el boton 1
    }//end if

    if(e.getSource()==registrar){//Una vez que se pesione el boton de registar se llamara al metodo conect
      conect();
    }//end if

    if(e.getSource()==iniciar){//Una vez que se pesione el boton de iniciar se hara todo esto
      registrar.setEnabled(false); // deshabilitar el boton 1
      carta.setEnabled(false); // deshabilitar el boton 1
      iniciar.setEnabled(false); // deshabilitar el boton 1
      enviar.println("inicio");//se envia la palabra inicio al servidor
      String chale=null;

      while(true){
      try {
          String lines;
          while ((lines = recibir.readLine()) != null) {//mientras que el usuario este recibiendo se hara lo que este dentro del while
            chale=lines;
            if(chale.equals("ya")){//Si el usuario recibe la palabra ya el while se frena
              break;
            }
          }//end while

          break;
      }catch (IOException exd) {
          System.out.println("Error al recibir mensajes del servidor: " + exd.getMessage());
          System.exit(1);
      }
    }//end While

    In();//se manda a llamar al metodo In
    }//end if

    if(e.getSource()==loteria){
      if(lote){//Una vez oprimido el boton loteria y la variable lote sea verdadera se hace lo que este en el if
        enviar.println("loteria");//Se manda la palabra loteria al servidor
      }
    }//end if
  }

  public void conect(){

    try{
      jugador=new Socket(host, puerto);//Se crea un socket con el host ya predefinido y el pueto dado
      enviar=new PrintWriter(jugador.getOutputStream(),true );//Se crea un Printwriter con el metodo getOutputStream para poder enviar al servidor
      recibir=new BufferedReader(new InputStreamReader(jugador.getInputStream() ));//Se crea un BufferedReader con el metodo getInputStream() para poder recibir del servido
      String nombreS=(String)us.getText();
      enviar.println(nombreS);//despues de conectarse se envia el nombre del usuario
      //Si conexion fue exitosa se manda un mensaje al usuario
      JOptionPane.showMessageDialog(null, "Te has conectado exitosamente");
    }catch(Exception e){
      //Si ocurre un error al conectarse se manda un mensaje al usuario
      JOptionPane.showMessageDialog(null, "Hubo un error al Conectarse");
    }
      registrar.setEnabled(false);//El boton regisrar queda inhabilitgado
  }

  public void In(){
    //Creamos un objeto array de tipo String para del tamaño del arreglo de los numeros aleatorios ya creado
    numerosComoString = new String[numerosAleatorios.length];

    //Le mandamos todos los datos del arreglo de los numeros aleatorias al nuevo areglo
    for (int i = 0; i < numerosAleatorios.length; i++) {
      numerosComoString[i] = String.valueOf(numerosAleatorios[i]);
    }

    //Creamos un hilo
    new Thread(new Runnable(){
        public void run() {
            try {
                String line;
                while ((line = recibir.readLine()) != null) {//El metodo while se ejecutara mientras este recibiendo algo del servidor
                  if(!line.equals("gano")){//si el mensaje recibido es diferente de la palabra gano se ejecutara todo lo del if
                  panel.remove(imG);//Se elimina el boton imG
                  panel.revalidate(); // Revalida el panel para actualizar la interfaz
                  panel.repaint(); // Repinta el panel para actualizar la interfaz
                  imG = new JButton();
                  iconG= new ImageIcon("Loteria//Loteria2//"+line+".png");
                  imG.setIcon(iconG);
                  imG.setBounds(480,100,200,320);
                  panel.add(imG);
                  panel.revalidate(); // Revalida el panel para actualizar la interfaz
                  panel.repaint(); // Repinta el panel para actualizar la interfaz

                  //Reproduciomos un sonido
                  try{
                    clip=AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(new File("Loteria//Loteria//sonidito3.wav")));
                    clip.start();
                  }catch(Exception ev){
                  System.out.println(ev.getMessage());
                  }

                  //Dentro de este for comparamos la palabra recibida del servidor con nuestras palabras del arreglo contruido arriba
                      for (int i = 0; i < numerosComoString.length; i++) {
                          if (numerosComoString[i].equalsIgnoreCase(line)) {
                            //Eliminamos la palabra encontrada del arrego llamando al metodo
                              numerosComoString = eliminarElemento(numerosComoString, i);
                              break;
                          }
                      }

                      //Si el arreglo queda vacio y el comparador esta igualado a nueve habilitamos la variable lote
                      if (numerosComoString.length == 0  &&  comparador==9) {
                        lote=true;
                      }
                }//end while

                //Si recibimos la palabra gano y ademas el arrego esta vacio y el contador es igual a nueve se manda un mensaje
                else if (numerosComoString.length == 0  &&  comparador==9 && line.equals("gano")) {
                  JOptionPane.showMessageDialog(null, "Has ganado muchas felicidades");
                  lote=false;
                  break;
                }

                //si se recibe la palabra gano solamente se recibe un mensaje del servidor y se imprime un mensaje
                else if(line.equals("gano")){
                  line = recibir.readLine();
                  JOptionPane.showMessageDialog(null, line);
                  lote=false;
                  break;
                }
                }

                comparador=0;
                //habilitamos cada uno de los botones para poder jugar de nuevo
                im1.setEnabled(true);
                im2.setEnabled(true);
                im3.setEnabled(true);
                im4.setEnabled(true);
                im5.setEnabled(true);
                im6.setEnabled(true);
                im7.setEnabled(true);
                im8.setEnabled(true);
                im9.setEnabled(true);
                carta.setEnabled(true); // deshabilitar el boton 1
                iniciar.setEnabled(true); // deshabilitar el boton 1

            } catch (IOException e) {
                System.out.println("Error al recibir mensajes del servidor: " + e.getMessage());
                System.exit(1);
            }
        }
    }).start();
  }

//Creamos un metodo que haga la busqueda del indice dado para poder ser eliminado y se retorna ese arreglo
  public static String[] eliminarElemento(String[] arr, int indice) {
      String[] nuevoArr = new String[arr.length - 1];
      for (int i = 0, j = 0; i < arr.length; i++) {
          if (i != indice) {
              nuevoArr[j++] = arr[i];
          }
      }
      return nuevoArr;
  }

  public static void main(String[]args){
    LoteriaU l = new LoteriaU();//Se crea un objeto de la clase principal
    l.IP();//se manda a llamar el metodo IP
  }//end main
}//end class
