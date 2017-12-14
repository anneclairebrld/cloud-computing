package app;

import java.awt.*;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Interaction {
    private Integer row;
    private Integer col;
    private Color color;

    public Interaction(){}

    public Interaction(String object){
        JSONObject obj = new JSONObject(object);
        setRow(obj.getInt("row"));
        setCol(obj.getInt("col"));
        setColor(obj.getJSONArray("color"));
    }

    public void setRow(Integer row){this.row = row;}
    public void setCol(Integer col){this.col = col;}
    public void setColor(JSONArray color){this.color = new Color(color.getInt(0), color.getInt(1), color.getInt(2));}

    public Integer getRow(){
        return row;
    }

    public Integer getCol() {
        return col;
    }

    public Color getColor() {
        System.out.println("Color: " + color);
        return color;
    }
}
