package com.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Base64;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTCreationException;

import java.util.Map;
import java.util.HashMap;

//https://99clb5k89d.execute-api.us-east-1.amazonaws.com/test/profile  GET  Authorization
//https://99clb5k89d.execute-api.us-east-1.amazonaws.com/test/login  POST username, password en el cuerpo del json



public class Executor {



  Map<String, String>  db = new HashMap<String, String>();



  public void execute(InputStream inputStream, OutputStream outputStream) throws Exception {


    db.put("abautista86@hotmail.com", "abautista86");
    db.put("aalvarezs@intercam.com.mx", "aalvarezs");
    db.put("acloeza@intercam.com.mx ", "acloeza");
    db.put("gdejesus@intercam.com.mx", "gdejesus");
    db.put("mvasquez@intercam.com.mx", "mvasquez");
    db.put("lflores@intercam.com.mx", "lflores");
    db.put("dleonr@intercam.com.mx", "dleonr");
    db.put("rgarciaq@intercam.com.mx", "rgarciaq");
    db.put("cesar.cast.more@gmail.com", "cesar.cast.more");


    JSONObject request = (JSONObject) new JSONParser().parse(toString(inputStream));
    JSONObject response= new JSONObject();

    System.out.println(request.toJSONString());

    JSONObject context = (JSONObject) request.get("context");
    String path = (String) context.get("resource-path");


    if(path.equals("/login")){

      JSONObject body = (JSONObject) request.get("body-json");


      String username = (String) body.get("username");
      String password = (String) body.get("password");

      if(db.containsKey(username)){

        String pass = db.get(username);

        if(pass.equals(password)){


          try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            String token = JWT.create()
                .withIssuer("tecgurus")
                .withClaim("email", username)
                .sign(algorithm);

              response.put("success", true);
              response.put("token", token);

          } catch (JWTCreationException exception){
            System.out.println("entroo aqui 4");

            //Invalid Signing configuration / Couldn't convert Claims.
          }

        } else {
          response.put("success", false);
          response.put("error", "El password es incorrecto");
        }


      }else {
        response.put("success", false);
        response.put("error", "El username no existe");

      }



    }else if(path.equals("/profile")){

      JSONObject params = (JSONObject) request.get("params");

      JSONObject headers = (JSONObject) params.get("header"); 
      String authorization = (String) headers.get("Authorization");
      String token = authorization.replace("Bearer ", "");

      String email =getEmail(token);
      String secret = db.get(email);

      if(!isValid(token, secret)){
        response.put("success", false);
        response.put("error", "El token es invalido");
      }else {
        response.put("success", true);
        JSONObject user =  new JSONObject();
        user.put("email", email);
        response.put("profile", user);

      }

    }


    System.out.println(response.toJSONString());




    outputStream.write(response.toJSONString().getBytes(Charset.forName("UTF-8")));



    

  }

  

  private String toString(InputStream in) throws Exception {
    StringBuilder sb = new StringBuilder();
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String read;

    while ((read = br.readLine()) != null) {
      sb.append(read);
    }

    br.close();
    return sb.toString();
  }


  private Boolean isValid(String token, String secret){
    try {

      if(secret== null){
        return false;

      }

      String email = getEmail(token);
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm)
          .withIssuer("tecgurus")
          .withClaim("email", email)
          .build(); //Reusable verifier instance

      DecodedJWT jwt = verifier.verify(token);

    } catch (JWTVerificationException exception){

      return false;
            
    }

    return true;

    }


    private String getEmail(String token){
      try{

      DecodedJWT decodedJwt = JWT.decode(token);
      String payload = decodedJwt.getPayload();
      byte[] decodedBytes = Base64.getDecoder().decode(payload);
      JSONParser parser = new JSONParser();
      JSONObject payloadJson = (JSONObject) parser.parse(new String(decodedBytes));
      String email = (String) payloadJson.get("email");
      return email;

      } catch(Exception e){
        return "";

      }


    }
  


}