package com.example.playertool5e.ui.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DiceRollParser {
    //String syntax: XnY
    //d = keep Y lowest rolls
    //k = keep Y highest rolls
    //r = re-roll values equal to or lower than Y
    //s = count amount of rolls equal to or above Y

    private final Random rand;

    public DiceRollParser() {
        this.rand = new Random();
    }

    public String evaluate(String str) {
        String[] strings = str.split("(?<=,) | (?=,)");

        StringBuilder sb = new StringBuilder();

        for (String string : strings) {
            if (string.charAt(0) == ',') {
                string = string.substring(1, string.length() - 1);
                string = calculateDiceRoll(string);
            }
            sb.append(string);
            sb.append(" ");
        }
        return sb.toString();
    }

    public String calculateDiceRoll(String str) {
        StringBuilder sb = new StringBuilder();

        String[] strings = str.split("((?<=[d+a._])|(?=[d+_a.]))");
        boolean showIndividualDice = strings[0].equals("_");
        //System.out.println(Arrays.asList(strings).toString());
        int total = 0;

        for (int i = 0; i < strings.length; i++) {
            String current = strings[i];

            if (Objects.equals(current, "+")) {
                int roll = Integer.parseInt(strings[i + 1]);
                sb.append(roll).append(" + ");
                total = total + roll;
            } else if (Objects.equals(current, "d")) {
                int roll = rollDice(strings[i + 1], Integer.parseInt(strings[i - 1]), sb, showIndividualDice);
                total = total + roll;
            }
        }

        sb.delete(sb.length() - 3, sb.length());
        sb.append(")");
        sb.insert(0, total + " (");

        return sb.toString();
    }

    private int rollDice(String str, int amount, StringBuilder sb, boolean showIndividualDice) {
        //String syntax: XnY
        //d = keep Y lowest rolls
        //k = keep Y highest rolls
        //r = re-roll values equal to or lower than Y
        //s = count amount of rolls equal to or above Y

        String[] strings = str.split("((?<=[lhrs])|(?=[lhrs]))");

        int total = 0;
        int diceSize = Integer.parseInt(strings[0]);

        if (strings.length == 1) {
            for (int i = 0; i < amount; i++) {
                total = total + rand.nextInt(diceSize) +1;
            }
            sb.append(total).append(" + ");
        } else {
            int n = Integer.parseInt(strings[2]);
            String current = strings[1];

            switch (current) {
                case "l": {

                    ArrayList<Integer> results = new ArrayList<>();
                    for (int k = 0; k < amount; k++) {
                        results.add(rand.nextInt(diceSize) + 1);
                    }

                    Collections.sort(results);
                    Collections.reverse(results);
                    List<Integer> subList = results.subList(results.size() - n, results.size());

                    if (showIndividualDice) {
                        sb.append(subList).append(" = ");
                    }

                    //int roll = Collections.min(results);
                    for (Integer roll : subList) {

                        total = total + roll;
                    }
                    sb.append(total).append(" + ");

                    break;
                }
                case "h": {
                    ArrayList<Integer> results = new ArrayList<>();
                    for (int k = 0; k < amount; k++) {
                        results.add(rand.nextInt(diceSize) + 1);
                    }

                    Collections.sort(results);
                    List<Integer> subList = results.subList(results.size() - n, results.size());

                    //int roll = Collections.min(results);
                    if (showIndividualDice) {
                        sb.append(subList).append(" = ");
                    }
                    for (Integer roll : subList) {

                        total = total + roll;
                    }

                    sb.append(total).append(" + ");
                    break;
                }
                case "r": {
                    ArrayList<Integer> results = new ArrayList<>();
                    for (int i = 0; i < amount; i++) {
                        int roll = rand.nextInt(diceSize) + 1;
                        if (roll <= n) {

                            roll = rand.nextInt(diceSize) + 1;
                        }
                        results.add(roll);
                    }
                    if (showIndividualDice) {
                        sb.append(results).append(" = ");
                    }
                    for (Integer roll : results) {
                        total = total + roll;
                    }
                    sb.append(total).append(" + ");
                    break;
                }
                case "s": {
                    ArrayList<Integer> results = new ArrayList<>();
                    for (int i = 0; i < amount; i++) {
                        int roll = rand.nextInt(diceSize) + 1;
                        if (roll >= n) {
                            results.add(roll);
                        }
                    }
                    if (showIndividualDice) {
                        sb.append(results).append(" = ");
                    }
                    total = results.size();
                    sb.append(total).append(" + ");
                    break;
                }
            }
        }
        return total;
    }
}