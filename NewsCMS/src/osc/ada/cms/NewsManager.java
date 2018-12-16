package osc.ada.cms;

import java.util.ArrayList;
import java.util.List;
import osc.ada.news.*;

public class NewsManager {

    private List<News> newsList;

    public NewsManager() {
        newsList = new ArrayList<>();
    }

    public void addNews(News news) {
        newsList.add(news);
    }

    public void removeNews(News news) {
        newsList.remove(news);
    }

    public List<News> getAllNews() {
        return newsList;
    }

    public List<News> getNewsByAuthor(String author) {
        ArrayList<News> results = new ArrayList<>();
        for (News n : newsList) {
            if (n.getAuthor().equals(author)) {
                results.add(n);
            }
        }
        return results;
    }

    public List<News> getNewsByCategory(Category category) {
        ArrayList<News> results = new ArrayList<>();
        for (News n : newsList) {
            if (n.containsCategory(category)) {
                results.add(n);
            }
        }
        return results;
    }

    public List<News> getNewsByDate(String date) {
        ArrayList<News> results = new ArrayList<>();
        for (News n : newsList) {
            if (n.getDate().equals(date)) {
                results.add(n);
            }
        }
        return results;
    }

    public News getNewsAt(int index) {
        return newsList.get(index);
    }

    public void printNews(List<News> news) {
        for (int i = 0; i < news.size(); i++) {
            System.out.println(" [" + (i) + "]");
            System.out.println(news.get(i));
            System.out.println();
        }
    }

    public void printAllNews() {
        printNews(newsList);
    }
}
