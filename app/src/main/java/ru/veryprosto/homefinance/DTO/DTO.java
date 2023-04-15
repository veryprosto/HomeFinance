package ru.veryprosto.homefinance.DTO;

public class DTO {

    private String leftUp;
    private String rightUp;
    private String leftDown;
    private String rightDown;
    private int iconResource;

    private boolean parent = false;
    private String parentId = "";
    private boolean childVisibility = false;

    public DTO() {
    }

    public DTO(String leftUp, String rightUp, String leftDown, String rightDown, int iconResource) {
        this.leftUp = leftUp;
        this.rightUp = rightUp;
        this.leftDown = leftDown;
        this.rightDown = rightDown;
        this.iconResource = iconResource;
    }

    public DTO(String date) {
        this.leftUp = date;
    }

    public String getLeftUp() {
        return leftUp;
    }

    public void setLeftUp(String leftUp) {
        this.leftUp = leftUp;
    }

    public String getRightUp() {
        return rightUp;
    }

    public void setRightUp(String rightUp) {
        this.rightUp = rightUp;
    }

    public String getLeftDown() {
        return leftDown;
    }

    public void setLeftDown(String leftDown) {
        this.leftDown = leftDown;
    }

    public String getRightDown() {
        return rightDown;
    }

    public void setRightDown(String rightDown) {
        this.rightDown = rightDown;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isChildVisibility() {
        return childVisibility;
    }

    public void setChildVisibility(boolean childVisibility) {
        this.childVisibility = childVisibility;
    }
}

