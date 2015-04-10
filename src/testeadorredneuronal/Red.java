package testeadorredneuronal;

/**
 * Clase para construir la red neuronal completa.
 * Se trata de un perceptrón multicapa.
 *
 * @author SAMUAN
 */
public class Red {
    private double[][] vector_datos;
    
    private double[] vector_entrada;
    private double[] vector_parcial;
    private double[] vector_salida;

    private double[][] sinapsisA; //relaciona la entrada con la capa oculta
    private double[][] sinapsisB; //relaciona la capa oculta con la capa de salida

    private double[] biasA; //capa oculta
    private double[] biasB; //capa de salida

    private double[] target;
    private double[] errorA; //errores en la capa oculta
    private double[] errorB; //errores en la capa de salida

    private double ratioAprendizaje;
    private int numEntradas;
    private int numCapaOculta;
    private int numCapaSalida;
    private int ciclos;
    

    /**
     * Inicia los pesos con valores en el intervalo (-0.5,0.5)
     * 
     * Cada sinapsis independiente porque cada capa tiene 
     * distintas neuronas.
     *
     */
    public void iniciarSinapsis(){   
        //System.out.println("sinapsis A");
        for(int i=0;i<sinapsisA.length;i++){
            for(int j=0;j<sinapsisA[0].length;j++){        
               sinapsisA[i][j]=Math.random()-0.5;
               //System.out.print( sinapsisA[i][j]+" ");               
            }
            //System.out.println();
        }           
        //  System.out.println();
        //   System.out.println("sinapsis B");
        for(int i=0;i<sinapsisB.length;i++){
            for(int j=0;j<sinapsisB[0].length;j++) {
                sinapsisB[i][j]=Math.random()-0.5;
                // System.out.print( sinapsisB[i][j]+" ");
            }
            //System.out.println();
        }        
        // System.out.println();
    }

    /**
     * Iniciar los datos básicos de la red.
     * Se utiliza al preparar la red para aprendizaje.
     *
     * @param ratioAprendizaje
     * @param numEntradas Cantidad de datos de entrada
     * @param numCapaOculta Cantidad de neuronas en capa oculta
     * @param numCapaSalida Cantidad de neuronas en capa de salida
     */
    public void iniciarRed(double ratioAprendizaje,int numEntradas, int numCapaOculta, int numCapaSalida ){
        this.ratioAprendizaje=ratioAprendizaje;
        this.numEntradas=numEntradas;
        this.numCapaOculta=numCapaOculta;
        this.numCapaSalida=numCapaSalida;

        vector_entrada=new double[this.numEntradas];
        vector_parcial=new double[this.numCapaOculta];
        vector_salida=new double[this.numCapaSalida];

        sinapsisA=new double[numCapaOculta][numEntradas]; //1 neurona por fila
        sinapsisB=new double[numCapaSalida][numCapaOculta]; //cada columna son pesos que llegan a la neurona.

        biasA=new double[numCapaOculta];
        biasB=new double[numCapaSalida];

        target=new double[numCapaSalida];
        errorA=new double[numCapaOculta];
        errorB=new double[numCapaSalida];
    }
    
    /**
     * Inicia la red para calcular salidas.
     * 
     * @param numEntradas Cantidad de datos de entrada
     * @param numCapaOculta Cantidad de neuronas en capa oculta
     * @param numCapaSalida Cantidad de neuronas en capa de salida
     */
    public void iniciarRed(int numEntradas, int numCapaOculta, int numCapaSalida){
        this.numEntradas=numEntradas;
        this.numCapaOculta=numCapaOculta;
        this.numCapaSalida=numCapaSalida;
        
        vector_entrada=new double[this.numEntradas];
        vector_parcial=new double[this.numCapaOculta];
        vector_salida=new double[this.numCapaSalida];

        sinapsisA=new double[numCapaOculta][numEntradas]; //1 neurona por fila
        sinapsisB=new double[numCapaSalida][numCapaOculta]; //cada columna son pesos que llegan a la neurona.

        biasA=new double[numCapaOculta];
        biasB=new double[numCapaSalida];
    }

