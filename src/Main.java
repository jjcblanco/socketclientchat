
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static JFrame f;
    public static JTextField tf;
    public static JTextArea  recibido;
    public static JLabel iplocal;
    public static JLabel recibidoTiempo;

    public static void main(String[] args) throws IOException {
        Socket s=null;
        try {
            Scanner scn = new Scanner(System.in);

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056

                  s = new Socket(ip, 5056);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            Main.inicializar_frame(s,dis,dos);


            // the following loop performs the exchange of
            // information between client and client handler
            while (true) {

                System.out.println(dis.readUTF());
                //String tosend = scn.nextLine();
                //dos.writeUTF(tosend);

                // If client sends exit,close this connection
                // and then break from the while loop
               /* if (tosend.equals("Exit")) {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
*/
                // printing date or time as requested by client
                String received = dis.readUTF();

                recibido.append(received);
                recibido.append("\n");
                System.out.println(received);
            }

            // closing resources
            //scn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void inicializar_frame(Socket s,DataInputStream dis,DataOutputStream dos) {
        {
            f=new JFrame("Cliente chat");
            f.getContentPane().setBackground(Color.YELLOW);
            tf=new JTextField();
            tf.setBounds(600,80, 150,20);


            JTextField mensajeTf=new JTextField();
            mensajeTf.setBounds(100,80, 300,30);
            mensajeTf.setFont(new Font("Optima", Font.BOLD, 20));

            JLabel titulo=new JLabel("Cliente Chat");
            titulo.setBounds(10,10, 250,50);
            titulo.setFont(new Font("Serif", Font.BOLD, 44));

            JLabel iplabel=new JLabel("ip local: ");
            iplabel.setBounds(540,20, 100,30);

            iplocal=new JLabel(" ");
            iplocal.setBounds(640,20, 100,30);
            try {
                iplocal.setText(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            recibidoTiempo=new JLabel("datos recibidos");
            recibidoTiempo.setBounds(100,580, 100,30);

            recibido= new JTextArea ("");
            recibido.setBounds(100,140, 100,300);
            titulo.setFont(new Font("Arial", Font.BOLD, 24));

            JLabel servidoriplabel=new JLabel("Servidor: ");
            servidoriplabel.setBounds(530,75, 100,30);


            JButton btnFecha=new JButton("Fecha");
            btnFecha.setBounds(640,180,95,30);

            btnFecha.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    try {
                        dos.writeUTF("Date");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            JButton btnHora=new JButton("Hora");
            btnHora.setBounds(640,220,95,30);

            btnHora.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    try {
                        dos.writeUTF("Time");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            JButton desconectar=new JButton("Desconectar");
            desconectar.setBounds(640,320,95,30);

            desconectar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try {
                        s.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    tf.setText(" Desconectado");
                }
            });

            JButton salir=new JButton("Salir");
            salir.setBounds(640,370,95,30);

            salir.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try {
                        dos.writeUTF("Exit");
                        s.close();
                        dis.close();
                        dos.close();
                        f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    tf.setText(" Desconectado");
                }
            });

            f.add(btnFecha);
            f.add(tf);
            f.add(mensajeTf);
            f.add(btnHora);

            f.add(desconectar);
            f.add(salir);
            f.add(iplabel);
            f.add(iplocal);
            f.add(servidoriplabel);
            f.add(recibido);
            f.add(titulo);
            f.setSize(800,800);
            //f.setExtendedState(f.getExtendedState() | JFrame.MAXIMIZED_BOTH);

            f.setLayout(null);
            f.setVisible(true);
        }
    }
}

