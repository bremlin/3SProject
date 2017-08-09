package com.ibcon.sproject.smet.model.objects;

public class SmetResource {

    private int id;
    private int titulId;
    private String name;
    private String code;
    private String type;
    private String units;

    public SmetResource(String name, String code, String type, String units) {
        this.name = (name != null) ? name.replace("'", "") : "";
        this.code = (code != null) ? code.replace("'", "") : "";
        this.type = (type != null) ? type : "";
        this.units = units;
        //todo create
    }


    public int getId() {
        return id;
    }

    public int getTitulId() {
        return titulId;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getUnits() {
        return units;
    }
}
