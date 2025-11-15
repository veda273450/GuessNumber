package GuessNumber;

import javax.swing.*;
import java.util.Random;


public class NumberGuessingGameGUI {
    public static void main(String[] args) {
        Random random = new Random();
        int totalScore = 0;
        int roundsPlayed = 0;

        // Choose difficulty
        String[] difficulties = {"Easy", "Medium", "Hard"};
        int difficultyChoice = JOptionPane.showOptionDialog(
            null,
            "Select Difficulty Level",
            "Difficulty",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            difficulties,
            difficulties[0]
        );

        int lowerBound = 1;
        int upperBound = 100;
        int maxAttempts = 7;

        switch (difficultyChoice) {
            case 0: // Easy
                upperBound = 50;
                maxAttempts = 10;
                break;
            case 1: // Medium
                upperBound = 100;
                maxAttempts = 7;
                break;
            case 2: // Hard
                upperBound = 200;
                maxAttempts = 5;
                break;
            default:
                JOptionPane.showMessageDialog(null, "No difficulty selected. Exiting.");
                return;
        }

        // Ask how many rounds
        String roundsInput = JOptionPane.showInputDialog(null, "How many rounds would you like to play?");
        if (roundsInput == null) return;
        int totalRounds;

        try {
            totalRounds = Integer.parseInt(roundsInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input for rounds. Exiting.");
            return;
        }

        for (int round = 1; round <= totalRounds; round++) {
            int numberToGuess = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
            boolean guessedCorrectly = false;

            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                String input = JOptionPane.showInputDialog(
                    null,
                    "🎯 Round " + round + "\nAttempt " + attempt + " of " + maxAttempts +
                    "\nGuess a number between " + lowerBound + " and " + upperBound + ":"
                );

                if (input == null) {
                    JOptionPane.showMessageDialog(null, "Game canceled.");
                    return;
                }

                int userGuess;
                try {
                    userGuess = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "❗ Invalid number. Try again.");
                    attempt--; // Invalid input doesn't count
                    continue;
                }

                if (userGuess == numberToGuess) {
                    int roundScore = (maxAttempts - attempt + 1) * 10;
                    totalScore += roundScore;
                    guessedCorrectly = true;
                    JOptionPane.showMessageDialog(null,
                        "🎉 Correct! You guessed it in " + attempt + " attempt(s).\nScore this round: " + roundScore
                    );
                    break;
                } else if (userGuess < numberToGuess) {
                    JOptionPane.showMessageDialog(null, "🔼 Too low!");
                } else {
                    JOptionPane.showMessageDialog(null, "🔽 Too high!");
                }
            }

            if (!guessedCorrectly) {
                JOptionPane.showMessageDialog(null,
                    "❌ Out of attempts! The correct number was: " + numberToGuess
                );
            }

            roundsPlayed++;

            // Ask if user wants to play next round
            if (round < totalRounds) {
                int playAgain = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to continue to the next round?",
                    "Continue?",
                    JOptionPane.YES_NO_OPTION
                );
                if (playAgain != JOptionPane.YES_OPTION) {
                    break;
                }
            }
        }

        double averageScore = (roundsPlayed == 0) ? 0 : (double) totalScore / roundsPlayed;

        JOptionPane.showMessageDialog(null,
            "🏁 Game Over!\nRounds Played: " + roundsPlayed +
            "\nTotal Score: " + totalScore +
            "\nAverage Score: " + String.format("%.2f", averageScore)
        );
    }
}

