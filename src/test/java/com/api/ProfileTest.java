package com.api;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Before;
import  org.junit.Assert;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ProfileTest {

   Executor notif;
    FileInputStream input;
    ByteArrayOutputStream baos;

    @Before
    public void initialize() throws Exception {

         try{
            notif= new Executor();
            input=new FileInputStream(new File("src/test/resources/profile.json"));
            baos = new ByteArrayOutputStream();

        } catch(Exception ex){
                          ex.printStackTrace(); 

              Assert.fail("Error: No se pudo inicializar");
        }


    }

    @Test
    public void method() throws Exception {

       notif.execute(input, baos);    

    }

}