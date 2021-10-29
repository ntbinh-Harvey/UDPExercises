/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Message;
import model.Worker;
import view.ServerView;

/**
 *
 * @author HarveyNguyen
 */
public class ServerControl {

    private ServerView view;
    private Connection con;
    private DatagramSocket myServer;
    private int serverPort = 5555;
    private DatagramPacket receivePacket = null;

    public ServerControl(ServerView view) {
        this.view = view;
        getDBConnection("ltm", "root", "");
        openServer(serverPort);
        view.showMessage("UDP server is running...");
        while (true) {
            listenning();
        }
    }

    private void getDBConnection(String dbName, String username, String password) {

        String dbUrl = "jdbc:mysql://localhost:3306/" + dbName;
        String dbClass = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection(dbUrl, username, password);
        } catch (Exception e) {
            view.showMessage(e.getStackTrace().toString());
        }
    }

    private void openServer(int portNumber) {
        try {
            myServer = new DatagramSocket(portNumber);
        } catch (IOException e) {
            view.showMessage(e.toString());
        }
    }

    private void listenning() {
        Message req = receiveData();
        Message res = null;
        if (req.getCode() == 1) {
            res = new Message(0, "", null, null, listAllHometown());
        } else if (req.getCode() == 2) {
            int addCheck = addWorker(req);
            if (addCheck > 0) {
                res = new Message(1, "Success", null, null);
            } else {
                res = new Message(0, "Fail", null, null);
            }
        } else if (req.getCode() == 3) {
            res = new Message(1, "Success", null, searchWorkerByFullname(req), null);
        } else if (req.getCode() == 4) {
            res = new Message(0, "Success", null, listWorkerByHometown(req), null);
        }
        sendData(res);
    }

    private void sendData(Message m) {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(m);
            oos.flush();
            InetAddress IPAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
            byte[] sendData = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData,
                    sendData.length, IPAddress, clientPort);

            myServer.send(sendPacket);
        } catch (Exception ex) {
            view.showMessage(ex.getStackTrace().toString());
        }
    }

    private Message receiveData() {
        Message rs = null;
        try {
            byte[] receivedData = new byte[1024];
            receivePacket = new DatagramPacket(receivedData, receivedData.length);
            myServer.receive(receivePacket);
            ByteArrayInputStream bais = new ByteArrayInputStream(receivedData);
            ObjectInputStream ois = new ObjectInputStream(bais);
            rs = (Message) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    private ArrayList<String> listAllHometown() {
        ArrayList<String> ns = new ArrayList<String>();
        String query = "SELECT name FROM tblHometown";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ns.add(rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ns;
    }

    private int addWorker(Message m) {
        //Lấy id quê quán
        int hometown = 0;
        String query = "SELECT id FROM tblHometown WHERE name = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, m.getW().getHometown());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                hometown = rs.getInt("id");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //add worker
        String sql = "INSERT INTO tblWorker(fullname, DoB, gender, coefficientsSalary, hometownId) VALUE (?,?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, m.getW().getFullname());
            ps.setString(2, m.getW().getDoB());
            ps.setString(3, m.getW().getGender());
            ps.setFloat(4, m.getW().getCoefficientsSalary());
            ps.setInt(5, hometown);
            int rs = ps.executeUpdate();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private ArrayList<Worker> searchWorkerByFullname(Message m) {
        ArrayList<Worker> ns = new ArrayList<Worker>();
        String query = "SELECT * FROM tblWorker, tblHometown WHERE tblWorker.hometownid = tblHometown.id AND tblWorker.fullname LIKE ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + m.getRequest() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Worker w = new Worker();
                w.setFullname(rs.getString("fullname"));
                w.setDoB(rs.getString("DoB"));
                w.setHometown(rs.getString("tblHometown.name"));
                w.setGender(rs.getString("gender"));
                w.setCoefficientsSalary(rs.getFloat("coefficientsSalary"));
                ns.add(w);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ns;
    }

    private ArrayList<Worker> listWorkerByHometown(Message m) {
        ArrayList<Worker> ns = new ArrayList<Worker>();
        String query = "SELECT * FROM tblWorker, tblHometown WHERE tblWorker.hometownid = tblHometown.id AND tblHometown.name = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, m.getRequest());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Worker w = new Worker();
                w.setFullname(rs.getString("fullname"));
                w.setDoB(rs.getString("DoB"));
                w.setHometown(rs.getString("tblHometown.name"));
                w.setGender(rs.getString("gender"));
                w.setCoefficientsSalary(rs.getFloat("coefficientsSalary"));
                ns.add(w);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ns;
    }
}
