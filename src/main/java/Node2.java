import java.util.ArrayList;

public class Node2 {
    //Сайт
    private String path;
    //Нужен для того что бы записывать все сайты родители в этой ветке
    private ArrayList<String> listPath = new ArrayList<>();
    //Нужен для записи детей этого родителя
    private ArrayList<Node2> children;
    private int level = 0;

    private long thread;

    //Нужен для первого запуска так ещё нет списка с родителями
    public Node2(String path) {
        this.listPath.add(path);
        this.path = path;
        children = new ArrayList<>();
    }

    //Нужен для заведения детей у которых есть список с родителями
    public Node2(String path, ArrayList<String> listPath) {
        this.path = path;
        children = new ArrayList<>();
        this.listPath.addAll(listPath);
    }

    public long getThread() {
        return thread;
    }

    public void setThread(long thread) {
        this.thread = thread;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //Метод для добавления детей
    public void setChildren(Node2 node) {
        //Добавляем ребенка в список родителей так как на
        // следующем вложении он сам станет родителем
        this.listPath.add(node.getPath());
        //Изменяем уровень вложенности у ребёнка
        node.setLevel(this.level + 1);
        //Добавление в список детей
        children.add(node);
    }

    public ArrayList<Node2> getChildren() {
        return children;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<String> getListPath() {
        return listPath;
    }

    //Метод по сути идёт в глубину пока не будет дно
    // и редактирую вывод (необходимое кол-во отступов и т.д.)
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        //Добавляем в builder данные родителя
        builder.append(thread + "(" + level + ")" + path + "\n");
        //Вызываем у родителя список с детьми
        for (Node2 ch : getChildren()) {
            //У ребенка вызываем метод toString и указываем
            // необходимое кол-во отступов, что бы отобразить его
            builder.append(" ".repeat(ch.getLevel()) + ch);
        }
        return builder.toString();
    }
}
