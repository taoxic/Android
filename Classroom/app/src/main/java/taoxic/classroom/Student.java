package taoxic.classroom;

/**
 * Created by XJTUSE-PC on 2015/11/1.
 */
public class Student {
    private String name;
    private String  number;
    private  String mac;

    public Student(){

    }
    public Student(String name,String number){
        this.name = name;
        this.number =number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
