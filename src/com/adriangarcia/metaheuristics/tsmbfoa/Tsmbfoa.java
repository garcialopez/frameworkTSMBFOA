
package com.adriangarcia.metaheuristics.tsmbfoa;

import java.util.Arrays;

/**
 *
 * @author Adrian
 */
public class Tsmbfoa implements Foraging{
    private final Mechanism mechanism;
    private final NumberRandom numberRandom;    
    private Configurator newBacter;
    private int generation, count;
    public Tsmbfoa() {
        this.numberRandom = new NumberRandom();
        this.mechanism = new Mechanism();        
        this.generation = 0;
        this.count = 0;
    }
    
    @Override
    public void chemotaxis(Configurator bacterium, Problem problem, Function function) {                
         this.newBacter = new Configurator();
         int numberVariables = problem.getNumberAssignedVariable();
         int middle = (int) (bacterium.getNc() / 2);                //(mitad)
         double[][] newBacterium = new double[1][numberVariables * 2 + 2];
         double[] angles = new double[numberVariables];
         double[] stem = new double[numberVariables * 2 + 2];       //vastago
         boolean flag;                        //(bandera)1:true, 0:false
         int v1, v2, v3;
         double[] bact1;
         double[] bact2;
         double[] bact3;
         
          for (int b = 0; b < bacterium.getBacteriumMatix().length; b++) { //for b
             flag = true; 
            for (int c = 0; c < bacterium.getNc(); c++) {   //start chemotaxis - for c                
                if (flag) 
                    angles = this.mechanism.generateAngles(numberVariables);
                v1 = this.numberRandom.getRandomRankUnif(0, (bacterium.getBacteriumMatix().length - 1));
                v2 = this.numberRandom.getRandomRankUnif(0, (bacterium.getBacteriumMatix().length - 1));
                v3 = this.numberRandom.getRandomRankUnif(0, (bacterium.getBacteriumMatix().length - 1));
                while(v1 == v2)
                     v2 = this.numberRandom.getRandomRankUnif(0, bacterium.getSb()-1);
                while(v3 == v2 || v3 == v1)
                     v3 = this.numberRandom.getRandomRankUnif(0, bacterium.getSb()-1);
                
                //preguntar si en bact1 es b o v1
                bact1 = Arrays.copyOfRange(bacterium.getBacteriumMatix()[v1], 0, bacterium.getBacteriumMatix()[v1].length);
                bact2 = Arrays.copyOfRange(bacterium.getBacteriumMatix()[v2], 0, bacterium.getBacteriumMatix()[v2].length);
                bact3 = Arrays.copyOfRange(bacterium.getBacteriumMatix()[v3], 0, bacterium.getBacteriumMatix()[v3].length);
                                //bacterium.getFactorE() es B
                for(int m = 0; m < numberVariables; m++)
                    stem[m] = bact1[m] + (bacterium.getScalingFactor() - 1) * (bact2[m] - bact3[m]);
                
                for (int k=0; k<numberVariables; k++){ //for k                    
                    if (c > middle || c < middle){
                        // preguntamos si es par o impar
                        if (c%2 == 0){
                            newBacterium[0][k] = stem[k];                           
                            newBacterium[0][k + numberVariables] = bacterium.getBacteriumMatix()[b][k + numberVariables];
                        }else{                            
                            newBacterium[0][k] = bacterium.getBacteriumMatix()[b][k] + bacterium.getBacteriumMatix()[b][k + numberVariables] * angles[k];                                                        
                            newBacterium[0][k + numberVariables] = bacterium.getBacteriumMatix()[b][k + numberVariables];
                        }                                                      
                    }else{ // si mitad == c entonces ocurre el AGRUPAMIENTO
                        newBacterium[0][k] = bacterium.getBacteriumMatix()[b][k] + bacterium.getScalingFactor() * (bacterium.getBacteriumMatix()[0][k] - bacterium.getBacteriumMatix()[b][k]);
                        newBacterium[0][k + numberVariables] = bacterium.getBacteriumMatix()[0][k + numberVariables];
                    }
                    
                     // Se valida que la nueva bacteria este dentro de los rangos de las variables
                    
                    if (newBacterium[0][k] < problem.getRankVariable()[k][0]){
                        //System.out.println("revaso menor");
                        newBacterium[0][k] = (problem.getRankVariable()[k][0] * 2.0 - newBacterium[0][k]);
                    }
                    if (newBacterium[0][k] > problem.getRankVariable()[k][1]){
                        //System.out.println("revaso mayor");
                        newBacterium[0][k] = (problem.getRankVariable()[k][1] * 2.0 - newBacterium[0][k]);                            
                    }    
                    if (newBacterium[0][k] < problem.getRankVariable()[k][0] || newBacterium[0][k] > problem.getRankVariable()[k][1]){ 
                        newBacterium[0][k] =  this.numberRandom.getRandomRankUnif(problem.getRankVariable()[k][0], problem.getRankVariable()[k][1]);
                        //System.out.println("revaso ambos");
                    }
                                        
                }// for k
              
                newBacter.setBacteriumMatix(newBacterium);
                newBacterium = this.mechanism.evaluationFyC(newBacter, problem, function);
                
               // Despues de la evaluaci??n en la funci??n obhetivo de la nueva bacteria, 
               // se compara con la bacteria en proceso usando reglas de factibilidad
                
                if (newBacterium[0][numberVariables * 2 + 1] == 0 && bacterium.getBacteriumMatix()[b][numberVariables * 2 + 1] == 0){
                        if (newBacterium[0][numberVariables * 2] < bacterium.getBacteriumMatix()[b][numberVariables * 2]){ 
                            flag = false;    
                            bacterium.getBacteriumMatix()[b] = Arrays.copyOfRange(newBacterium[0], 0, newBacterium[0].length);
                        }else{
                            flag = true;
                        }
                }else if (newBacterium[0][numberVariables * 2 + 1] > 0 && bacterium.getBacteriumMatix()[b][numberVariables * 2 + 1] > 0){
                        if (newBacterium[0][numberVariables * 2 + 1] < bacterium.getBacteriumMatix()[b][numberVariables * 2 + 1]){
                            flag = false;                            
                            bacterium.getBacteriumMatix()[b] = Arrays.copyOfRange(newBacterium[0], 0, newBacterium[0].length);
                        }else{
                            flag = true;
                        }
                }else if (newBacterium[0][numberVariables * 2 + 1] == 0 &&  bacterium.getBacteriumMatix()[b][numberVariables * 2 + 1] > 0){
                        flag = false;
                        bacterium.getBacteriumMatix()[b] = Arrays.copyOfRange(newBacterium[0], 0, newBacterium[0].length);
                }else{
                        flag = true;                    
                }
                
            } //for c            
            bacterium.sortPopulation(problem.getNumberAssignedVariable());            
        }//for b
          
    }

