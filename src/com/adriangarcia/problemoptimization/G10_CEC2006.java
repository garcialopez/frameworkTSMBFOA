/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adriangarcia.problemoptimization;

import com.adriangarcia.metaheuristics.tsmbfoa.Configurator;
import com.adriangarcia.metaheuristics.tsmbfoa.Problem;

/**
 * 
 * @author José Adrian
 */
public class G10_CEC2006 extends Problem{

    public G10_CEC2006() {
        this.setBestKnownValue(7049.2480205286); 
        this.setNameProblem("G10_CEC2006");
        this.setFunction("x1 + x2 + x3");                   //Función objetivo
        this.setOrderVariables("iter{x,1,8}");             //Orden de sus variables        
        this.setObj(Problem.MINIMIZATION);                      // Objetivo -> min o max        
        this.setRankVariable("(100,10000);ran{2:3,1000:10000};ran{4:8,10:1000}"); //Se inserta el rango para cada variable.
        //Se aplica el metodo para detectar las restricciones
        String constraints = "{-1 + 0.0025 * (x4 + x6) <= 0};"
                           + "{-1 + 0.0025 * (x5 + x7 - x4) <= 0};"
                           + "{-1 + 0.01 * (x8 - x5) <= 0};"
                           + "{-x1 * x6 + 833.33252 * x4 + 100 * x1 - 83333.333 <= 0};"
                           + "{-x2 * x7 + 1250 * x5 + x2 * x4 - 1250 * x4 <= 0};"
                           + "{-x3 * x8 + 1250000 + x3 * x5 - 2500 * x5 <= 0}";
        this.detectConstraints(constraints);
    }
    
    @Override
    public Configurator getRecommendedSetting() {
        this.configurator = new Configurator();
        /**
         * Inicia la calibración de parámetros del algoritmo...
         */
        this.configurator.setSb(13);            /* Se establece la cantidad de bacterias 
                                                    para la población inicial.         */
        this.configurator.setStepSize(0.0005);    // Se establece el tamaño de paso.        
        this.configurator.setNc(6);            /* Se establece el número de ciclos         
                                                   quimiotáxicos.                     */
        this.configurator.setScalingFactor(1.95);      // Se establece el factor de escalamiento.      
        this.configurator.setBacteriaReproduce(1);          /* Se establece el número de bacterias 
                                                   a reproducir.                      */
        this.configurator.setRepcycle(100);     // Frecuencia de reproducción
        this.configurator.setEvaluations(30000);       // Número de evaluaciones   

        return this.configurator;
    }

}
