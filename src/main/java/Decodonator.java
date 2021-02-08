import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Decodonator {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);

        //src/main/resources/EncryptedDirectory.txt
        System.out.print("Enter path to file \n(Example: src/main/resources/EncryptedDirectory.txt )\nHere: ");
        File rootDir = new File(in.nextLine());

        System.out.print("Enter Key word \n(Example: Mother)\nHere: ");
        String key = in.nextLine();
        char[] LatinosAlph = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k','l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
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

        String delimiter = "`";
        String secondDelimiter = "\n";
        FileReader reader = new FileReader(rootDir);
        BufferedReader bufferedReader = new BufferedReader(reader);

        StringBuilder text = new StringBuilder();
        String LoneleyReader = bufferedReader.readLine();
        text.append(LoneleyReader);
        while (LoneleyReader != null) {
            LoneleyReader = bufferedReader.readLine();
            if (LoneleyReader != null) {text.append("\n" + LoneleyReader);}
        }
        bufferedReader.close();
        reader.close();
        List<String> encryptedDirectory = Arrays.asList(text.toString().split(delimiter));

        String EDirectoryPath = encryptedDirectory.get(0);
        String preparedKey = key.repeat(EDirectoryPath.length() / key.length() + 1)
                .substring(0, EDirectoryPath.length());
        StringBuilder DirectoryFinalDecodedPath = new StringBuilder();
        for(int i = 0; i < EDirectoryPath.length(); i++) {
            int index = 0;
            char[] lineInMatrix = DoubleLatinosMatrix[Arrays.binarySearch(LatinosAlph, preparedKey.charAt(i))];
            for (int j = 0; j < lineInMatrix.length; j++) {
                if(lineInMatrix[j] == EDirectoryPath.charAt(i)) {
                    index = j;
                    break;
                }
            }
            DirectoryFinalDecodedPath.append(DoubleLatinosMatrix[0][index]);
        }

        File root = new File(DirectoryFinalDecodedPath.toString());
        root.mkdir();

        for(int i = 1; i < encryptedDirectory.size(); i++) {
            String CurrientFile = encryptedDirectory.get(i);
            int FlagVar = Integer.parseInt(CurrientFile.substring(0, 1));
            CurrientFile = CurrientFile.substring(1);
            DirectoryFinalDecodedPath.delete(0, DirectoryFinalDecodedPath.length());

            if (FlagVar == 0) {
                preparedKey = key.repeat(CurrientFile.length() / key.length() + 1)
                        .substring(0, CurrientFile.length());
                for(int j = 0; j < CurrientFile.length(); j++) {
                    int index = 0;
                    char[] lineInMatrix = DoubleLatinosMatrix[Arrays.binarySearch(LatinosAlph, preparedKey.charAt(j))];
                    for (int k = 0; k < lineInMatrix.length; k++) {
                        if(lineInMatrix[k] == CurrientFile.charAt(j)) {
                            index = k;
                            break;
                        }
                    }
                    DirectoryFinalDecodedPath.append(DoubleLatinosMatrix[0][index]);
                }
                File file = new File(DirectoryFinalDecodedPath.toString());
                file.mkdir();
            }
            else {
                String[] BytesOfPath = CurrientFile.split(secondDelimiter);
                EDirectoryPath = BytesOfPath[0];
                preparedKey = key.repeat(EDirectoryPath.length() / key.length() + 1)
                        .substring(0, EDirectoryPath.length());
                for(int j = 0; j < EDirectoryPath.length(); j++) {
                    int index = 0;
                    char[] lineInMatrix = DoubleLatinosMatrix[Arrays.binarySearch(LatinosAlph, preparedKey.charAt(j))];
                    for (int k = 0; k < lineInMatrix.length; k++) {
                        if(lineInMatrix[k] == EDirectoryPath.charAt(j)) {
                            index = k;
                            break;
                        }
                    }
                    DirectoryFinalDecodedPath.append(DoubleLatinosMatrix[0][index]);
                }
                File file = new File(DirectoryFinalDecodedPath.toString());
                file.createNewFile();

                String BytesVar = BytesOfPath[1];
                StringBuilder DecodedBytes = new StringBuilder();
                preparedKey = key.repeat(BytesVar.length() / key.length() + 1)
                        .substring(0, BytesVar.length());
                for(int j = 0; j < BytesVar.length(); j++) {
                    int index = 0;
                    char[] lineInMatrix = DoubleLatinosMatrix[Arrays.binarySearch(LatinosAlph, preparedKey.charAt(j))];
                    for (int k = 0; k < lineInMatrix.length; k++) {
                        if(lineInMatrix[k] == BytesVar.charAt(j)) {
                            index = k;
                            break;
                        }
                    }
                    DecodedBytes.append(DoubleLatinosMatrix[0][index]);
                }

                FileWriter writer = new FileWriter(file);
                String message = DecodedBytes.toString();
                writer.write(message);
                writer.close();
            }
        }
        rootDir.delete();
    }
}
