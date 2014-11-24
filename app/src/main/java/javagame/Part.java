/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

/**
 *
 * @author EvanKirkland
 */
public class Part {
    String name;
    int status;

    /* Constructor */
    public Part(String n, int s) {
        name = n;
        status = s;
    };

    /* Setter Functions */
    public void setStatus(int s) {
        status = s;
    }
    public void setName(String n) {
        name = n;
    }

    /* Getter Functions */
    public int getStatus() {
        return status;
    }
    public String getName() {
        return name;
    }

    /* Increment Functions */
    /* For bool value, T is ADD, F is SUB */
    public void incrementStatus(int val, boolean action) {
        if(action) {
            status += val;
        } else {
            status -= val;
        }
        System.out.println("Part [" + name + "] has been repaired to [" + status + "] status.");
        if(status < 25) {
            System.out.println("WARNING: part [" + name + "] at critical level [" + status + "].");
        }
    }
}

