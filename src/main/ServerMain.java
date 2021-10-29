/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import control.ServerControl;
import view.ServerView;

/**
 *
 * @author HarveyNguyen
 */
public class ServerMain {

    public static void main(String[] args) {
        ServerView view = new ServerView();

        ServerControl control = new ServerControl(view);
    }
}