    /**
     * Establece el vector con todos los datos para entrenamiento.
     * 
     */
    public void setVector_datos(double[][] vector_datos) {
        this.vector_datos = vector_datos;
    }
    
    /**
     *
     * algoritmo de entrenamiento
     *
     * ejecución hacia adelante
     * calculo errores en ultima capa
     * modificación pesos de sinapsis B
     * calculo error capa oculta
     * modificación pesos sinapsis A
     */
    public void entrenamiento(int ciclos){
        this.ciclos=ciclos;
        //iniciar sinapsis
        iniciarSinapsis();
        //capturar datos de prueba + numero de iteraciones

        //cada entrada será 8 valores de entrada + 4 de salida.
        //setVector_entrada
        //setTarget

        //repetir varias veces , una por cada valor de entrada , target.
        //hasta que el error sea cercano a cero o se acaben las entradas.
        for(int i=0;i<ciclos;i++) {
            //elegir una entrada al azar
            int marcaEntrada=(int) (vector_datos.length*Math.random());
            System.out.println("marca: "+marcaEntrada);
            for(int j=0;j<vector_datos[marcaEntrada].length;j++){
                if(j<vector_entrada.length){
                    vector_entrada[j]=vector_datos[marcaEntrada][j];
                }else{
                    target[j-vector_entrada.length]=vector_datos[marcaEntrada][j];
                }
            }
            
            imprimirVector(vector_entrada);
            imprimirVector(target);
                   
            
            calcular();
            calcularErroresNeuronaSalida();
            modificarPesosSinapsisB();
            calcularErrorCapaOculta();
            modificarPesosSinapsisA();
        }
        System.out.println("FIN ENTRENAMIENTO");
    }

     /**
     * Calcula los valores de salida de la red completa.
     *
     */
    public void calcular(){
        //vector entrada por sinapsisA
        double suma=0;
        for(int i=0;i<sinapsisA.length;i++){ //i es cada neurona de esta capa
            suma=0;
            for(int j=0;j<sinapsisA[0].length;j++) { //j es numero de entradas que llegan a la neurona
                System.out.println("indices_: "+i+" "+j);
                suma +=vector_entrada[j]*sinapsisA[i][j];
                System.out.println("CALCULAR calculo: "+vector_entrada[j]+" "+sinapsisA[i][j]+" "+suma );
            }
            suma=suma+biasA[i];
            System.out.println("suma "+suma);
            System.out.println("parcial "+vector_parcial.length);
            vector_parcial[i]=logsig(suma);
           
        }

        //vector parcial por sinapsisB
        for(int i=0;i<sinapsisB.length;i++){
            suma=0;
            for(int j=0;j<sinapsisB[i].length;j++) {
                suma +=vector_parcial[j]*sinapsisB[i][j];
            }
            suma=suma+biasB[i];
            vector_salida[i]=logsig(suma);
           
        }
        
        imprimirVector(vector_parcial);
        imprimirVector(vector_salida);
        
    }

    /**
     * Función de activación
     * Se utiliza una función sigmoidal.
     *
     * @param val valor de x
     * @return resultado
     */
    private double logsig(double val){
        return 1/(1+Math.exp(-val));
    }

    /**
     * Calcula los errores en la última capa.
     */
    public void calcularErroresNeuronaSalida(){
        for(int i=0;i<vector_salida.length;i++){
            errorB[i]=vector_salida[i]*(1-vector_salida[i])*(target[i]-vector_salida[i]);
            System.out.println("SALIDA error "+i+" "+errorB[i]);
        }
    }

