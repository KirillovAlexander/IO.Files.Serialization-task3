import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    static final String SAVE_PATH = "F://Netology.Games//savegames";

    public static void main(String[] args) {
        openZip(SAVE_PATH + "//saves.zip", SAVE_PATH + "//");

        File dir = new File(SAVE_PATH);
        List<GameProgress> gameProgresses = new ArrayList<>();
        for (File item : dir.listFiles()) {
            if (isSaveFile(item.getName())) {
                GameProgress progress = openProgress(SAVE_PATH + "//" + item.getName());
                if (progress != null) gameProgresses.add(progress);
            }
        }

        for (GameProgress progress : gameProgresses) {
            System.out.println(progress);
        }

    }

    private static void openZip(String zipPath, String destination) {
        try (FileInputStream fis = new FileInputStream(zipPath);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            String name;
            while ((entry = zis.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(destination + name);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fout.write(c);
                }
                fout.flush();
                zis.closeEntry();
                fout.close();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static GameProgress openProgress(String progressPath) {
        GameProgress gameProgress = null;

        try (FileInputStream fis = new FileInputStream(progressPath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }

    private static boolean isSaveFile(String name) {
        return name.contains(".dat");
    }
}
