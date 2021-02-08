import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Encryptionator {
    public static void main(String[] args) throws Exception {
        String QuotationMarkInStringVar = "`";
        String EndLineInStringVar = "\n";
        Scanner in = new Scanner(System.in);

        System.out.print("Enter the path to the folder \n(Example: D:\\TestForSecurity ) \nHere: ");
        String filePath = in.nextLine();
        File rootDirectory = new File(filePath);

        System.out.print("Create Key word \n(Example: Mother) \nHere: ");
        String keyWord = in.nextLine();

        //D:\TestForSecurity

        char[] LatinosAlph = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '!', '@', '\"', '#', '№', ';', '$', '%', ':', '^', '&', '?', '*', '(', ')', '-', '_', '+', '=', '[',
                '{', ']', '}', '\\', '|', '/', '\'', ',', '<', '.', '>'};
        Arrays.sort(LatinosAlph);

        char[][] DoubleLatinosMatrix = new char[LatinosAlph.length][LatinosAlph.length];
        for (int i = 0, k; i < DoubleLatinosMatrix.length; i++) {
            k = i;
            for (int j = 0; j < DoubleLatinosMatrix[i].length; j++) {
                DoubleLatinosMatrix[i][j] = LatinosAlph[k];
                k++;
                if (k == LatinosAlph.length) {k = 0;}
            }
        }

        List<String> EndList = new ArrayList<>();
        Queue<File> filesTree = new PriorityQueue<>();

        String preparedKey = keyWord.repeat(filePath.length() / keyWord.length() + 1)
                .substring(0, filePath.length());

        StringBuilder encodedFile = new StringBuilder();
        for (int i = 0; i < filePath.length(); i++) {
            encodedFile.append(DoubleLatinosMatrix[Arrays.binarySearch(LatinosAlph,
                    preparedKey.charAt(i))][Arrays.binarySearch(LatinosAlph, filePath.charAt(i))]);
        }
        EndList.add(encodedFile.toString());

        Collections.addAll(filesTree, rootDirectory.listFiles());
        while (!filesTree.isEmpty()) {
            File currientFileOrDir = filesTree.remove();
            String pathCurFile = currientFileOrDir.getPath();
            encodedFile.delete(0, encodedFile.length());

            preparedKey = keyWord.repeat(pathCurFile.length() / keyWord.length() + 1)
                    .substring(0, pathCurFile.length());
            for(int i = 0; i < pathCurFile.length(); i++) {
                encodedFile.append(DoubleLatinosMatrix[Arrays.binarySearch(LatinosAlph,
                        preparedKey.charAt(i))][Arrays.binarySearch(LatinosAlph, pathCurFile.charAt(i))]);
            }
            if (currientFileOrDir.isDirectory()) {
                Collections.addAll(filesTree, currientFileOrDir.listFiles());

                EndList.add("0" + encodedFile.toString());
            }
            else
            {
                String PathWord = encodedFile.toString();

                String curFileString = Files.readString(Path.of(pathCurFile));
                preparedKey = keyWord.repeat(curFileString.length() / keyWord.length() + 1)
                        .substring(0, curFileString.length());
                encodedFile.delete(0, encodedFile.length());
                for(int i = 0; i < curFileString.length(); i++) {
                    encodedFile.append(DoubleLatinosMatrix[Arrays.binarySearch(LatinosAlph,
                            preparedKey.charAt(i))][Arrays.binarySearch(LatinosAlph, curFileString.charAt(i))]);
                }
                EndList.add("1" + PathWord + EndLineInStringVar + encodedFile.toString());
            }
        }

        File file = new File("src/main/resources/EncryptedDirectory.txt");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        for(int i = 0; i < EndList.size(); i++) {
            bufferedWriter.write(EndList.get(i) + QuotationMarkInStringVar);
        }
        bufferedWriter.close();
        fileWriter.close();

        Files.walk(rootDirectory.toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}