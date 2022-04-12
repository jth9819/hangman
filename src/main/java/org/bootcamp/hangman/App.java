package org.bootcamp.hangman;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class App {
    static Scanner scanner = new Scanner(System.in);

    static ArrayList<Integer> letterIndexes = new ArrayList<>();
    static ArrayList<String> guesses = new ArrayList<>();
    static ArrayList<String> words = new ArrayList<>();

    static Random randomWord = new Random();
    static String chosenWord;
    static int wordLength;
    static int playerErrors = 0;
    static final int ERRORS_ALLOWED = 6;

    static JSONArray dataObject;

    public static void InputReplaceLetter() {
        System.out.print("Enter a letter: ");
        Character userCharacterInput = scanner.next().charAt(0);
        if (!chosenWord.contains(userCharacterInput.toString())) {
            playerErrors += 1;
            if (playerErrors < 6) {
                System.out.println("That was incorrect! Try again, you have " + (6 - playerErrors) + " mistakes left.");
            }else if (playerErrors == 6) {
                System.out.println("Game over.");
            }
        } else {
            System.out.println("That was correct!");
            for (int i = 0; i < wordLength; i++) {
                if (chosenWord.charAt(i) == (userCharacterInput)) {
                    letterIndexes.add(i);
                }
            }
            for (int j = 0; j < letterIndexes.size(); j++) {
                guesses.set(letterIndexes.get(j), String.valueOf(userCharacterInput));
            }
        }
        System.out.println(guesses);
        letterIndexes.clear();
    }

    public static void getConnection() {
        try {
            URL url = new URL("https://random-word-api.herokuapp.com/word");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner2 = new Scanner(url.openStream());

                while (scanner2.hasNext()) {
                    informationString.append(scanner2.nextLine());
                }

                scanner2.close();
//                System.out.println(informationString);

                JSONParser parse = new JSONParser();
                dataObject = (JSONArray) parse.parse(String.valueOf(informationString));

//                System.out.println(dataObject.get(0));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getWord() {
        getConnection();

        words.add(dataObject.get(0).toString());
        chosenWord = words.get(randomWord.nextInt(1));
        wordLength = chosenWord.length();

        for (int i = 0; i < wordLength; i++) {
            guesses.add("_");
        }
    }

    public static void drawHangman(int playerErrors) {
        switch (playerErrors) {
            case 1: {
                showOne();
                break;
            }
            case 2: {
                showTwo();
                break;
            }
            case 3: {
                showThree();
                break;
            }
            case 4: {
                showFour();
                break;
            }
            case 5: {
                showFive();
                break;
            }
            case 6: {
                showSix();
                break;
            }
            default: {
                break;
            }
        }
    }


    private static void showOne() {
        System.out.println("    ________");
        System.out.println("    |      \\|");
        System.out.println("    o       |");
        System.out.println("            |");
        System.out.println("            |");
        System.out.println("            |");
        System.out.println(" ___________|___");
        System.out.println(" |     1/6      |");
        System.out.println(" |              |");
    }

    private static void showTwo() {
        System.out.println("    ________");
        System.out.println("    |      \\|");
        System.out.println("    o       |");
        System.out.println("    |       |");
        System.out.println("    |       |");
        System.out.println("            |");
        System.out.println(" ___________|___");
        System.out.println(" |     2/6      |");
        System.out.println(" |              |");
    }

    private static void showThree() {
        System.out.println("    ________");
        System.out.println("    |      \\|");
        System.out.println("    o       |");
        System.out.println("    |\\      |");
        System.out.println("    |       |");
        System.out.println("            |");
        System.out.println(" ___________|___");
        System.out.println(" |     3/6      |");
        System.out.println(" |              |");
    }

    private static void showFour() {
        System.out.println("    ________");
        System.out.println("    |      \\|");
        System.out.println("    o       |");
        System.out.println("   /|\\      |");
        System.out.println("    |       |");
        System.out.println("            |");
        System.out.println(" ___________|___");
        System.out.println(" |     4/6      |");
        System.out.println(" |              |");
    }

    private static void showFive() {
        System.out.println("    ________");
        System.out.println("    |      \\|");
        System.out.println("    o       |");
        System.out.println("   /|\\      |");
        System.out.println("    |       |");
        System.out.println("   /        |");
        System.out.println(" ___________|___");
        System.out.println(" |     5/6      |");
        System.out.println(" |              |");
    }

    private static void showSix() {
        System.out.println("    ________");
        System.out.println("    |      \\|");
        System.out.println("    o       |");
        System.out.println("   /|\\      |");
        System.out.println("    |       |");
        System.out.println("   / \\      |");
        System.out.println(" ___________|___");
        System.out.println(" |    6/6       |");
        System.out.println(" |   R.I.P      |");
        System.out.println("GAME OVER!");
        System.out.println("The word was: " + chosenWord);
    }

    public static void checkIfWin()
    {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < guesses.size(); i++) {
            str.append(guesses.get(i));
        }

        if ((str.toString().equals(chosenWord)))
        {
            System.out.println("You win!");
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Hangman!");
        getWord();
        System.out.println("The word is: " + chosenWord);
        System.out.println(guesses);
        while (playerErrors < ERRORS_ALLOWED && guesses.contains("_")) {
            InputReplaceLetter();
            drawHangman(playerErrors);
            checkIfWin();
        }
    }
}
