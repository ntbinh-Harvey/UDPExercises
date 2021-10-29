/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import model.Message;
import view.ClientView;

/**
 *
 * @author HarveyNguyen
 */
public class ClientControl {

    private ClientView view;
    private int serverPort = 5555;
    private int clientPort = 6666;
    private String serverHost = "localhost";
    private DatagramSocket myClient;

    public ClientControl(ClientView view) {
        this.view = view;
        this.view.addWorker(new addWorker());
        this.view.searchByName(new searchByName());
        this.view.listSameHTWorker(new listSameHTWorker());
        getHomeTown();
    }

    private void openConnection() {
        try {

            myClient = new DatagramSocket(clientPort);
        } catch (Exception ex) {
            view.showMessage(ex.getStackTrace().toString());
        }
    }

    private void closeConnection() {
        try {

            myClient.close();
        } catch (Exception ex) {
            view.showMessage(ex.getStackTrace().toString());
        }
    }

    public void sendData(Message m) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(m);
            oos.flush();
            InetAddress IPAddress = InetAddress.getByName(serverHost);
            byte[] sendData = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
            myClient.send(sendPacket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Message receiveData() {
        Message result = new Message();
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            myClient.receive(receivePacket);
            ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
            ObjectInputStream ois = new ObjectInputStream(bais);
            result = (Message) ois.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public void getHomeTown() {
        Message m = new Message(1);
        openConnection();
        sendData(m);
        Message res = receiveData();
        view.takeList(res.getHometown());
        closeConnection();
    }

    class addWorker implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                Message m = new Message();
                m.setW(view.sendWorker());
                m.setCode(2);
                openConnection();
                sendData(m);
                Message res = receiveData();
                closeConnection();
                if (res.getCode() == 1) {
                    view.clearDataPreviousAddedWorker();
                    view.displaySuccess();
                } else {
                    view.displayFail();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class searchByName implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                view.clearDisplay(1);
                Message m = new Message();
                m.setRequest(view.sendName());
                m.setCode(3);
                openConnection();
                sendData(m);
                Message res = receiveData();
                closeConnection();
                view.displayTable(res.getLw(), res.getCode());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    class listSameHTWorker implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                view.clearDisplay(0);
                Message m = new Message();
                m.setRequest(view.sendHT());
                m.setCode(4);
                openConnection();
                sendData(m);
                Message res = receiveData();
                closeConnection();
                view.displayTable(res.getLw(), res.getCode());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
