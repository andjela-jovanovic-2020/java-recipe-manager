/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import baza.Database;
import gui.GlavniProzor;
import java.util.Scanner;
import rad.ObradaRecepta;
import rad.ObradaSastojka;        
import recept.Kategorija;
import recept.Recept;                              
        
public class Main {

    public static void main(String[] args) {
        
        Database.connect();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GlavniProzor().setVisible(true);
            }
        });
        
        
    }
    
}
