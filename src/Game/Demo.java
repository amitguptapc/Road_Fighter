package Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;

public class Demo implements KeyListener {
  private static int currentLane=1;
  private static int newLane=1;
  private static boolean paused=false;
  private static boolean crashed=false;
  private static int crashCount=-0;
  private static long score=0;
  private static java.applet.AudioClip turn;

  public static void main(String[] args) {
    JFrame frame =new JFrame();
    JPanel panel=new JPanel();
    panel.setPreferredSize(new Dimension(660,790));
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    frame.setLocationRelativeTo(null);
    Graphics g=panel.getGraphics();
    panel.setFocusable(true);
    panel.requestFocus();
    Demo d1=new Demo();
    panel.addKeyListener(d1);

    java.applet.AudioClip theme = Applet.newAudioClip(Demo.class.getResource("../audios/theme.wav"));
    java.applet.AudioClip crash = Applet.newAudioClip(Demo.class.getResource("../audios/crash.wav"));
    turn=Applet.newAudioClip(Demo.class.getResource("../audios/turn.wav"));


    Image road1Image= null;
    Image road2Image= null;
    Image myCarImage= null;
    Image car1Image= null;
    Image car2Image= null;
    Image car3Image= null;
    Image car4Image= null;
    Image car5Image= null;
    Image crashImage= null;
    Image pauseImage= null;
    try{
      road1Image = ImageIO.read(Demo.class.getResource("../images/road.png"));
      road2Image = ImageIO.read(Demo.class.getResource("../images/road.png"));
      myCarImage = ImageIO.read(Demo.class.getResource("../images/mycar.png"));
      car1Image = ImageIO.read(Demo.class.getResource("../images/car1.png"));
      car2Image = ImageIO.read(Demo.class.getResource("../images/car2.png"));
      car3Image = ImageIO.read(Demo.class.getResource("../images/car3.png"));
      car4Image = ImageIO.read(Demo.class.getResource("../images/car4.png"));
      car5Image = ImageIO.read(Demo.class.getResource("../images/car5.png"));
      crashImage = ImageIO.read(Demo.class.getResource("../images/crash.png"));
      pauseImage = ImageIO.read(Demo.class.getResource("../images/pause.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    Random r1=new Random();
    Image imageArray[]= {car1Image,car2Image,car3Image,car4Image,car5Image};
    int laneArray[]={130 ,287,438};
    int yCord=-104;
    Image carShown=null;
    boolean traffic=false;
    int xcord=287;
    int carNum=1,laneNum=1,onLane=1;
    Rectangle myCarRect=new Rectangle(110,200);
    Rectangle shownCarRect=new Rectangle(110,200);
    theme.loop();
    int road1YCord=0;
    int road2YCord=-833;


    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // theme.loop();
    while(true) {
      try {
        Thread.sleep(70);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if(Demo.crashCount==1&&Demo.crashed){
        continue;
      }
      if(Demo.crashCount==2&&!Demo.crashed){
        yCord=-200;
        Demo.score=0;
        xcord=287;
        Demo.currentLane=Demo.newLane=1;
        Demo.crashCount=1;
        traffic=false;
      }
      if(Demo.paused){
        g.drawImage(pauseImage,200,280,null);
        continue;
      }

      Demo.score++;
      myCarRect.x=xcord;
      myCarRect.y=575;
      shownCarRect.x=onLane;
      shownCarRect.y=yCord;

      if(Demo.currentLane!=Demo.newLane){
        if(Demo.newLane>Demo.currentLane){
          xcord+=15;
          if(xcord>=laneArray[Demo.newLane])
          {
            xcord=laneArray[Demo.newLane];
            Demo.currentLane=Demo.newLane;
          }
        }
        if(Demo.newLane<Demo.currentLane){
          xcord-=15;
          if(xcord<=laneArray[Demo.newLane])
          {
            xcord=laneArray[Demo.newLane];
            Demo.currentLane=Demo.newLane;
          }
        }
      }

      if(!traffic){
        traffic=true;
        carNum=r1.nextInt(5);
        laneNum=r1.nextInt(3);
        carShown=imageArray[carNum];
        onLane=laneArray[laneNum];
      }

      road1YCord+=20;
      if(road1YCord>833)
        road1YCord=-833;
      road2YCord+=20;
      if(road2YCord>833)
        road2YCord=-833;
      g.drawImage(road1Image, 0, road1YCord, null);
      g.drawImage(road2Image, 0, road2YCord, null);
      g.drawImage(carShown, onLane, yCord, null);
      g.drawImage(myCarImage, xcord, 575, null);

      g.setColor(Color.BLACK);
      g.setFont(new Font("Arial",Font.BOLD,16));
      g.drawString("Your Score ",567,33);
      g.setFont(new Font("Arial",Font.BOLD,20));
      g.drawString(Demo.score+"",595,60);

      if(shownCarRect.intersects(myCarRect)){
        g.drawImage(crashImage,180,230,null);
        Demo.crashed=true;
        Demo.crashCount=1;
        crash.play();
      }

      yCord+=30;
      if(yCord>790) {//894
        traffic = false;
        yCord=-200;
      }

    }
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode()==KeyEvent.VK_LEFT&&!Demo.paused&&!Demo.crashed){
      turn.play();
      if(Demo.currentLane==1){
        Demo.newLane=0;
      }
      if(Demo.currentLane==2){
        Demo.newLane=1;
      }
    }
    if(e.getKeyCode()==KeyEvent.VK_RIGHT&&!Demo.paused&&!Demo.crashed){
      turn.play();
      if(Demo.currentLane==0){
        Demo.newLane=1;
      }
      if(Demo.currentLane==1){
        Demo.newLane=2;
      }
    }
    if(e.getKeyCode()==KeyEvent.VK_SPACE&&!Demo.crashed){
      Demo.paused=!Demo.paused;
    }
    if(e.getKeyCode()==KeyEvent.VK_ENTER&&Demo.crashed) {
      Demo.crashed=false;
      Demo.crashCount=2;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }
}