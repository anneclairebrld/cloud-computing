package app;

import java.awt.*;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Interaction {
    //private Integer dimY;
    //private Integer row;
    //private Integer col;
    private Integer index;
    private Integer red;
    private Integer green;
    private  Integer blue;

    public Interaction(){}

    public Interaction(String object){
        JSONObject obj = new JSONObject(object);
        //setDimY(obj.getInt("dimY"));
        //setRow(obj.getInt("row"));
        //setCol(obj.getInt("col"));
        setIndex(obj.getInt("index"));
        setRed(obj.getJSONArray("color"));
        setGreen(obj.getJSONArray("color"));
        setBlue(obj.getJSONArray("color"));
    }
    //public void setDimY(Integer dimY){this.dimY = dimY;}
    //public void setRow(Integer row){this.row = row;}
    //public void setCol(Integer col){this.col = col;}

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setRed(JSONArray color){this.red = color.getInt(0);}

    public void setGreen(JSONArray color) {
        this.green = color.getInt(1);;
    }

    public void setBlue(JSONArray color) { this.blue = color.getInt(2);}

    public Integer getIndex() {
        return index;
    }

    /*public Integer getDimY() {
        return dimY;
    }

    public Integer getRow(){
        return row;
    }

    public Integer getCol() {
        return col;
    }*/

    public Integer getRed() {
        //System.out.println("Color: " + color);
        return red;
    }

    public Integer getGreen() {
        return green;
    }

    public Integer getBlue() {
        return blue;
    }
}
