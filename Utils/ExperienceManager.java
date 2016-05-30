package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Klasa zawierająca metody do wczytywania i zapisywania zdobytego doświadczenia.
 */
public class ExperienceManager {

    private static int experience;

    /**
     * Wczytuje doświadczenie przy użyciu metody {@link #loadExperience()}.
     * @return Doświadczenie.
     */
    public static int getExperience(){
        loadExperience();
        return  experience;
    }

    /**
     * Odczytuje z pliku zdobyte wcześniej doświadczenie.
     * @return Doświadczenie.
     */
    public static int loadExperience()
    {
        String load = "";
        try {
            Scanner odczyt = new Scanner(new File("experience.txt"));
            load = odczyt.next();
            experience = Integer.parseInt(load);
            return experience;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Dodaje do poprzedniego doświadczenia to zdobyte podczas ostatniej rozgrywki i zapisuje nową wartość do pliku.
     * @param score Zdobyte doświadczenie.
     */
    public static void saveExperience(int score){
        experience += score;
        String f = "" + experience;
        try {
            File file = new File("experience.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(f);
            bw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
