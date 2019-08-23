package com.collado.yurisan;

import java.io.BufferedReader;
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
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Nivel2 {
public void runNivel2() throws org.json.simple.parser.ParseException, ParseException {
		
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
		    System.out.println("Lectura de Properties: config.properties");
			Properties prop = new Properties();
		    prop.load(input);
		    System.out.println("Inicio de llamada a api: "+prop.getProperty("url.api"));
		    URL url = new URL(prop.getProperty("url.api"));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Falla : HTTP codigo de error : "
						+ conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
			    System.out.println("Fin llamada a api: "+prop.getProperty("url.api"));
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject) parser.parse(output);
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
		            
		            String fechasString= String.join(", ",fechas);		            
		            String fechaFaltanteString= String.join(", ",fechaFaltante);	
		            
		            //-- Escritura del Json de salida
		            PrintWriter writer = new PrintWriter(prop.getProperty("archivo.salida"), "UTF-8");
		            writer.println("fecha creación: "+fechaCreacionTemp);
		            writer.println("fecha fin: "+fechaFinTemp);
		            writer.println("fechas recibidas: "+fechasString);
		            writer.println("fechas faltantes: "+fechaFaltanteString);
		            writer.close();
		    	    System.out.println("Creado archivo de salida en la ruta: \\miPrueba\\target\\"+prop.getProperty("archivo.salida"));

			}
			conn.disconnect();

		  } catch (MalformedURLException e) {
			e.printStackTrace();
		  }  
			catch (IOException e) {
			  
			e.printStackTrace();
		  }
	}
	
	public  List<String> getFechaFaltante(Date entrada, Date salida, List<String> fechas) {		
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
	    System.out.println("Fin de Calculo fecha");

		return fechaFaltante;
	}
	
	public List<String>  existeFecha(List<String> fechaFaltante,List<String> fechas, LocalDate fechaTemp){
		if(fechas.indexOf(fechaTemp.toString())==-1) {
			fechaFaltante.add(fechaTemp.toString());				
		}	
		return fechaFaltante;
	}

}