    /**
     * Modifica los pesos de la sinapsis entre la capa oculta y la capa de salida.
     * WAα = WAα + η δα outA   WAβ = WAβ + η δβ outA
     * WBα = WBα + η δα outB   WBβ = WBβ + η δβ outB
     * WCα = WCα + η δα outC   WCβ = WCβ + η δβ outC
     */
    public void modificarPesosSinapsisB(){
        for(int i=0;i<sinapsisB.length;i++){
            for(int j=0;j<sinapsisB[0].length;j++){
                sinapsisB[i][j]=sinapsisB[i][j]+ratioAprendizaje*errorB[i]*vector_parcial[j];
               System.out.println("MODIFICAR  "+i+" "+j+" "+sinapsisB[i][j]);
            }

        }
    }

    /**
     * Calcula los errores de la capa oculta utilizando los errores de la capa de salida
     *
     * δA = outA (1 – outA) (δα WAα + δβ WAβ)
     δB = outB (1 – outB) (δα WBα + δβWBβ)
     δC = outC (1 – outC) (δα WCα + δβWCβ)
     */
    public void calcularErrorCapaOculta(){
        double suma=0;
        for(int i=0;i<sinapsisB[0].length;i++){ //para cada neurona  de capa oculta        
            suma=0;
            for(int j=0;j<sinapsisB.length;j++) { //calculo la aportación de error a cada neurona de destino. Por eso lo recorro en columna.
                suma=suma+errorB[j]*sinapsisB[j][i];
            }
            errorA[i]=vector_parcial[i]*(1-vector_parcial[i])*suma;
        }
        imprimirVector(errorA);
    }

    /**
     * Modifica los pesos de la sinapsis entre los datos de entrada y la capa oculta.
     *
     * WλA = WλA + ηδA inλ     WΩA = WΩA + ηδA inΩ
     * WλB = WλB + ηδB inλ     WΩB = WΩB + ηδB inΩ
     * ...
     */
    public void modificarPesosSinapsisA(){
        for(int i=0;i<sinapsisA.length;i++){ //para cada neurona
            for(int j=0;j<sinapsisA[0].length;j++){ //para cada peso que llega a la neurona
                sinapsisA[i][j]=sinapsisA[i][j]+ratioAprendizaje*errorA[i]*vector_entrada[j];
            }
        }
    }


    /************* MÉTODOS GET SET ****************************/

    public double[] getVector_entrada() {
        return vector_entrada;
    }

    public void setVector_entrada(double[] vector_entrada) {
        this.vector_entrada = vector_entrada;
    }

    public double[] getVector_salida() {
        return vector_salida;
    }

    public double[] getVector_parcial() {
        return vector_parcial;
    }

    public void setVector_parcial(double[] vector_parcial) {
        this.vector_parcial = vector_parcial;
    }

    public void setVector_salida(double[] vector_salida) {
        this.vector_salida = vector_salida;
    }

    public double[] getBiasA() {
        return biasA;
    }

    public void setBiasA(double[] biasA) {
        this.biasA = biasA;
    }

    public double[] getBiasB() {
        return biasB;
    }

    public void setBiasB(double[] biasB) {
        this.biasB = biasB;
    }

    public double[][] getSinapsisB() {
        return sinapsisB;
    }

    public void setSinapsisB(double[][] sinapsisB) {
        this.sinapsisB = sinapsisB;
    }

    public double[][] getSinapsisA() {
        return sinapsisA;
    }

    public void setSinapsisA(double[][] sinapsisA) {
        this.sinapsisA = sinapsisA;
    }

    public double[] getTarget() {
        return target;
    }

    public void setTarget(double[] target) {
        this.target = target;
    }

    public void imprimirSinapsis(){
        for(int i=0;i<sinapsisA.length;i++) {
            String linea="";
            for (int j = 0; j < sinapsisA[0].length; j++) {
                linea += " "+sinapsisA[i][j];
            }
            System.out.println("SINAPSIS "+linea);
        }
    }
    
    public void imprimirVector(double[] vec){
        for(double dat:vec) System.out.print(dat+" ");
        System.out.println();
    }

}


    