    @Override
    public void grouping() {
        
    }

    @Override
    public void reproduction(Configurator bacterium) {           
        if (this.generation%bacterium.getRepcycle() == 0) {
            int row = bacterium.getBacteriumMatix().length;
            int column = bacterium.getBacteriumMatix()[0].length;
            for(int y = (row - bacterium.getBacteriaReproduce()); y < row; y++){
                System.arraycopy(bacterium.getBacteriumMatix()[((row-1) - y)], 0, bacterium.getBacteriumMatix()[y], 0, column);            
            }
            bacterium.sortPopulation((bacterium.getBacteriumMatix()[0].length-2)/2);
        }
    }

    @Override
    public void eliminationDispersal(Configurator bacterium, Problem problem, Function function) {
        this.newBacter = new Configurator();        
        double[][] bacter = new double[1][problem.getNumberAssignedVariable() * 2 + 2];        
        double[] angles = mechanism.generateAngles(problem.getNumberAssignedVariable());
        
        for (int j = 0; j < problem.getNumberAssignedVariable(); j++) {
            bacter[0][j] = numberRandom.getRandomRankUnif(problem.getRankVariable()[j][0], problem.getRankVariable()[j][1]);
            bacter[0][problem.getNumberAssignedVariable() + j] = angles[j];
        }
        this.newBacter.setBacteriumMatix(bacter);
        bacter = mechanism.evaluationFyC(this.newBacter, problem, function);
        
        System.arraycopy(bacter[0], 0, 
                         bacterium.getBacteriumMatix()[bacterium.getSb()-1], 0, 
                         problem.getNumberAssignedVariable());

    }

    @Override
    public void updateStepSize(Configurator bacterium, Problem problem) {
         for (int i = 0; i < bacterium.getSb(); i++) {
                for (int j = 0; j < problem.getNumberAssignedVariable(); j++) {
                    bacterium.getBacteriumMatix()[i][problem.getNumberAssignedVariable() + j] = this.numberRandom.getRandomRankUnif(problem.getRankVariable()[j][0], problem.getRankVariable()[j][1]) * bacterium.getStepSize();
                    //         bacterias[u][numVariables+k]=bacterias[u][k+numVariables]-bacterias[u][k+numVariables]*[gene/gmax]; //dinamico
                }
            }
    }

    /**
     * @return the generation
     */
    public int getGeneration() {
        return generation;
    }

    /**
     * @param value
     */
    public void increaseGeneration(int value) {
        this.setGeneration(this.generation + value);
    }

    /**
     * @param bacterium
     * @return the count
     * Retorna true o false si (contador + (sb * nc)) <= evaluaciones
     */
    public boolean getInstruction(Configurator bacterium) {
        return ((this.getCount() + (bacterium.getSb() * bacterium.getNc())) <= bacterium.getEvaluations()) ;        
    }

    /**
     * @param value the count to set
     */
    public void increaseCount(int value) {
        this.setCount(this.getCount() + value);
    }

    /**
     * @param generation the generation to set
     */
    public void setGeneration(int generation) {
        this.generation = generation;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }
    
}
