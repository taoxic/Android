package taoxic.classroom;

/**
 * Created by XJTUSE-PC on 2015/11/2.
 */
public class IpMessageProtocol {

    private String name;
    private String num;
    private String sex;
    private String ipAddress;
    private String macAddress;

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }

    public String getSex() {
        return sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    //得到协议串
    public String getProtocolString(){
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append(":");;
        sb.append(num);
        sb.append(":");
        sb.append(sex);
        sb.append(":");
        sb.append(ipAddress);
        sb.append(":");
        sb.append(macAddress);
        return sb.toString();
    }
}
