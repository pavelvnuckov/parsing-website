import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Parser {

    private Node2 node;

    public Parser() {
    }

    public void pars(Node2 nodes) {
        try {
            // Передаём адрес сайта который нужно пропарсить
            String address = nodes.getPath();
            Document doc = Jsoup.connect(address).get();
            //Получаем все элементы с CSS запросом
            Elements el = doc.select("a");
            //Перебираем все полученные значения
            el.forEach(e -> {
                //Получаем ссылку
                String currentSait = e.attr("href");
                String regex = "^https://skillbox.ru/.+";
                //Проверяем домен
                if (currentSait.matches(regex)) {
                    //Если домен подходит, то создаём очередной узел
                    Node2 child = new Node2(currentSait, nodes.getListPath());
                    //Проверяем если нет в этой ветке адреса то обрабатываем
                    if (!originalNode(child)) {

                        //Добавляем к сайту родителю ребенка
                        nodes.setChildren(child);
                        //Указываем в ребенке какой поток обработал
                        child.setThread(Thread.currentThread().getId());
                        System.out.println((Thread.currentThread().getId() + "(" + child.getLevel()
                                + ")" + child.getPath() + "\n"));
                        //Проверяем какое колено сейчас обрабатывается если максимальное, то глубже не идём
                        if (child.getLevel() <= 3) {
                            //Запускаем этот же метод только уже для ребенка, что бы идти глубже
                            pars(child);
                        }
                    }
                }
            });
        } catch (IOException e) {
        }
    }

    //Метод для проверки если текущий сайт в этой ветке, что бы избежать циклов и повторов
    public boolean originalNode(Node2 node2) {
        boolean result = false;
        //Перебираем список с сайтами в этой ветке
        for (String n : node2.getListPath()) {
            if (n.equals(node2.getPath())) {
                result = true;
                break;
            }
        }
        return result;
    }
}
