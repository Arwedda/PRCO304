/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author jkell
 */
public class Currency {
    private String id;
    private String name;
    private String value;

    public Currency() {
        this.id = "unknown";
        this.name = "unknown";
        this.value = "unknown";
    }
    
    public Currency(String id, String name) {
        this.id = id;
        this.name = name;
        this.value = "unknown";
    }

    public Currency(String id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString(){
        return "id="+id+"name="+name+"value="+value;
    }
}
