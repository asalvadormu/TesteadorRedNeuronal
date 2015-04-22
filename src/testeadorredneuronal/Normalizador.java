/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testeadorredneuronal;

/**
 *
 * @author SAMUAN
 */
public class Normalizador {
    
    private static double[] media={0.2626928896628888, 269.5530546623794, 2.289896310288866, 0.3739119275341829, 65.41407717041797, 0.6586289112276231, 1.0353608131525718, 1.369774919614148};
    private static double[] desviacion={0.1972426192739203, 259.49314232770456, 0.1663845913252326, 0.17075000616781955, 29.46390971747825, 0.2705688426168408, 0.20544079399393517, 1.0370560054028108};
    

    public static double[] normaliza(double[] resul) {
        double[] valor=new double[resul.length];
        for(int carac=0;carac<valor.length;carac++){
            valor[carac] = (resul[carac]-media[carac])/desviacion[carac];
        }
        return valor;      
    }

    public static double[] getMedia() {
        return media;
    }

    public static void setMedia(double[] media) {
        Normalizador.media = media;
    }

    public static double[] getDesviacion() {
        return desviacion;
    }

    public static void setDesviacion(double[] desviacion) {
        Normalizador.desviacion = desviacion;
    }  
   
}
