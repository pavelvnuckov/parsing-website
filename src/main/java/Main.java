import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        String domain = "https://skillbox.ru/";

        long start2 = System.currentTimeMillis();
        // Создаём первую ссылку
        Node2 node1 = new Node2(domain);
        // Создаём класс кторый находит в переданной странице ссылки
        Parser parser = new Parser();
        // Передаём в метод сайт на котором нужно искать ссылки
        parser.pars(node1);
        long finish2 = (System.currentTimeMillis() - start2) / 1000;

        // Записываем файл с деревом сайтов
        PrintWriter writer2 = new PrintWriter("filePars.txt");
        writer2.write(node1.toString());
        //Закрываем запись в файл иначе конец не дописывается
        writer2.close();
        System.out.println("Записал filePars");

        long start3 = System.currentTimeMillis();
        // Создаём первую ссылку
        Node2 node2 = new Node2(domain);
        // Создаём класс в котором происодит поиск ссылок
        ParsingPool parsingPool = new ParsingPool(node2);
        // Создаётся Pool потоков который будут обрабатывать class ParsingPool
        ForkJoinPool forkJoinPool2 = new ForkJoinPool();
        // Предаём в Pool класс который нужно обрабатывать
        forkJoinPool2.invoke(parsingPool);
        long finish3 = (System.currentTimeMillis() - start3) / 1000;

        //Запись файла с результатом работы Pool
        PrintWriter writer3 = new PrintWriter("filePoll2.txt");
        writer3.write(node2.toString());
        //Закрываем запись в файл иначе конец не дописывается
        writer3.close();
        System.out.println("Записал файл filePoll2");

        //Вывод времени сколько работал метод с 1 потоком
        System.out.println("Обработка Pars заняла - " + finish2);
        //Вывод времени сколько работал метод многопоточном режиме
        System.out.println("Обработка Pool2 заняла - " + finish3);
        //System.out.println("Обработка PoolTask заняла - " + finish4);
    }
}

