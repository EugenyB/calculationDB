package beans;

import org.json.JSONArray;

import java.util.List;
import java.util.stream.Collectors;

public class Calculation {
    private int id;
    private String data;
    private String creationDate;

    public Calculation(int id, String data, String creationDate) {
        this.id = id;
        this.data = data;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public List<String> getCalculations() {
        return new JSONArray(data).toList().stream().map(String::valueOf).collect(Collectors.toList());
    }

}
