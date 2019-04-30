package beans;

import dao.CalculationDAO;
import org.json.JSONArray;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.InitialContext;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@SessionScoped
public class CalcBean implements Serializable {
    CalculationDAO calculationDAO;
    private int selectedId = 0;
    private String currentDate;
    private String data;

    private Part file;

    @PostConstruct
    public void init() {
        try {

            InitialContext cxt = new InitialContext();
            if (cxt == null) {
                throw new Exception("Error -- no context!");
            }

            DataSource ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/postgres");
            if (ds == null) {
                throw new Exception("Data source not found!");
            }
            calculationDAO = new CalculationDAO(ds.getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Calculation> getCalculations() {
        return calculationDAO.findAll();
    }

    public void readFile() {
        try {
            InputStream inputStream = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String data = reader.lines().collect(Collectors.joining());
            String creationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            this.currentDate = creationDate;
            this.data = data;
        } catch (Exception ignored) {}
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String show(int id) {
        selectedId = id;
        return "showcalculation.xhtml";
    }

    public int getSelectedId() {
        return selectedId;
    }

    public String showNew() {
        selectedId = 0;
        return "index.xhtml";
    }

    public boolean isCalculationPresent() {

        return currentDate!=null;
    }

    public List<TableRow> getNewCalculations() {
        return new JSONArray(data).toList().stream()
                .map(String::valueOf)
                .map(TableRow::new)
                .collect(Collectors.toList());
    }

    public String saveToDb() {
        calculationDAO.save(currentDate, data);
        currentDate = null;
        selectedId = 0;
        return "index.xhtml";
    }

    public List<TableRow> selected(int id) {
        Calculation calculation = calculationDAO.find(id);
        return new JSONArray(calculation.getData()).toList().stream()
                .map(String::valueOf)
                .map(TableRow::new)
                .collect(Collectors.toList());
    }

    public String delete() {
        calculationDAO.delete(selectedId);
        return "index.xhtml";
    }

    public String getSelectedDate() {
        Calculation calculation = calculationDAO.find(selectedId);
        return calculation.getCreationDate();
    }
}
