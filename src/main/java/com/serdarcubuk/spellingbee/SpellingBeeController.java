package com.serdarcubuk.spellingbee;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class SpellingBeeController {

    private final int length = 7;
    @FXML
    private TextField kelimeText;
    @FXML
    private TextField girisText;
    @FXML
    private TextArea cevaplar;
    @FXML
    private Label hataText;
    @FXML
    private Label words;
    @FXML
    private Label scoreText;
    @FXML
    private Button Button1;
    @FXML
    private Button Button2;
    @FXML
    private Button Button3;
    @FXML
    private Button Button4;
    @FXML
    private Button Button5;
    @FXML
    private Button Button6;
    @FXML
    private Button Button7;
    private List<String> wordList;
    private int score = 0;
    private List<String> acceptedWords;
    private List<String> randomList;
    private List<String> answers;
    private String gameLetters;

    public void initialize() {
        acceptedWords = new ArrayList<>();
        randomList = new ArrayList<>();
        answers = new ArrayList<>();
        readWordList();
    }

    protected void readWordList() {
        try {
            wordList = Files.readAllLines(Path.of("src\\main\\resources\\com\\serdarcubuk\\spellingbee\\sozluk.txt"));
            for (String s : wordList) {
                Set<Character> pangram = s.chars().mapToObj(chr -> (char) chr).collect(Collectors.toSet());
                if (pangram.size() == length) {
                    String pangramString = pangram.toString().substring(1, 3 * pangram.size() - 1).replaceAll(", ", "");
                    randomList.add(pangramString);
                }
            }
        } catch (Exception e) {
            hataText.setText("Sözlük Okunamadı.\nDosya konumunu kontrol edin.");
        }
    }

    protected void possibleAnswers() {
        List<Character> girisList = gameLetters.chars().mapToObj(e -> (char) e).toList();
        for (int i = 0; i < wordList.size(); i++) {
            boolean answer = true;
            if (wordList.get(i).length() < 4) {
                continue;
            }

            for (int j = 0; j < wordList.get(i).length(); j++) {
                if (!girisList.contains(wordList.get(i).charAt(j))) {
                    answer = false;
                    break;
                }
            }
            if (answer) {
                answers.add(wordList.get(i));
            }
        }
    }

    protected boolean generatePuzzle(String letters) {
        if (letters.length() == length) {
            letters = letters.toUpperCase(Locale.ROOT);
            List<Character> alphabet = Arrays.asList('A', 'B', 'C', 'Ç', 'D', 'E', 'F', 'G', 'Ğ', 'H', 'I',
                    'İ', 'J', 'K', 'L', 'M', 'N', 'O', 'Ö', 'P', 'R', 'S', 'Ş', 'T', 'U', 'Ü', 'V', 'Y', 'Z');
            List<Character> vowel = Arrays.asList('A', 'E', 'I', 'İ', 'O', 'Ö', 'U', 'Ü');
            List<Character> puzzleLetters = new ArrayList<>();
            int vowelCount = 0;
            for (int i = 0; i < length; i++) {
                if (alphabet.contains(letters.charAt(i))) {
                    if (!puzzleLetters.contains(letters.charAt(i))) {
                        puzzleLetters.add(letters.charAt(i));
                        if (vowel.contains(letters.charAt(i))) {
                            vowelCount++;
                        }
                    } else {
                        hataText.setText("Lütfen Farklı\nharfler giriniz!");
                        return false;
                    }
                } else {
                    hataText.setText("Lütfen sadece Türkçe\nkarakter giriniz!");
                    return false;
                }
            }
            if (vowelCount < 1) {
                hataText.setText("Lütfen en az 1 sesli\nharf giriniz!");
                return false;
            }
            if (vowelCount > 5) {
                hataText.setText("Lütfen en az 2 sessiz\nharf giriniz!");
                return false;
            }
            return true;
        } else {
            hataText.setText("Lütfen 7 harf giriniz!");
            return false;
        }
    }

    protected void updateScore() {
        score += kelimeText.getText().length() - 3;
        scoreText.setText("Skor: " + score);
    }

    @FXML
    protected void onGirisButtonClick() {
        if (generatePuzzle(girisText.getText())) {
            Button1.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(0)));
            Button2.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(1)));
            Button3.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(2)));
            Button4.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(3)));
            Button5.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(4)));
            Button6.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(5)));
            Button7.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(6)));
            gameLetters = girisText.getText().toUpperCase(Locale.ROOT);
            hataText.setText("");
            score = 0;
            scoreText.setText("Skor: " + score);
            words.setText("");
            cevaplar.setText("");
        }
    }

    @FXML
    protected void onEnterButtonClick() {
        if (checkWord()) {
            words.setText(words.getText() + kelimeText.getText() + "\n");
        }
        kelimeText.setText("");
    }

    @FXML
    protected void onRandomButtonClick() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(randomList.size());
        String randomElement = randomList.get(randomIndex);
        girisText.setText(randomElement);
        onGirisButtonClick();
    }

    @FXML
    protected void onCevaplarButtonClick() {
        possibleAnswers();
        String result = answers.stream().
                map(i -> String.valueOf(i)).
                collect(Collectors.joining("\n"));
        cevaplar.setText(result);
    }

    protected boolean checkWord() {
        List<Character> girisList = gameLetters.chars().mapToObj(e -> (char) e).toList();
        for (int i = 0; i < kelimeText.getText().length(); i++) {
            if (!girisList.contains(kelimeText.getText().toUpperCase(Locale.ROOT).charAt(i))) {
                hataText.setText("Geçersiz Kelime!");
                return false;
            }
        }
        if (kelimeText.getText().length() < 4) {
            hataText.setText("En az 4 harfli\nkelime geçerlidir.");
            return false;
        }
        if (wordList.contains(kelimeText.getText().toUpperCase(Locale.ROOT))) {
            if (acceptedWords != null && acceptedWords.contains(kelimeText.getText())) {
                hataText.setText("Bu kelimeyi\nzaten buldunuz.");
                return false;
            }
            boolean pangram = false;
            if (acceptedWords != null)
                acceptedWords.add(kelimeText.getText());
            Set<Character> charsSet = kelimeText.getText().chars().mapToObj(e -> (char) e).collect(Collectors.toSet());
            if (charsSet.size() == length) {
                score += 7;
                pangram = true;
            }
            updateScore();
            if (pangram) {
                hataText.setText("Tebrikler!\n" + kelimeText.getText() + " PANGRAM " + (kelimeText.getText().length() + 4) + " Puan");
            } else {
                hataText.setText("Tebrikler!\n" + kelimeText.getText() + " " + (kelimeText.getText().length() - 3) + " Puan");
            }
            return true;
        } else {
            hataText.setText("Kelime Sözlükte Yok!");
            return false;
        }
    }

    @FXML
    protected void onButton1Click() {
        kelimeText.appendText(Button1.getText());
    }

    @FXML
    protected void onButton2Click() {
        kelimeText.appendText(Button2.getText());
    }

    @FXML
    protected void onButton3Click() {
        kelimeText.appendText(Button3.getText());
    }

    @FXML
    protected void onButton4Click() {
        kelimeText.appendText(Button4.getText());
    }

    @FXML
    protected void onButton5Click() {
        kelimeText.appendText(Button5.getText());
    }

    @FXML
    protected void onButton6Click() {
        kelimeText.appendText(Button6.getText());
    }

    @FXML
    protected void onButton7Click() {
        kelimeText.appendText(Button7.getText());
    }

    @FXML
    protected void onDeleteButtonClick() {
        if (!kelimeText.getText().isEmpty())
            kelimeText.deleteText(kelimeText.getText().length() - 1, kelimeText.getText().length());
    }

    @FXML
    protected void kelimeTextTyped() {
        kelimeText.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
    }

    @FXML
    protected void girisTextTyped() {
        girisText.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
    }

    @FXML
    protected void Karistir() {
        int[] array = {1, 2, 3, 4, 5, 6};
        int a = 6;
        Random rd = new Random();


        for (int i = a - 1; i > 0; i--) {

            int j = rd.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        Button2.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(array[0])));
        Button3.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(array[1])));
        Button4.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(array[2])));
        Button5.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(array[3])));
        Button6.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(array[4])));
        Button7.setText(String.valueOf(girisText.getText().toUpperCase(Locale.ROOT).charAt(array[5])));
    }
}