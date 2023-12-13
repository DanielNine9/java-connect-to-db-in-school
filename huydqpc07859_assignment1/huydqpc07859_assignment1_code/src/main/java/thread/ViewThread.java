/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author dinhh
 */
public class ViewThread extends Thread {

    private JLabel clockLabel;

    public ViewThread(JLabel clockLabel) {
        this.clockLabel = clockLabel;

    }

    @Override
    public void run() {
        while (true) {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss aa");
            this.clockLabel.setText(String.valueOf(dateFormat.format(now)));
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ViewThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
