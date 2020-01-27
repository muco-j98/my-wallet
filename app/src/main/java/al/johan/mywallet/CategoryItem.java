package al.johan.mywallet;

public class CategoryItem {
    private String categoryName;
    private int categoryImage;

    public CategoryItem(String categoryName, int categoryImage) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryImage() {
        return categoryImage;
    }
}
