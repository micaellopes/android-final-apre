package com.example.andrey.academiaand;

import com.example.andrey.academiaand.model.Academia;

/**
 * Created by Andrey on 12/06/2017.
 */

public class AcademiaController {

    private static AcademiaController instace;
    private Academia academia;
    private AcademiaController(){}

    public static AcademiaController getInstace(){
        if( instace == null){
            instace = new AcademiaController();
        }
        return instace;
    }

    public Academia getAcademia() {
        return academia;
    }

    public void setAcademia(Academia academia) {
        this.academia = academia;
    }
}
