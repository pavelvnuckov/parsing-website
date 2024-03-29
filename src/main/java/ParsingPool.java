import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.RecursiveAction;

public class ParsingPool extends RecursiveAction {
    private Node2 node;
    //Список задач для дальнейшей обработки


    public ParsingPool(Node2 node) {
        this.node = node;
        // this.subTask
    }

    @Override
    protected void compute() {

        try {

            // Передаём адрес сайта который нужно пропарсить
            String address = node.getPath();
            Document doc = Jsoup.connect(address).get();
            //Получаем все элементы с CSS запросом
            Elements el = doc.select("a");
            LinkedList<ParsingPool> subTask = new LinkedList<>();
            //Перебираем все полученные значения
            el.forEach(e -> {
                //Получаем ссылку
                String currentSait = e.attr("href");
                String regex = "^https://skillbox.ru/.+";
                //Проверяем домен
                if (currentSait.matches(regex)) {
                    //Если домен подходит, то создаём очередной узел
                    Node2 child = new Node2(currentSait, node.getListPath());
                    //Проверяем если нет в этой ветке адреса то обрабатываем
                    if (!originalNode(child)) {
                        //Добавляем к сайту родителю ребенка
                        node.setChildren(child);
                        //Указываем в ребенке какой поток обработал
                        child.setThread(Thread.currentThread().getId());
                        //Проверяем какое колено сейчас обрабатывается если максимальное, то глубже не идём
                        if (child.getLevel() <= 3) {
                            //Создаём задачу, что нужно идти глубже
                            // т.е вызывать этот же метод для ребенка
                            ParsingPool parsingPool = new ParsingPool(child);
                            parsingPool.fork(); //Запускаем асинхронно
                            subTask.add(parsingPool); //Добавляем задачу в список задач
                        }
                    }
                }

            });
            for (ParsingPool n : subTask) {
                //Собираем результат из всех задач после их окончания
                //т.е пока не закончат свою обработку дети результата не будет
                n.join();
            }

        } catch (IOException e) {
        }
        //Меняем список задач из Array в Linked
        //3.Перенесли список задач в try и цикл перенесли в try (перед catch) ---> ошибка в итоговом файле
        //2.Перенесли список задач из конструктора в compute
        //1.Последнее изменение перенос из цикла списка задач и список задач создаётся в конструкторе
        //1.Перебираем список задач

    }

    //Метод проверки уникальности сайта в этой ветке
    public boolean originalNode(Node2 node2) {
        boolean result = false;
        //Перебираем все сайты в этой ветке
        for (String n : node2.getListPath()) {
            if (n.equals(node2.getPath())) {
                result = true;
                break;
            }
        }
        return result;
    }
}
