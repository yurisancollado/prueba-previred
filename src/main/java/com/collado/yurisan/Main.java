package com.collado.yurisan;

import java.util.List;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

	public static void main(String[] args) throws Throwable {
		//getNivel1();		
		Nivel2 obj = new Nivel2();
		obj.runNivel2();
	}
	
	

	public static void getNivel1() throws org.json.simple.parser.ParseException, ParseException {
		JSONParser json = new JSONParser();
        
        try (FileReader reader = new FileReader("entrada.json"))
        {
        	//-- Lectura del Json de salida
            Object obj = json.parse(reader); 
            JSONObject jsonObject = (JSONObject) obj;            
            long id = (long) jsonObject.get("id");
            String fechaCreacionTemp = (String) jsonObject.get("fechaCreacion");
            String fechaFinTemp = (String) jsonObject.get("fechaFin");
            JSONArray fechasJson = (JSONArray)jsonObject.get("fechas");  
            
            Date fechaCreacion=new SimpleDateFormat("yyyy-MM-dd").parse(fechaCreacionTemp); 
            Date fechaFin=new SimpleDateFormat("yyyy-MM-dd").parse(fechaFinTemp); 
            
            List<String> fechas = new ArrayList<String>();
            for (int i = 0; i < fechasJson.size(); i++) {
            	fechas.add( (String)fechasJson.get(i));
            }
            
            List<String> fechaFaltante=getFechaFaltante(fechaCreacion,fechaFin, fechas);
           
            //-- Escritura del Json de salida
            JSONObject salidaDetalle = new JSONObject();
            salidaDetalle.put("id",id);
            salidaDetalle.put("fechaCreacion", fechaCreacionTemp);
            salidaDetalle.put("fechaFin", fechaFinTemp);
            salidaDetalle.put("fechasFaltantes", fechaFaltante);
            
            JSONArray salidaLista = new JSONArray();
            salidaLista.add(salidaDetalle);
            
            try (FileWriter file = new FileWriter("salidaNivel1.json")) {            	 
                file.write(salidaLista.toJSONString());
                file.flush();     
            } catch (IOException e) {
                e.printStackTrace();
            }    
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	public static List<String> getFechaFaltante(Date entrada, Date salida, List<String> fechas) {		
		LocalDate entradaLocal = entrada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int anoEntrada= entradaLocal.getYear();
		int mesEntrada = entradaLocal.getMonthValue();
		
		LocalDate salidaLocal =  salida.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int anoSalida = salidaLocal.getYear();
		int mesSalida = salidaLocal.getMonthValue();
		
		int difMes= (anoSalida - anoEntrada) * 12 + (mesSalida - mesEntrada) + 1;
		
	
		List<String> fechaFaltante= new ArrayList<String>();	
		
		LocalDate fechaTemp=entradaLocal;
		
		fechaFaltante=existeFecha(fechaFaltante,fechas,fechaTemp);
		
		for(int i=1;i<difMes;i++) {
			fechaTemp=fechaTemp.plusMonths( 1 );
			fechaFaltante=existeFecha(fechaFaltante,fechas,fechaTemp);		
		}	
		return fechaFaltante;
	}
	
	public static List<String>  existeFecha(List<String> fechaFaltante,List<String> fechas, LocalDate fechaTemp){
		if(fechas.indexOf(fechaTemp.toString())==-1) {
			fechaFaltante.add(fechaTemp.toString());				
		}	
		return fechaFaltante;
	}
}
