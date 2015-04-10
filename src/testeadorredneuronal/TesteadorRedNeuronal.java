package testeadorredneuronal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

/**
 * 
 */
public class TesteadorRedNeuronal {

    /**
     * Para unos pesos indicados prueba la red con un archivo
     * Devuelve el valor esperado y el valor calculado.
     * 
     * Entrada de archivo con pesos
     * Entrada directorio con archivos de prueba
     * Entrada con tipo de salida
     * 
     * @param args the command line arguments
     * 
     */
    public static void main(String[] args) {
        //captura argumentos        
        String archivoPesos=args[0];
        String dirEntrada=args[1];
        String tipo=args[2]; //indica si es sentarse, salto, golpe o caida. 1 2 3 4
        int elTipo=Integer.parseInt(tipo);
        
        //tratar archivo de pesos.         
        File archivo=new File(archivoPesos);
        String linea;
        LinkedList listaDatos1=new LinkedList();
        LinkedList listaDatos2=new LinkedList();
        String marcador="dato0";
        double[] valoresD;
        try{
            BufferedReader br=new BufferedReader( new FileReader (archivo));
            while((linea=br.readLine())!=null){
                System.out.println(linea+" "+linea.length());
                if(linea.length()>0 && !linea.startsWith("#")){ 
                    if(linea.contains("DATA1")){
                        marcador="data1";
                    }else if(linea.contains("DATA2")){
                        marcador="data2";
                    }else{                        
                        String[] valores = linea.split(",");
                        if(valores.length>1){
                            //  System.out.println("Tamaño vlaores "+valores.length);
                            valoresD=new double[valores.length];
                            for(int i=0;i<valores.length;i++){
                                //System.out.println(""+i);
                                valoresD[i]=Double.parseDouble(valores[i]);
                            }
                            if(marcador.equals("data1")){
                                listaDatos1.add(valoresD);
                            }else if(marcador.equals("data2")){
                                listaDatos2.add(valoresD);
                            }                        
                        }
                    }  
                }             
            }
        }catch(Exception e){
            e.printStackTrace();
        }
       
        //generar las matrices de sinapsis para enviar a red.       
        double[][] sinapsisA; //relaciona la entrada con la capa oculta
        int longi=((double[])listaDatos1.get(0)).length; //System.out.println("longi "+longi);
        sinapsisA=new double[listaDatos1.size()][longi];
        for(int j=0;j<listaDatos1.size();j++){
            sinapsisA[j]=(double[])listaDatos1.get(j);
        }
                
        double[][] sinapsisB; //relaciona la capa oculta con la capa de salida
        int longib=((double[])listaDatos2.get(0)).length;
        sinapsisB=new double[listaDatos2.size()][longib];
        for(int j=0;j<listaDatos2.size();j++){
            sinapsisB[j]=(double[])listaDatos2.get(j);
        }
        
        Red red=new Red();
        System.out.println("iniciar red: "+sinapsisA[0].length+" "+sinapsisA.length+" "+sinapsisB.length);
        red.iniciarRed(sinapsisA[0].length,sinapsisA.length,sinapsisB.length);
        red.setSinapsisA(sinapsisA);
        red.setSinapsisB(sinapsisB);
        
        
        red.imprimirSinapsis();
        
        double[] biasA=new double[sinapsisA.length];
        for(int j=0;j<sinapsisA.length;j++){ 
            biasA[j]=1;
        }
        red.setBiasA(biasA);
        
        double[] biasB=new double[sinapsisB.length];
        for(int j=0;j<sinapsisB.length;j++){
            biasB[j]=1;
        }
        red.setBiasB(biasB);
         
        red.imprimirVector(biasA);
        
                
                
                
        //cargar archivos de prueba
        //para cada uno
        //calcular y comparar con resultado 
        Boolean capturar = false;
        double gravedad=9.8066;
        LinkedList<Muestra> listaCompleta=new LinkedList<Muestra>();
        
        int contadorPruebasTotales=0;
        int contadorPositivos=0;
        int contadorNegativos=0; //el archivo no coincide
        int contadorNulos=0; //el archivo no pasa el primer filtro
        
        File f=new File(dirEntrada);
        if (f.exists()){  
            File[] ficheros = f.listFiles(); //para cada archivo con datos
            contadorPruebasTotales=ficheros.length;
            for (int x=0;x<ficheros.length;x++){
                System.out.println();
                System.out.println(ficheros[x].getName());
                System.out.println();
                
                listaCompleta.clear();
                
                try {
                    String nombrearchivo=dirEntrada+"/"+ficheros[x].getName();
                    BufferedReader br=new BufferedReader( new FileReader (nombrearchivo));
                              
                    // Lectura del fichero
                    String resultado="";
                    while((linea=br.readLine())!=null){
                    //  System.out.println(linea);
                
                        if(capturar){
                            String[] valores = linea.split(",");
                            double[] dValor=new double[valores.length];
                            double acele=0;
                            for(int i=0;i<valores.length;i++){
                                dValor[i]=Double.parseDouble(valores[i]);  
                                if(i>0){
                                    dValor[i]=dValor[i]/gravedad;
                            // System.out.println(valoresD[i]);
                                    acele=acele+Math.pow(dValor[i],2);
                                }
                            }
                            acele= Math.sqrt(acele);          
                            
                    //  System.out.println(valoresD[0]+" "+acele);
                   // resultado=resultado+valoresD[0]+","+acele+"\r\n";
                            listaCompleta.add(new Muestra((long)dValor[0],acele));     
                        }
                
                        if( linea.compareTo("@DATA")==0){
                            capturar=true;
                        }                
                    }
                    
                    capturar=false;
                     //terminado el proceso de archivo de entrada
                     //Iniciar monitor para comprobar si es caida u otra cosa.
                    Monitor monitor=new Monitor();
                    monitor.tratar(listaCompleta);            
                    double[] resul= monitor.getResultadoCara();   
                    
                    if(resul!=null){
                        //monitor devuelve los 8 valores.
                        //ahora pasar por red para obtener la salida.                    
                        red.setVector_entrada(resul);
                        red.imprimirVector(resul);
                        red.calcular();
                        double[] laSalida = red.getVector_salida();
                        
                        double mayor=0;
                        int marca=-1;
                        for(int k=0;k<laSalida.length;k++){
                            if( laSalida[k]>mayor){
                                mayor=laSalida[k];
                                marca=k;
                            }
                        }
                        marca=marca+1;
                        if(marca==elTipo){
                            contadorPositivos++;
                        }else{
                            contadorNegativos++;
                        }
                    }else{
                        contadorNulos++; //archivo no ha pasado el primer filtro
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }   
            System.out.println("Pruebas totales: "+contadorPruebasTotales);
            System.out.println("Positivos: "+contadorPositivos);
            System.out.println("Negativos: "+contadorNegativos);
            System.out.println("Positivos: "+contadorNulos);
              
        }
        
       
    }
    
}
