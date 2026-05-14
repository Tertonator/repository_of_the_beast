package com.particlesdu.pripravanaquadterm;

public class Instruction {
    // Typ inštrukcie, napríklad STEP, TURN, FORWARD...
    public InstructionType type;

    // Znamienko argumentu:
    // '+' znamená pripočítať
    // '-' znamená odpočítať / záporný pohyb
    // ' ' znamená nastaviť hodnotu
    public char sign;

    // Číselná hodnota argumentu bez znamienka
    public int value;

    public Instruction(String text) {

        // Odstránime zbytočné medzery na začiatku a na konci
        text = text.trim();

        // Rozdelíme inštrukciu podľa medzier
        // napríklad "STEP +5" -> ["STEP", "+5"]
        String[] parts = text.split("\\s+");

        // Prvá časť je názov inštrukcie
        type = InstructionType.valueOf(parts[0]);

        // Druhá časť je argument
        String argument = parts[1];

        // Ak argument začína znamienkom + alebo -
        if (argument.charAt(0) == '+' || argument.charAt(0) == '-') {

            // Uložíme si znamienko
            sign = argument.charAt(0);

            // Uložíme si číselnú hodnotu bez znamienka
            value = Integer.parseInt(argument.substring(1));

        } else {

            // Ak argument nemá znamienko,
            // znamená to nastavenie hodnoty
            sign = ' ';

            // Uložíme celé číslo
            value = Integer.parseInt(argument);
        }
    }

    // Táto metóda sa používa pre premenné ako:
    // SLEEP, COLOR, STEP, TURN
    //
    // Ak bola inštrukcia napríklad:
    // STEP 10   -> nastaví hodnotu na 10
    // STEP +5   -> zvýši starú hodnotu o 5
    // STEP -3   -> zníži starú hodnotu o 3
    public int applyTo(int oldValue) {

        if (sign == '+') {
            return oldValue + value;
        }

        if (sign == '-') {
            return oldValue - value;
        }

        return value;
    }

    // Táto metóda je hlavne pre FORWARD.
    //
    // FORWARD 5  -> ide dopredu 5 * stepSize
    // FORWARD +5 -> tiež ide dopredu 5 * stepSize
    // FORWARD -5 -> ide dozadu 5 * stepSize
    public int signedValue() {

        if (sign == '-') {
            return -value;
        }

        return value;
    }
}
