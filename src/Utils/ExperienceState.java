package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Artur on 23.04.2016.
 */
public class ExperienceState {

    private int experience;

    public ExperienceState(){

        experience = this.loadExperience();
    }

    public int getExperience(){return  experience;}

    public int loadExperience()
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

    public void saveExperience(int score){
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
