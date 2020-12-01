package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public final class ConcurrentGUI extends JFrame {

        private static final long serialVersionUID = 1L;
        private static final double WIDTH_PERC = 0.2;
        private static final double HEIGHT_PERC = 0.1;
        private final JLabel display = new JLabel();
        private final JButton up = new JButton("Up");
        private final JButton down = new JButton("Down");
        private final JButton stop = new JButton("Stop");

        public ConcurrentGUI() {
            super();
            final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            final JPanel panel = new JPanel();
            panel.add(display);
            panel.add(up);
            panel.add(down);
            panel.add(stop);
            this.getContentPane().add(panel);
            this.setVisible(true);

            final Agent agent = new Agent();
            new Thread(agent).start();

            //pulsante stop
            stop.addActionListener(e -> {
                    agent.stopCounting();
                    stop.setEnabled(false);
                    up.setEnabled(false);
                    down.setEnabled(false);
            });
            //pulsante up
            up.addActionListener(e -> agent.upCounting());

            //pulsante down
            down.addActionListener(e -> agent.downCounting());
        }

private class Agent implements Runnable {

       private volatile boolean updown = true;
       private volatile boolean stop;
       private volatile int counter;

       @Override
       public void run() {
           while (!this.stop) {
               try {
                   //contatore che va avanti di default
                   SwingUtilities.invokeLater(() -> ConcurrentGUI.this.display.setText(Integer.toString(Agent.this.counter)));
                   if (this.updown) {
                       this.counter++;
                   } else {
                       this.counter--;
                   }

                   Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                  }
           }

       }

       public void stopCounting() {
           this.stop = true;
       }

       public void upCounting() {
           this.updown = true;
       }

       public void downCounting() {
           this.updown = false;
       }
   }
} 
