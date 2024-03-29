import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class PoolTask extends RecursiveTask<List<String>> {
    String path;

    int level;
    ArrayList<String> rootPath = new ArrayList<>();

    ArrayList<String> list = new ArrayList<>();

    public PoolTask(String path, List<String> rootPath, int level) {
        this.path = path;
        this.rootPath.addAll(rootPath);
        this.level = level + 1;
    }
    public PoolTask(String path) {
        this.path = path;

    }

    @Override
    protected List<String> compute() {
        try {

            Document doc = Jsoup.connect(path).get();
            Elements el = doc.select("a");
//            System.out.println("Path - " + path);
//            System.out.println("el.size() - " + el.size());
            for (int i = 0; i < el.size(); i++) {
                ArrayList<PoolTask> subTask = new ArrayList<>();
                String currentSait = el.get(i).attr("href");
                //System.out.println(i + "--------------------------");

                String regex = "^https://skillbox.ru/.+";
                if (currentSait.matches(regex)) {
                    //System.out.println(currentSait);
                    if (!originalSait(rootPath, currentSait)) {

                        rootPath.add(currentSait);
                        list.add(" ".repeat(level) + "(" + level + ")" + currentSait + "\n");
//                        System.out.println("Добавил - " + Thread.currentThread().getId()
//                                + "("+ level + ")" + " Сайт - " + currentSait);
                        if (level <= 3) {
                            //System.out.println(rootPath.size());
                            PoolTask poolTask = new PoolTask(currentSait, rootPath, level);
                            poolTask.fork(); //Запускаем асинхронно
                            subTask.add(poolTask);
                        }
                    }
                }
                for (PoolTask n : subTask) {
                    n.join();
                }
            }

        } catch (IOException e) {
        }
        return list;
    }
    public boolean originalSait(List<String> rootPath, String currentSait) {
        boolean result = false;
        for(String n : rootPath) {
            if (n.equals(currentSait)) {
               // System.out.println("Совпало - " + n + " - " + currentSait);
                result = true;
                break;
            }
        }
        return result;
    }
}
