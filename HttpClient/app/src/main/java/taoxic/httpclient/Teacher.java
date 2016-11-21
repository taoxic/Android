package taoxic.httpclient;

import java.io.Serializable;

/**
 * Created by XJTUSE-PC on 2015/11/23.
 */
public class Teacher implements Serializable{
    private Integer id;
    private String name;
    private String tnum;
    private boolean isCache;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTnum() {
        return tnum;
    }
    public boolean isCache() {

        return isCache;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTnum(String tnum) {
        this.tnum = tnum;
    }

    public void setIsCache(boolean isCache) {
        this.isCache = isCache;
    }
}
