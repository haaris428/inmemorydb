package com.brainfish;

import com.brainfish.service.DBService;
import com.brainfish.service.DBServiceImpl;

import java.util.Scanner;

public class InMemoryDBApplication
{
    public static void main(String args[]) {
        try {
            Scanner command = new Scanner(System.in);

            DBService dbService = new DBServiceImpl();
            System.out.println("Enter command: ");
            boolean running = true;

            while(running){
                String in = command.nextLine();
                String[] c = in.split(" ");

                String cmd = c[0].toUpperCase();
                String key = "";
                String value = "";
                if (c.length == 2) {
                    key = c[1];
                }

                if (c.length == 3) {
                    key =  c[1];
                    value = c[2];
                }
                switch(cmd){
                    case ("SET"):
                        dbService.set(key,value);
                        break;

                    case "DELETE":
                        dbService.delete(key);
                        break;

                    case "GET":
                        String v = dbService.get(key);
                        System.out.println("Value: " + v);
                        break;

                    case "COUNT":
                        int count = dbService.count(key);
                        System.out.println("Count " + count);
                        break;

                    case "BEGIN":
                        dbService.beginTransaction();
                        break;

                    case "ROLLBACK":
                        dbService.rollbackTransaction();
                        break;

                    case "COMMIT":
                        dbService.commitTransaction();
                        break;

                    case "END":
                        dbService.commitTransaction();
                        running = false;
                        break;

                    default:
                        System.out.println("Command not recognized! " + cmd);
                        break;
                }
            }
            command.close();
        } catch (Exception ex) {
            System.out.println("UNEXPECTED ERROR  - Exception= " + ex);
        }

    }
}
