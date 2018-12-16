package osc.ada.cms;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import osc.ada.news.*;

public class CmsInterface {

    private Scanner reader;
    private NewsManager manager;

    public CmsInterface() {
        reader = new Scanner(System.in);
        manager = new NewsManager();
    }

    //region CORE METHODS
    public void start() {
        while (true) {
            printMenu();
            int command = readIntBetween(0, 6);

            switch (command) {
                case 1:
                    System.out.println("--CREATE NEWS--");
                    System.out.print("Return?(y/n) ");
                    if (readString().equals("y")) {
                        break;
                    }
                    createNews();
                    break;
                case 2:
                    System.out.println("--READ NEWS--");
                    System.out.print("Return?(y/n) ");
                    if (readString().equals("y")) {
                        break;
                    }
                    readNews();
                    break;
                case 3:
                    System.out.println("--UPDATE NEWS--");
                    System.out.print("Return?(y/n) ");
                    if (readString().equals("y")) {
                        break;
                    }
                    updateNews();
                    break;
                case 4:
                    System.out.println("--DELETE NEWS--");
                    System.out.print("Return?(y/n) ");
                    if (readString().equals("y")) {
                        break;
                    }
                    deleteNews();
                    break;
                case 5:
                    break;
                default:
                    System.out.println(" Unrecognised command! Try again ");
                    break;
            }
            if (command == 5) {
                break;
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--SYSTEM MENU--");
        System.out.println(" [1]Create news");
        System.out.println(" [2]Read news");
        System.out.println(" [3]Update news");
        System.out.println(" [4]Delete news");
        System.out.println(" [5]Quit");
    }

    private void createNews() {
        System.out.print("Enter title ");
        String title = readString();
        System.out.print("Enter content ");
        String content = readString();
        System.out.print("Enter author ");
        String author = readString();
        System.out.print("Enter date ");
        String date = readString();

        News news = new News(title, content, author, date);

        List<Integer> categoryIndices = getCategoryIndicesFromUser();
        news.addCategories(categoryIndices);

        manager.addNews(news);
    }

    private void readNews() {
        List<News> found = searchForNews();

        System.out.println(" Results:\n");
        if (found.isEmpty()) {
            System.out.println(" Not found!");
        } else {
            manager.printNews(found);
        }
    }

    private void updateNews() {
        List<News> found = askForSearchTermsUntilFound();
        manager.printNews(found);

        System.out.print("Choose news to update(e.g. 2) ");
        News newsToUpdate;
        while (true) {
            try {
                newsToUpdate = found.get(readInt());
                break;
            } catch (IndexOutOfBoundsException e) {
                System.out.print("No such news! Try again ");
            }
        }

        System.out.println(" [1]Title [2]Content [3]Author [4]Date [5]Categories");
        System.out.print("Enter what you want to update(1-5) ");

        switch (readIntBetween(0, 6)) {
            case 1:
                System.out.print("New title ");
                newsToUpdate.setTitle(readString());
                break;
            case 2:
                System.out.print("New content ");
                newsToUpdate.setContent(readString());
                break;
            case 3:
                System.out.print("New author ");
                newsToUpdate.setAuthor(readString());
                break;
            case 4:
                System.out.print("New date ");
                newsToUpdate.setDate(readString());
                break;
            case 5:
                System.out.println(" [1]Add category/categories [2]Remove category/categories");
                System.out.print("Enter number(1-2) ");
                switch (readIntBetween(0, 3)) {
                    case 1:
                        List<Integer> categoriesToAdd = getCategoryIndicesFromUser();
                        newsToUpdate.addCategories(categoriesToAdd);
                        break;
                    case 2:
                        List<Integer> categoriesToRemove = getCategoryIndicesFromUser();
                        newsToUpdate.removeCategories(categoriesToRemove);
                        break;
                    default:
                        System.out.println("Unrecognised command!");
                        break;
                }
                break;
            default:
                System.out.println("Unrecognised command!");
                break;
        }
    }

    private void deleteNews() {
        List<News> found = askForSearchTermsUntilFound();
        manager.printNews(found);

        System.out.print("Choose news to delete(e.g. 2) ");
        News newsToRemove;
        while (true) {
            try {
                newsToRemove = found.get(readInt());
                break;
            } catch (IndexOutOfBoundsException e) {
                System.out.print("No such news! Try again ");
            }
        }
        manager.removeNews(newsToRemove);
    }
    //end region

    //region CORE HELPER METHODS
    /**
     * Gets a list of integers from the user input which correspond to the
     * indices of the enum class "Category". The user is presented with a choice
     * of categories with 7 to choose from. He must enter his chosen categories
     * in the correct format and this method takes care of it.
     *
     * For example, the categories to choose from are: [0]BREAKING [1]CURRENT
     * [2]EDUCATION [3]POLITICS [4]MEDIA [5]TECH [6]WEATHER [7]OTHER. The user
     * is forced to choose numbers from 1-7 in a format "5,1,2,7". The method
     * then returns a list of integers: [5,1,2,7].
     */
    private List<Integer> getCategoryIndicesFromUser() {
        System.out.println("\n " + Category.categoriesToString());
        System.out.print("Enter categories(e.g. 1,2,3) ");

        String[] splitString;
        ArrayList<Integer> categoryIndices;
        while (true) {
            try {
                categoryIndices = new ArrayList<>();
                splitString = readString().split(",");
                for (String s : splitString) {
                    int i = Integer.parseInt(s);
                    //The user is forced to enter a number in the <0,8> range
                    //because there are only 7 categories. 
                    if (!(i > 0 && i < 8)) {
                        throw new IllegalArgumentException();
                    }
                    categoryIndices.add(i);
                }
                break;
            } catch (NumberFormatException e) {
                System.out.print("Can't parse string into numbers, try again(e.g. 1,2,3) ");
            } catch (IllegalArgumentException e) {
                System.out.print("Invalid input, try again(1-7) ");
            }
        }
        return categoryIndices;
    }

    /**
     * Searches for News in the NewsManager based on user input. If no such News
     * are found, returns an empty List.
     */
    private List<News> searchForNews() {
        System.out.println(" Search by: [1]Author [2]Category [3]Date [4]Read All");
        System.out.print("Enter number(1-4) ");

        List<News> found = new ArrayList<>();
        switch (readIntBetween(0, 5)) {
            case 1:
                System.out.print("Author name ");
                found = manager.getNewsByAuthor(readString());
                break;
            case 2:
                System.out.println("\n " + Category.categoriesToString());
                System.out.print("Pick a category ");
                found = manager.getNewsByCategory(Category.getCategoryAt(readIntBetween(0, 8)));
                break;
            case 3:
                System.out.print("Date ");
                found = manager.getNewsByDate(readString());
                break;
            case 4:
                found = manager.getAllNews();
                break;
            default:
                System.out.println("Unrecognised command! ");
                return new ArrayList<>();
        }
        return found;
    }

    /**
     * Asks the user for search terms until the resulting List of News isn't
     * empty. Used in the updateNews() and deleteNews() methods, where it's
     * important that the user has at least one News object to choose from.
     */
    private List<News> askForSearchTermsUntilFound() {
        List<News> found;
        do {
            found = searchForNews();
            if (found.isEmpty()) {
                System.out.println(" Not found! Try again ");
            }
        } while (found.isEmpty());
        return found;
    }
    //end region

    //region USER INPUT PROCESSING
    private String readString() {
        System.out.print("> ");
        return reader.nextLine();
    }

    private int readInt() {
        int number;
        while (true) {
            try {
                number = Integer.parseInt(readString());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Not a number, try again ");
            }
        }
        return number;
    }

    //readIntBetween(0,6) will return an integer in range <0,6>
    private int readIntBetween(int lower, int upper) {
        int result;
        do {
            result = readInt();
            if (!(result > lower && result < upper)) {
                System.out.print("Invalid input, try again(" + lower + "-" + upper + ") ");
            }
        } while (!(result > lower && result < upper));
        return result;
    }
    //end region
}
