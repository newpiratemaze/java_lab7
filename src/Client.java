import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Client {
    public String HOST = "localhost";
    private static final int PORT = 12345;

    public InputArgs params = new InputArgs();
    public void main(String[] args) {
        params = parseCmdArgs(args);
        HOST = params.ip;
        try (Socket socket = new Socket(HOST, PORT);
             // 1. Получаем низкоуровневый поток байт
             OutputStream rawOutput = socket.getOutputStream();
             InputStream rawInput = socket.getInputStream();

// 2. Добавляем буферизацию (для эффективности)
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(rawOutput);
             BufferedInputStream bufferedInput = new BufferedInputStream(rawInput);

// 3. Преобразуем байты ↔ символы (с кодировкой)
             OutputStreamWriter charOutput = new OutputStreamWriter(bufferedOutput, StandardCharsets.UTF_8);
             InputStreamReader charInput = new InputStreamReader(bufferedInput, StandardCharsets.UTF_8);

// 4. Добавляем удобные методы для работы со строками
             PrintWriter out = new PrintWriter(charOutput, true); // autoFlush автоматически сбрасывает буфер (с autoFlush=true)  и Не нужно вызывать flush() после каждой отправки
             BufferedReader in = new BufferedReader(charInput);
             //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Подключено к серверу. Вводите строки (Ctrl+D/Ctrl+C для выхода):");
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput); // Отправляем серверу
                String response = in.readLine(); // Читаем ответ
                System.out.println("Ответ сервера: " + response);
            }
        } catch (UnknownHostException e) {
            System.err.println("Неизвестный хост: " + HOST);
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        }
    }

    public InputArgs parseCmdArgs(String[] args) {
        InputArgs result = new InputArgs();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-ip" -> result.ip = args[++i];

            }
        }
        return result;
    }
}