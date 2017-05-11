package xyz.monkeycoding.servers.pojo;

/**
 * @Use
 * @Project springdemo
 * @Author Created by CZN on 2017/5/11.
 */
public class Quote {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Quote{" + "data='" + data + '\'' + '}';
    }
}
