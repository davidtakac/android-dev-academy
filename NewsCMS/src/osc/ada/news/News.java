package osc.ada.news;

import java.util.ArrayList;
import java.util.List;

public class News {

    private String title;
    private String content;
    private String author;
    private String date;
    private List<Category> categories;

    public News(String title, String content, String author, String date) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
        this.categories = new ArrayList<>();
    }

    //region GET/SET
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    //end region

    //region CATEGORY OPERATIONS
    public void addCategory(Category category) {
        categories.add(category);
    }

    public boolean containsCategory(Category category) {
        for (Category c : categories) {
            if (c == category) {
                return true;
            }
        }
        return false;
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }

    /**
     * Adds categories that correspond to the category indices. The method is
     * presented with the category indices that are to be added. It iterates
     * through all the indices and gets their corresponding Category value,
     * which it adds to the News object(if it's not already added).
     */
    public void addCategories(List<Integer> categoryIndices) {
        for (int index : categoryIndices) {
            Category currentCat = Category.getCategoryAt(index);
            if (!this.containsCategory(currentCat)) {
                this.addCategory(currentCat);
            }
        }
    }

    /**
     * Removes categories that correspond to the category indices. The method is
     * presented with the category indices that are to be removed. It iterates
     * through all the indices and gets their corresponding Category value. If
     * the News object contains the category, it removes it.
     */
    public void removeCategories(List<Integer> categoryIndices) {
        for (int index : categoryIndices) {
            Category currentCat = Category.getCategoryAt(index);
            if (this.containsCategory(currentCat)) {
                this.removeCategory(currentCat);
            }
        }
    }
    //end region

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Category c : categories) {
            sb.append(c);
            sb.append(", ");
        }
        String categoriesToPrint = sb.toString();
        if (categoriesToPrint.isEmpty()) {
            categoriesToPrint = "";
        }
        return " Title: " + title + "\n"
                + " Content: " + content + "\n"
                + " Author: " + author + "\n"
                + " Date: " + date + "\n"
                + " Categories: " + categoriesToPrint.substring(0, categoriesToPrint.length() - 2);
    }
}
