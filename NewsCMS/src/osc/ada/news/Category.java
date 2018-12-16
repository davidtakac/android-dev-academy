package osc.ada.news;

public enum Category {
    BREAKING, CURRENT, EDUCATION, POLITICS, MEDIA, TECH, WEATHER, OTHER;

    public static String categoriesToString() {
        String result = "";
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            result += "[" + i + "]";
            result += categories[i] + " ";
        }
        return result;
    }

    public static Category getCategoryAt(int index) {
        Category[] categories = Category.values();
        return categories[index];
    }
}
