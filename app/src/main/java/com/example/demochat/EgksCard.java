package com.example.demochat;

public class EgksCard {

    private String name,number,balance,lastUpdate;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public EgksCard(String n, String num){
        this.name = n;
        this.number = num;
        this.balance = "0\u20BD";
    }

    public EgksCard(String n, String num, int id){
        this.name = n;
        this.number = num;
        this.id = id;
        this.balance = "0\u20BD";
    }

    public EgksCard(String n, String num,String b, String lastUpdate, Integer id){
        this.name = n;
        this.number = num;
        if(b != null)
            this.balance = b;
        else this.balance = "0\u20BD";

        this.lastUpdate = lastUpdate;
        this.id = id;
    }



}
