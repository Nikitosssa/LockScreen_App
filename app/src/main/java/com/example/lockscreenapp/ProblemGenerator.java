package com.example.lockscreenapp;

import java.util.Random;

public class ProblemGenerator {

    public String operationMark,
                  problemString;
    public int acceptAnswer;


    Random random = new Random();
    public void generate(int level){

        int firstNumber,
            secondNumber,
            operationValue;


        if (level == 1){
            firstNumber = random.nextInt(11);
            secondNumber = random.nextInt(10 - firstNumber);

            operationValue = random.nextInt(2); // 0 -> +, 1 -> -
            if (operationValue == 0) {
                operationMark = "+";
                acceptAnswer = firstNumber + secondNumber;
                problemString = firstNumber + operationMark + secondNumber;
            } else {
                operationMark = "-";
                acceptAnswer = Math.max(firstNumber, secondNumber) - Math.min(firstNumber, secondNumber);
                problemString = Math.max(firstNumber, secondNumber) +operationMark + Math.min(firstNumber, secondNumber);
            }
        }else if (level == 2){
            firstNumber = random.nextInt(51);
            secondNumber = random.nextInt(50 - firstNumber);

            operationValue = random.nextInt(2); // 0 -> +, 1 -> -
            if (operationValue == 0) {
                operationMark = "+";
                acceptAnswer = firstNumber + secondNumber;
                problemString = firstNumber + operationMark + secondNumber;
            } else{
                operationMark = "-";
                acceptAnswer = Math.max(firstNumber, secondNumber) - Math.min(firstNumber, secondNumber);
                problemString = Math.max(firstNumber, secondNumber) +operationMark + Math.min(firstNumber, secondNumber);
            }
        }else if (level == 3){
            operationValue = random.nextInt(4); // 0 -> +, 1 -> -,2 -> /, 3 -> *
            if (operationValue == 0 || operationValue == 1) {
                firstNumber = random.nextInt(21);
                secondNumber = random.nextInt(20 - firstNumber);
                if (operationValue == 1) {
                    operationMark = "+";
                    acceptAnswer = firstNumber + secondNumber;
                    problemString = firstNumber + operationMark + secondNumber;
                }else{
                    operationMark = "-";
                    acceptAnswer = Math.max(firstNumber, secondNumber) - Math.min(firstNumber, secondNumber);
                    problemString = Math.max(firstNumber, secondNumber) + operationMark + Math.min(firstNumber, secondNumber);
                }
            }else if (operationValue == 2){
                operationMark = "/";
                firstNumber = 0;
                secondNumber = 0;
                while (secondNumber == 0) {
                    secondNumber = random.nextInt(6);
                    firstNumber = random.nextInt(6) * secondNumber;
                    problemString = firstNumber + operationMark + secondNumber;
                }
                acceptAnswer = firstNumber / secondNumber;
            }else{ //operationValue = 3
                operationMark = "*";
                firstNumber = random.nextInt(11);
                secondNumber = 20 / firstNumber;
                problemString = firstNumber + operationMark + secondNumber;
                acceptAnswer = firstNumber * secondNumber;
            }
        }else if (level == 4){
            operationValue = random.nextInt(4); // 0 -> +, 1 -> -,2 -> /, 3 -> *
            if (operationValue == 0 || operationValue == 1) {
                firstNumber = random.nextInt(51);
                secondNumber = random.nextInt(50 - firstNumber);
                if (operationValue == 0) {
                    operationMark = "+";
                    acceptAnswer = firstNumber + secondNumber;
                    problemString = firstNumber + operationMark + secondNumber;
                }else {
                    operationMark = "-";
                    acceptAnswer = Math.max(firstNumber, secondNumber) - Math.min(firstNumber, secondNumber);
                    problemString = Math.max(firstNumber, secondNumber) + operationMark + Math.min(firstNumber, secondNumber);
                }
            }else if (operationValue == 2){
                operationMark = "/";
                secondNumber = 0;
                firstNumber = 0;
                while (secondNumber == 0) {
                    secondNumber = random.nextInt(8);
                    firstNumber = random.nextInt(8) * secondNumber;
                    problemString = firstNumber + operationMark + secondNumber;
                }
                acceptAnswer = firstNumber / secondNumber;
            }else { //operationValue = 3
                operationMark = "*";
                firstNumber = random.nextInt(26);
                secondNumber = 50 / firstNumber;
                problemString = firstNumber + operationMark + secondNumber;
                acceptAnswer = firstNumber * secondNumber;
            }
        }else if (level == 5) {
            operationValue = random.nextInt(4); // 0 -> +, 1 -> -,2 -> /, 3 -> *
            if (operationValue == 0 || operationValue == 1) {
                firstNumber = random.nextInt(101);
                secondNumber = random.nextInt(100 - firstNumber);
                if (operationValue == 0) {
                    operationMark = "+";
                    acceptAnswer = firstNumber + secondNumber;
                    problemString = firstNumber + operationMark + secondNumber;
                }else {
                    operationMark = "-";
                    acceptAnswer = Math.max(firstNumber, secondNumber) - Math.min(firstNumber, secondNumber);
                    problemString = Math.max(firstNumber, secondNumber) + operationMark + Math.min(firstNumber, secondNumber);
                }
            }else if (operationValue == 2){
                operationMark = "/";
                secondNumber = 0;
                while (secondNumber == 0) {
                    secondNumber = random.nextInt(10);
                    firstNumber = random.nextInt(10) * secondNumber;
                    problemString = firstNumber + operationMark + secondNumber;
                    acceptAnswer = firstNumber / secondNumber;
                }
            }else { //operationValue = 3
                operationMark = "*";
                firstNumber = random.nextInt(51);
                secondNumber = 100 / firstNumber;
                problemString = firstNumber + operationMark + secondNumber;
                acceptAnswer = firstNumber * secondNumber;
            }
        }
    }
}
